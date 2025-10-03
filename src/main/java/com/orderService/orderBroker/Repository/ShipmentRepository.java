package com.orderService.orderBroker.Repository;

import com.orderService.orderBroker.Entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
