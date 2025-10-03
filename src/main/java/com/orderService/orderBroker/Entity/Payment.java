package com.orderService.orderBroker.Entity;

import com.orderService.orderBroker.Enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private Long orderId;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

}
