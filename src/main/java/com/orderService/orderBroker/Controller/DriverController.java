package com.orderService.orderBroker.Controller;

import com.orderService.orderBroker.Model.DeliveryDriverDTO;
import com.orderService.orderBroker.Service.DeliveryDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DeliveryDriverService deliveryDriverService;

    @PostMapping("/findHighestRatedDriver")
    public ResponseEntity<List<DeliveryDriverDTO>> findHighestRatedDriver() {
        List<DeliveryDriverDTO> drivers = deliveryDriverService.findHighestRatedDriver();
        if(!drivers.isEmpty()){
            return new ResponseEntity<>(drivers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
