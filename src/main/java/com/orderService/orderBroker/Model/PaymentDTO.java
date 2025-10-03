package com.orderService.orderBroker.Model;

import com.orderService.orderBroker.Enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private Long paymentId;
    private Long orderId;
    private Long userId;
    private PaymentStatus paymentStatus;


}
