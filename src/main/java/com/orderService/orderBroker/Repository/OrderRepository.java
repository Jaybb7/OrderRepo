package com.orderService.orderBroker.Repository;

import com.orderService.orderBroker.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
