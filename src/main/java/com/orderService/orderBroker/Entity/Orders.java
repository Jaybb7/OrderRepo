package com.orderService.orderBroker.Entity;

import com.orderService.orderBroker.Enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    private long userId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @ManyToOne
    private Items item;
    @ElementCollection
    private List<String> comments = new ArrayList<>();

}
