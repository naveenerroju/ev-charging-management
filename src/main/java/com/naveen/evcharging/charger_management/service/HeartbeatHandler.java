package com.naveen.evcharging.charger_management.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.naveen.evcharging.charger_management.document.ChargingStation;
import com.naveen.evcharging.charger_management.exception.InvalidInputException;
import com.naveen.evcharging.charger_management.model.ServerResponse;
import com.naveen.evcharging.charger_management.repository.ChargingStationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HeartbeatHandler implements ActionHandler{
    private final ChargingStationRepository chargerRepository;

    public HeartbeatHandler(ChargingStationRepository chargerRepository) {
        this.chargerRepository = chargerRepository;
    }

    @Override
    public ServerResponse handle(String chargerId, JsonNode payload) {
        // Handle the Heartbeat: Update the lastHeartbeat timestamp for the charger
        ChargingStation charger = chargerRepository.findById(chargerId).orElse(null);
        if (charger != null) {
            // Update the heartbeat timestamp
            charger.setLastHeartbeat(java.time.LocalDateTime.now());
            chargerRepository.save(charger);

            // Return the response in OCPP 1.6 format
            return new ServerResponse("Accepted", LocalDateTime.now());
        } else {
            throw new InvalidInputException("Charger ID doesn't exist");
        }
    }
}
