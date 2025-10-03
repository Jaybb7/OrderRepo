package com.orderService.orderBroker.Repository;

import com.orderService.orderBroker.Entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsRepository extends JpaRepository<Items, Long> {

}
