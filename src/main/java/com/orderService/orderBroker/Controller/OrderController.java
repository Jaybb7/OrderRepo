package com.orderService.orderBroker.Controller;

import com.orderService.orderBroker.Enums.OrderStatus;
import com.orderService.orderBroker.Model.OrdersDTO;
import com.orderService.orderBroker.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Authenticator;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    @PostMapping("/getOrders")
    public ResponseEntity<List<OrdersDTO>> getOrders(@RequestParam Long userId) {
        try {
            logger.info("Received request to fetch orders for userId: {}", userId);
            List<OrdersDTO> list = orderService.getOrdersForUser(userId);
            System.out.println(list);
            logger.info("Successfully retrieved {} orders for userId: {}", list.size(), userId);
            return ResponseEntity.ok(list);
        } catch (Exception ex) {
            logger.error("Error while fetching orders for userId: {} - {}", userId, ex.getMessage(), ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/modifyOrder")
    public ResponseEntity<Boolean> modifyOrder(
            @RequestParam Long orderId,
            @RequestParam OrderStatus orderStatus,
            @RequestParam String comment,
            @RequestHeader(value = "CorrelationId", required = false) String correlationId) {
        try {
            logger.info("Received modifyOrder request with Correlation ID: {} for orderId: {}", correlationId, orderId);

            OrdersDTO order = orderService.findOrder(orderId);
            if (order == null) {
                logger.warn("No order found for orderId: {} [CorrelationId={}]", orderId, correlationId);
                return ResponseEntity.badRequest().body(false);
            }

            var final_comment = LocalDateTime.now() + comment;
            order.setOrderStatus(orderStatus);
            order.getComments().add(final_comment);

            Boolean flag = orderService.saveOrder(order);
            logger.info("Order modification result for orderId: {} [CorrelationId={}]: {}", orderId, correlationId, flag);
            return ResponseEntity.ok(flag);

        } catch (Exception ex) {
            logger.error("Error modifying orderId: {} [CorrelationId={}]: {}", orderId, correlationId, ex.getMessage(), ex);
            return ResponseEntity.internalServerError().body(false);
        }
    }
}