package com.orderService.orderBroker.Model;

import com.orderService.orderBroker.Entity.DeliveryDriver;
import com.orderService.orderBroker.Enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDTO {

    private Long shipmentId;
    private Long orderId;
    private Long paymentId;
    private ShipmentStatus shipmentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime expectedDate;
    private DeliveryDriver deliveryDriver;

}
