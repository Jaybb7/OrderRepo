package com.orderService.orderBroker.Service;

import com.orderService.orderBroker.Entity.Orders;
import com.orderService.orderBroker.Entity.Payment;
import com.orderService.orderBroker.Enums.PaymentStatus;
import com.orderService.orderBroker.Repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripePaymentService extends PaymentService {

    private final PaymentRepository paymentRepository;

    public StripePaymentService(PaymentRepository paymentRepository) {
        super(paymentRepository);
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Map<Boolean, Long> processPayment(Orders order) {
        Payment payment = new Payment();
        payment.setOrderId(order.getOrderId());
        payment.setUserId(order.getUserId());
        payment.setPaymentStatus(PaymentStatus.APPROVED);
        paymentRepository.save(payment);
        System.out.println("Stripe payment processed");
        Map<Boolean, Long> map = new HashMap<>();
        map.put(true, payment.getPaymentId());
        return map;
    }

}
