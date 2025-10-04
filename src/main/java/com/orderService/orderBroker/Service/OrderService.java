package com.orderService.orderBroker.Service;

import com.orderService.orderBroker.Entity.Items;
import com.orderService.orderBroker.Entity.Orders;
import com.orderService.orderBroker.Enums.OrderStatus;
import com.orderService.orderBroker.Enums.ShipmentStatus;
import com.orderService.orderBroker.Model.OrderRequest;
import com.orderService.orderBroker.Model.ShipmentDTO;
import com.orderService.orderBroker.Repository.ItemsRepository;
import com.orderService.orderBroker.Repository.OrderRepository;
import com.orderService.orderBroker.Repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ItemsRepository itemsRepository;

    private final ShipmentService shipmentService;

    private final DeliveryDriverService deliveryDriverService;

    private final Logger logger = Logger.getLogger(OrderService.class.getName());
    private final ShipmentRepository shipmentRepository;

    @KafkaListener(topics = "create_order", groupId = "order-broker-group")
    public void placeOrder(OrderRequest orderRequest) {
        try {
            logger.info("Received order request for itemId: " + orderRequest.getItemId());
            Optional<Items> item = itemsRepository.findById(orderRequest.getItemId());
            if(item.isPresent()) {
                logger.info(orderRequest.getItemId() + " " + orderRequest.getOrderQuantity());
                if(item.get().getQuantity() >= orderRequest.getOrderQuantity()){
                    item.get().setQuantity(item.get().getQuantity() - orderRequest.getOrderQuantity());
                    itemsRepository.save(item.get());
                    Orders orders = new Orders();
                    orders.setUserId(orderRequest.getUserId());
                    orders.setOrderStatus(OrderStatus.PROCESSING);
                    orders.setItem(item.get());
                    Orders savedOrder = orderRepository.save(orders);
                    logger.info("Order saved with ID: " + savedOrder.getOrderId());
                    createShipment(savedOrder);
                }else{
                    logger.warning("Insufficient quantity for itemId: " + orderRequest.getItemId());
                }
            }else{
                logger.warning("Item not found for id: " + orderRequest.getItemId());
            }
        } catch (Exception e) {
            logger.severe("Error placing order: " + e.getMessage());
            throw e;
        }
    }

    public void createShipment(Orders savedOrder) {
        try {
            logger.info("Creating shipment for orderId: " + savedOrder.getOrderId());
            ShipmentDTO shipment = new ShipmentDTO();
            shipment.setOrderId(savedOrder.getOrderId());
            shipment.setShipmentStatus(ShipmentStatus.PENDING);
            shipment.setCreatedAt(LocalDateTime.now());
            shipment.setDeliveryDriver(deliveryDriverService.findADriver());
            shipmentService.createShipment(shipment);
        } catch (Exception e) {
            logger.severe("Error creating shipment: " + e.getMessage());
            throw e;
        }
    }

}
