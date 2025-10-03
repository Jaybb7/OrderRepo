package com.orderService.orderBroker.Model;

import com.orderService.orderBroker.Entity.Items;
import com.orderService.orderBroker.Enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDTO {

    private long orderId;
    private long userId;
    private OrderStatus orderStatus;
    private Items item;


}
