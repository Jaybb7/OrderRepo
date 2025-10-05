package com.orderService.orderBroker.Service;

import com.orderService.orderBroker.Entity.Orders;
import com.orderService.orderBroker.Entity.Payment;
import com.orderService.orderBroker.Enums.PaymentStatus;
import com.orderService.orderBroker.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public abstract class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentStatus checkPaymentStatus(Payment payment) {
        return paymentRepository.findById(payment.getPaymentId())
                .map(Payment::getPaymentStatus)
                .orElse(null);
    }

    public abstract Map<Boolean, Long> processPayment(Orders order);

}
