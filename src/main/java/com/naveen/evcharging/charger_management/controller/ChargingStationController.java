package com.naveen.evcharging.charger_management.controller;

import com.naveen.evcharging.charger_management.document.ChargingStation;
import com.naveen.evcharging.charger_management.model.ChargingStationsResponse;
import com.naveen.evcharging.charger_management.repository.ChargingStationRepository;
import com.naveen.evcharging.charger_management.service.ChargingStationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This rest controller is only for the internal server side testing/monitoring purpose.
 * Do not expose to the client.
 */
@RestController
@RequestMapping("/internal/stations")
public class ChargingStationController {

    private final ChargingStationsService service;

    public ChargingStationController(ChargingStationsService service) {
        this.service = service;
    }


    @GetMapping()
    public List<ChargingStationsResponse> getAllRegisteredChargingStations(){
        return service.getStationsStatuses();
    }

}
