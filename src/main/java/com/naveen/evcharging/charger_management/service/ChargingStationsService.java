package com.naveen.evcharging.charger_management.service;

import com.naveen.evcharging.charger_management.document.ChargingStation;
import com.naveen.evcharging.charger_management.model.ChargingStationsResponse;
import com.naveen.evcharging.charger_management.repository.ChargingStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargingStationsService {

    private final ChargingStationRepository repository;


    public ChargingStationsService(ChargingStationRepository repository) {
        this.repository = repository;
    }

    public List<ChargingStationsResponse> getStationsStatuses(){
        List<ChargingStation> stations = repository.findAll();

        return stations.stream()
                .map((station)-> new ChargingStationsResponse(station.getId(), station.getStatus()))
                .toList();

    }
}
