package com.orderService.orderBroker.Repository;

import com.orderService.orderBroker.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
