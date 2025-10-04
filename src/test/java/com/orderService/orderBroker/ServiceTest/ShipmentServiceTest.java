package com.orderService.orderBroker.ServiceTest;

import com.orderService.orderBroker.Entity.DeliveryDriver;
import com.orderService.orderBroker.Entity.Shipment;
import com.orderService.orderBroker.Enums.ShipmentStatus;
import com.orderService.orderBroker.Model.ShipmentDTO;
import com.orderService.orderBroker.Repository.ShipmentRepository;
import com.orderService.orderBroker.Service.DeliveryDriverService;
import com.orderService.orderBroker.Service.ShipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private DeliveryDriverService deliveryDriverService;

    @InjectMocks
    private ShipmentService shipmentService;

    private ShipmentDTO shipmentDTO;
    private DeliveryDriver driver;

    @BeforeEach
    void setUp() {
        driver = new DeliveryDriver();
        driver.setDriverId(1L);
        driver.setDriverName("John Doe");
        shipmentDTO = new ShipmentDTO();
        shipmentDTO.setShipmentId(100L);
        shipmentDTO.setOrderId(200L);
        shipmentDTO.setShipmentStatus(ShipmentStatus.PENDING);
    }

    @Test
    void testCreateShipment() {
        // Mock driver service
        when(deliveryDriverService.findADriver()).thenReturn(driver);

        // Mock repository save
        Shipment savedShipment = new Shipment();
        savedShipment.setShipmentId(100L);
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(savedShipment);

        // Call service
        ShipmentDTO result = shipmentService.createShipment(shipmentDTO);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result.getDeliveryDriver()).isEqualTo(driver);

        // Verify interactions
        verify(deliveryDriverService, times(1)).findADriver();
        verify(shipmentRepository, times(1)).save(any(Shipment.class));
    }
}