package com.orderService.orderBroker.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "shipments")
public class DeliveryDriver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;
    private String driverName;
    private String driverPhone;
    private String driverEmail;
    private String driverAddress;
    @OneToMany(mappedBy = "deliveryDriver", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Shipment> shipments = new ArrayList<>();

}
