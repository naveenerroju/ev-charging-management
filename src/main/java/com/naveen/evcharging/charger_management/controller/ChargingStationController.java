package com.naveen.evcharging.charger_management.controller;

import com.naveen.evcharging.charger_management.entity.ChargingStation;
import com.naveen.evcharging.charger_management.repository.ChargingStationRepository;
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

    @Autowired
    private ChargingStationRepository repository;

    @PostMapping()
    public ChargingStation registerChargingStation(@RequestBody ChargingStation chargingStation){
        return repository.save(chargingStation);
    }

    @GetMapping()
    public List<ChargingStation> getAllRegisteredChargingStations(){
        return repository.findAll();
    }

}
