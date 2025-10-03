package com.orderService.orderBroker.Repository;

import com.orderService.orderBroker.Entity.DeliveryDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriver, Long> {

    @Query("SELECT d FROM DeliveryDriver d LEFT JOIN d.shipments s " + "GROUP BY d " + "ORDER BY COUNT(s) ASC")
    DeliveryDriver findDriverWithLeastShipments();


}
