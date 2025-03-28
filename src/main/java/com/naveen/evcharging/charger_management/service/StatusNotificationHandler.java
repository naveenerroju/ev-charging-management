package com.naveen.evcharging.charger_management.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.naveen.evcharging.charger_management.document.ChargingStation;
import com.naveen.evcharging.charger_management.exception.InvalidInputException;
import com.naveen.evcharging.charger_management.model.ServerResponse;
import com.naveen.evcharging.charger_management.repository.ChargingStationRepository;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;

@Service
public class StatusNotificationHandler implements ActionHandler{

    private final ChargingStationRepository chargingStationRepository;

    public StatusNotificationHandler(ChargingStationRepository chargingStationRepository) {
        this.chargingStationRepository = chargingStationRepository;
    }

    @Override
    public ServerResponse handle(String chargerId, JsonNode payload) {

        String status = payload.path("status").asText(null);
        LocalDateTime timestamp;

        try{
            timestamp = LocalDateTime.parse(payload.path("timestamp").asText(null));
        } catch (DateTimeException ex){
            throw new InvalidInputException("timestamp is not in proper format. Provide ISO 8601 combined date and time representation");
        }

        ChargingStation charger = chargingStationRepository.findById(chargerId).orElse(null);
        if(charger!=null){
            charger.setStatus(status);
            charger.setLastStatusNotification(timestamp);
            chargingStationRepository.save(charger);
            return new ServerResponse("Accepted", LocalDateTime.now());
        } else {
            throw new InvalidInputException("Charger ID doesn't exist");
        }
    }

}
