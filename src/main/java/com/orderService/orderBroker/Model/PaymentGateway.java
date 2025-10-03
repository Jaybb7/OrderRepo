package com.orderService.orderBroker.Model;

public abstract class PaymentGateway<T> {

    public abstract T processPayment();

}
