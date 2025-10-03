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
public class Laptop extends Items {

    private String processor;
    private int ramSize;   // in GB
    private int storage;   // in GB
}
