package com.orderService.orderBroker.Service;

import com.orderService.orderBroker.Entity.DeliveryDriver;
import com.orderService.orderBroker.Repository.DeliveryDriverRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryDriverService {

    private final DeliveryDriverRepository deliveryDriverRepository;

    private static final Logger logger = LoggerFactory.getLogger(DeliveryDriverService.class);

    public DeliveryDriver findADriver() {
        logger.info("Attempting to find a driver with the least shipments.");
        try {
            DeliveryDriver driver = deliveryDriverRepository.findDriverWithLeastShipments();
            if (driver != null) {
                logger.debug("Driver found: {}", driver);
            }
            return driver;
        } catch (Exception e) {
            logger.error("Error occurred while finding a driver", e);
            throw new RuntimeException("Failed to find a driver", e);
        }
    }

}
