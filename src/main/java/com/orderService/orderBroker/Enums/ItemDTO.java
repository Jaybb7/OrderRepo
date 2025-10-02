package com.orderService.orderBroker.Enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private long id;
    private String itemName;
    private int quantity;
    private int price;


}
