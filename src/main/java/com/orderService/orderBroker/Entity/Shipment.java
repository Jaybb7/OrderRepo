package com.orderService.orderBroker.Entity;

import com.orderService.orderBroker.Enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipmentId;
    private Long orderId;
    private Long paymentId;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime expectedDate;
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private DeliveryDriver deliveryDriver;


}
