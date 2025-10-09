package com.orderService.orderBroker.Repository;

import com.orderService.orderBroker.Entity.DeliveryDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriver, Long> {

    @Query(value = "SELECT d.* FROM delivery_driver d " +
            "LEFT JOIN shipment s ON d.driver_id = s.driver_id " +
            "GROUP BY d.driver_id " +
            "ORDER BY COUNT(s.shipment_id) ASC " +
            "LIMIT 1",
            nativeQuery = true)
    DeliveryDriver findDriverWithLeastShipments();

    @Query(value = "SELECT * FROM delivery_driver ORDER BY ratings DESC LIMIT 10", nativeQuery = true)
    List<DeliveryDriver> findHighestRatedDriver();




}
