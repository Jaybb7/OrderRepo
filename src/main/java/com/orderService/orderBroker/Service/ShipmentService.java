package com.orderService.orderBroker.Service;

import com.orderService.orderBroker.Entity.DeliveryDriver;
import com.orderService.orderBroker.Entity.Shipment;
import com.orderService.orderBroker.Enums.ShipmentStatus;
import com.orderService.orderBroker.Model.ShipmentDTO;
import com.orderService.orderBroker.Repository.DeliveryDriverRepository;
import com.orderService.orderBroker.Repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    private final DeliveryDriverRepository deliveryDriverRepository;

    private final DeliveryDriverService deliveryDriverService;

    private final Logger logger = LoggerFactory.getLogger(ShipmentService.class);

    public ShipmentDTO createShipment(ShipmentDTO shipmentDTO) {
        logger.info("Creating shipment with details: {}", shipmentDTO);
        try {
            DeliveryDriver driver = deliveryDriverService.findADriver();
            shipmentDTO.setDeliveryDriver(driver);
            Shipment shipment = new Shipment();
            BeanUtils.copyProperties(shipmentDTO, shipment);
            Shipment savedShipment = shipmentRepository.save(shipment);
            logger.info("Shipment created successfully with ID: {}", savedShipment.getShipmentId());
            return shipmentDTO;
        } catch (Exception ex) {
            logger.error("Error while creating shipment: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to create shipment", ex);
        }
    }

    public boolean cancelShipment(Long shipmentId) {
        logger.info("Cancelling shipment with ID: {}", shipmentId);
        try {
            Optional<Shipment> shipmentOpt = shipmentRepository.findById(shipmentId);
            if (shipmentOpt.isPresent()) {
                shipmentRepository.deleteById(shipmentId);
                logger.info("Shipment with ID: {} cancelled successfully", shipmentId);
                return true;
            } else {
                logger.warn("Shipment with ID: {} not found", shipmentId);
                return false;
            }
        } catch (Exception ex) {
            logger.error("Error while cancelling shipment with ID: {}", shipmentId, ex);
            throw new RuntimeException("Failed to cancel shipment", ex);
        }
    }

    public boolean changeShipmentStatus(Long shipmentId, ShipmentStatus shipmentStatus) {
        logger.info("Changing shipment status for ID: {} to {}", shipmentId, shipmentStatus);
        try {
            return shipmentRepository.findById(shipmentId).map(shipment -> {
                shipment.setShipmentStatus(shipmentStatus);
                shipmentRepository.save(shipment);
                logger.info("Shipment ID: {} status changed to {}", shipmentId, shipmentStatus);
                return true;
            }).orElseGet(() -> {
                logger.warn("Shipment with ID: {} not found", shipmentId);
                return false;
            });
        } catch (Exception ex) {
            logger.error("Error while changing shipment status for ID: {}", shipmentId, ex);
            throw new RuntimeException("Failed to change shipment status", ex);
        }
    }

    public boolean assignDriver(Long shipmentId, Long driverId) {
        logger.info("Assigning driver ID: {} to shipment ID: {}", driverId, shipmentId);
        try {
            return shipmentRepository.findById(shipmentId).map(shipment -> {
                return deliveryDriverRepository.findById(driverId).map(driver -> {
                    shipment.setDeliveryDriver(driver);
                    shipmentRepository.save(shipment);
                    logger.info("Driver ID: {} assigned to shipment ID: {}", driverId, shipmentId);
                    return true;
                }).orElseGet(() -> {
                    logger.warn("Driver with ID: {} not found", driverId);
                    return false;
                });
            }).orElseGet(() -> {
                logger.warn("Shipment with ID: {} not found", shipmentId);
                return false;
            });
        } catch (Exception ex) {
            logger.error("Error assigning driver ID: {} to shipment ID: {}", driverId, shipmentId, ex);
            throw new RuntimeException("Failed to assign driver", ex);
        }
    }

}
