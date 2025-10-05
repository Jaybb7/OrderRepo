package com.orderService.orderBroker.Service;

import com.orderService.orderBroker.Entity.Items;
import com.orderService.orderBroker.Repository.ItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemsService {

    private final ItemsRepository itemsRepository;

    public List<Items> getAllItems() {
        return itemsRepository.findAll();
    }

}
