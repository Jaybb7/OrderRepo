package com.orderService.orderBroker.Service;

import com.orderService.orderBroker.Model.PaymentGateway;
import org.springframework.stereotype.Service;

@Service
public class PaymentService extends PaymentGateway<String> {

    @Override
    public String processPayment() {
        return "Payment Successful";
    }
}
