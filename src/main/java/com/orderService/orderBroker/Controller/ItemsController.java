package com.orderService.orderBroker.Controller;

import com.orderService.orderBroker.Entity.Items;
import com.orderService.orderBroker.Service.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemsController {

    private final ItemsService itemsService;

    @GetMapping("/getItems")
    public ResponseEntity<List<Items>> getItems() {
        List<Items> items = itemsService.getAllItems();
        return ResponseEntity.ok(items);
    }

}
