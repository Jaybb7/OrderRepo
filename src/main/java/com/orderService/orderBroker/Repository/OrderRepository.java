package com.orderService.orderBroker.Repository;

import com.orderService.orderBroker.Entity.Orders;
import com.orderService.orderBroker.Model.OrdersDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByUserId(long userId);
}
