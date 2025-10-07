package com.orderService.orderBroker.Model;

import com.orderService.orderBroker.Entity.Items;
import com.orderService.orderBroker.Enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDTO {

    private long orderId;
    private long userId;
    private OrderStatus orderStatus;
    private Items item;
    private List<String> comments = new ArrayList<>();


}
