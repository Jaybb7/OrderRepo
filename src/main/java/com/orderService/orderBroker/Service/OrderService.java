package com.orderService.orderBroker.Service;

import com.orderService.orderBroker.Entity.Items;
import com.orderService.orderBroker.Entity.Orders;
import com.orderService.orderBroker.Enums.ItemDTO;
import com.orderService.orderBroker.Enums.OrderStatus;
import com.orderService.orderBroker.Model.OrderRequest;
import com.orderService.orderBroker.Repository.ItemsRepository;
import com.orderService.orderBroker.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ItemsRepository itemsRepository;

    private final Logger logger = Logger.getLogger(OrderService.class.getName());

    @KafkaListener(topics = "create_order", groupId = "order-broker-group")
    public void placeOrder(OrderRequest orderRequest) {
        logger.info("Received order request for item: " + orderRequest.getItemName() + " with quantity: " + orderRequest.getOrderQuantity() + " from user: " + orderRequest.getUserId());
        Items item = itemsRepository.findByItemName(orderRequest.getItemName());
        logger.info("Fetched item details: " + item);
        ItemDTO itemDTO = new ItemDTO();
        BeanUtils.copyProperties(item, itemDTO);
        if(itemDTO.getQuantity() >= orderRequest.getOrderQuantity()){
            logger.info("Sufficient stock available. Processing order...");
            item.setQuantity(item.getQuantity() - orderRequest.getOrderQuantity());
            itemsRepository.save(item);
            logger.info("Updated item quantity. Remaining stock: " + item.getQuantity());
            Orders orders = new Orders();
            orders.setUserId(orderRequest.getUserId());
            orders.setOrderStatus(OrderStatus.PROCESSING);
            logger.info("Order marked as PROCESSING for user: " + orderRequest.getUserId());
            orderRepository.save(orders);
        }else{
            logger.warning("Insufficient stock for item: " + orderRequest.getItemName() + ". Order FAILED for user: " + orderRequest.getUserId());
            Orders orders = new Orders();
            orders.setUserId(orderRequest.getUserId());
            orders.setOrderStatus(OrderStatus.FAILED);
            orderRepository.save(orders);
        }
    }

}
