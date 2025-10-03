package com.orderService.orderBroker.Model;

import com.orderService.orderBroker.Entity.Shipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDriverDTO {

    private String driverId;
    private String driverName;
    private String driverPhone;
    private String driverEmail;
    private String driverAddress;
    private List<Shipment> shipments = new ArrayList<>();

}
