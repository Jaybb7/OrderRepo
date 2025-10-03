package com.orderService.orderBroker.Entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobilePhone extends Items {

    private String brand;
    private int batteryCapacity; // in mAh
    private boolean has5G;
}
