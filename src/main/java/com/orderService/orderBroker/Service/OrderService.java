package com.orderService.orderBroker.Service;

import com.orderService.orderBroker.Entity.Items;
import com.orderService.orderBroker.Entity.Orders;
import com.orderService.orderBroker.Enums.OrderStatus;
import com.orderService.orderBroker.Model.OrderRequest;
import com.orderService.orderBroker.Repository.ItemsRepository;
import com.orderService.orderBroker.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ItemsRepository itemsRepository;

    private final Logger logger = Logger.getLogger(OrderService.class.getName());

    @KafkaListener(topics = "create_order", groupId = "order-broker-group")
    public void placeOrder(OrderRequest orderRequest) {
        Optional<Items> item = itemsRepository.findById(orderRequest.getItemId());
        if(item.isPresent()) {
            System.out.println(orderRequest.getItemId() + " " + orderRequest.getOrderQuantity());
            if(item.get().getQuantity() >= orderRequest.getOrderQuantity()){
                item.get().setQuantity(item.get().getQuantity() - orderRequest.getOrderQuantity());
                itemsRepository.save(item.get());
                Orders orders = new Orders();
                orders.setUserId(orderRequest.getUserId());
                orders.setOrderStatus(OrderStatus.PROCESSING);
                orders.setItem(item.get());
                orderRepository.save(orders);
            }else{
                Orders orders = new Orders();
                orders.setUserId(orderRequest.getUserId());
                orders.setOrderStatus(OrderStatus.FAILED);
                orderRepository.save(orders);
            }
        }else{
            System.out.println("Item not found");
        }
    }

}
