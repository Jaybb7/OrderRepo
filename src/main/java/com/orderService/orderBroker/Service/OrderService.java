package com.orderService.orderBroker.Service;

import com.orderService.orderBroker.Entity.Items;
import com.orderService.orderBroker.Entity.Orders;
import com.orderService.orderBroker.Enums.OrderStatus;
import com.orderService.orderBroker.Enums.ShipmentStatus;
import com.orderService.orderBroker.Model.OrderRequest;
import com.orderService.orderBroker.Model.ShipmentDTO;
import com.orderService.orderBroker.Repository.ItemsRepository;
import com.orderService.orderBroker.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class responsible for handling order placement and shipment creation.
 * Listens to Kafka messages for order requests, processes payments, and triggers shipment creation.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemsRepository itemsRepository;
    private final ShipmentService shipmentService;
    private final DeliveryDriverService deliveryDriverService;
    private final StripePaymentService stripePaymentService;
    private final Logger logger = Logger.getLogger(OrderService.class.getName());

    /**
     * Listens to the "create_order" Kafka topic and processes the order including validation, payment, and shipment creation.
     *
     * @param orderRequest the order details received from Kafka
     */
    @KafkaListener(topics = "create_order", groupId = "order-broker-group")
    @Transactional
    public void placeOrder(OrderRequest orderRequest) {
        logger.info("Received order request for itemId: " + orderRequest.getItemId() + " from userId: " + orderRequest.getUserId());
        try {
            Optional<Items> item = itemsRepository.findById(orderRequest.getItemId());
            if (item.isEmpty()) {
                logger.warning("Item not found for ID: " + orderRequest.getItemId());
                return;
            }

            Items foundItem = item.get();
            logger.info("Item found: " + foundItem.getClass().getSimpleName() + " | Quantity available: " + foundItem.getQuantity());

            if (foundItem.getQuantity() < orderRequest.getOrderQuantity()) {
                logger.warning("Insufficient quantity for itemId: " + orderRequest.getItemId());
                return;
            }

            foundItem.setQuantity(foundItem.getQuantity() - orderRequest.getOrderQuantity());
            itemsRepository.save(foundItem);
            logger.info("Updated item quantity for itemId: " + orderRequest.getItemId());

            Orders order = new Orders();
            order.setUserId(orderRequest.getUserId());
            order.setOrderStatus(OrderStatus.STARTED);
            order.setItem(foundItem);
            Orders savedOrder = orderRepository.save(order);
            logger.info("Order saved successfully with ID: " + savedOrder.getOrderId());

            Map<Boolean, Long> paymentResult = stripePaymentService.processPayment(savedOrder);
            if (paymentResult == null || paymentResult.isEmpty()) {
                logger.severe("Payment processing returned no result for orderId: " + savedOrder.getOrderId());
                throw new RuntimeException("Payment processing failed - empty response.");
            }

            Map.Entry<Boolean, Long> entry = paymentResult.entrySet().iterator().next();
            Boolean paymentSuccess = entry.getKey();
            Long paymentId = entry.getValue();

            if (Boolean.TRUE.equals(paymentSuccess)) {
                logger.info("Payment successful. Payment ID: " + paymentId + ". Proceeding to shipment creation.");
                ShipmentDTO shipment = new ShipmentDTO();
                shipment.setOrderId(savedOrder.getOrderId());
                shipment.setShipmentStatus(ShipmentStatus.PENDING);
                shipment.setCreatedAt(LocalDateTime.now());
                shipment.setDeliveryDriver(deliveryDriverService.findADriver());
                shipment.setPaymentId(paymentId);

                shipmentService.createShipment(shipment);
                logger.info("Shipment created successfully for orderId: " + savedOrder.getOrderId());
            } else {
                logger.warning("Payment failed for order ID: " + savedOrder.getOrderId() + ". No shipment will be created.");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing order request for itemId: " + orderRequest.getItemId(), e);
            throw e; // Ensures transaction rollback
        }
    }
}
