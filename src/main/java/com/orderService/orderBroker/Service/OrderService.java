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
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    /**
     * Listens to the "create_order" Kafka topic and processes the order including validation, payment, and shipment creation.
     *
     * @param orderRequest the order details received from Kafka
     */
    @KafkaListener(topics = "create_order", groupId = "order-broker-group")
    @Transactional
    public void placeOrder(@Payload OrderRequest orderRequest, @Header("correlationId") String correlationId) {
        MDC.put("correlationId", correlationId);
        logger.info("Start processing order for user {}, correlationId={}", orderRequest.getUserId(), correlationId);
        logger.info("Received order request for itemId: {} from userId: {}", orderRequest.getItemId(), orderRequest.getUserId());
        try {
            Optional<Items> item = itemsRepository.findById(orderRequest.getItemId());
            if (item.isEmpty()) {
                logger.warn("Item not found for ID: {}, correlationId={}", orderRequest.getItemId(), correlationId);
                return;
            }

            Items foundItem = item.get();
            logger.info("Item found: {} | Quantity available: {}, correlationId={}", foundItem.getClass().getSimpleName(), foundItem.getQuantity(), correlationId);

            if (foundItem.getQuantity() < orderRequest.getOrderQuantity()) {
                logger.warn("Insufficient quantity for itemId: {}, requested: {}, available: {}, correlationId={}",
                        orderRequest.getItemId(), orderRequest.getOrderQuantity(), foundItem.getQuantity(), correlationId);
                return;
            }

            foundItem.setQuantity(foundItem.getQuantity() - orderRequest.getOrderQuantity());
            itemsRepository.saveAndFlush(foundItem);
            logger.info("Updated item quantity for itemId: {}, new quantity: {}, correlationId={}",
                    orderRequest.getItemId(), foundItem.getQuantity(), correlationId);

            Orders order = new Orders();
            order.setUserId(orderRequest.getUserId());
            order.setOrderStatus(OrderStatus.STARTED);
            order.setItem(foundItem);
            Orders savedOrder = orderRepository.save(order);
            logger.info("Order saved successfully with ID: {}, correlationId={}", savedOrder.getOrderId(), correlationId);

            Map<Boolean, Long> paymentResult = stripePaymentService.processPayment(savedOrder);
            if (paymentResult == null || paymentResult.isEmpty()) {
                logger.error("Payment processing returned no result for orderId: {}, correlationId={}", savedOrder.getOrderId(), correlationId);
                throw new RuntimeException("Payment processing failed - empty response.");
            }

            Map.Entry<Boolean, Long> entry = paymentResult.entrySet().iterator().next();
            Boolean paymentSuccess = entry.getKey();
            Long paymentId = entry.getValue();

            if (Boolean.TRUE.equals(paymentSuccess)) {
                logger.info("Payment successful. Payment ID: {}. Proceeding to shipment creation. correlationId={}", paymentId, correlationId);
                ShipmentDTO shipment = new ShipmentDTO();
                shipment.setOrderId(savedOrder.getOrderId());
                shipment.setShipmentStatus(ShipmentStatus.PENDING);
                shipment.setCreatedAt(LocalDateTime.now());
                shipment.setDeliveryDriver(deliveryDriverService.findADriver());
                shipment.setPaymentId(paymentId);

                shipmentService.createShipment(shipment);
                logger.info("Shipment created successfully for orderId: {}, correlationId={}", savedOrder.getOrderId(), correlationId);
            } else {
                logger.warn("Payment failed for order ID: {}. No shipment will be created. correlationId={}", savedOrder.getOrderId(), correlationId);
            }

            logger.info("Finished processing order for orderId: {}, correlationId={}", savedOrder.getOrderId(), correlationId);

        } catch (Exception e) {
            logger.error("Error processing order request for itemId: {}, correlationId={}", orderRequest.getItemId(), correlationId, e);
            throw e; // Ensures transaction rollback
        } finally {
            MDC.clear();
        }
    }
}
