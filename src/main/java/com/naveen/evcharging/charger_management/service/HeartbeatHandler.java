package com.naveen.evcharging.charger_management.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.naveen.evcharging.charger_management.entity.ChargingStation;
import com.naveen.evcharging.charger_management.repository.ChargingStationRepository;
import org.springframework.stereotype.Service;

@Service
public class HeartbeatHandler implements ActionHandler{
    private final ChargingStationRepository chargerRepository;

    public HeartbeatHandler(ChargingStationRepository chargerRepository) {
        this.chargerRepository = chargerRepository;
    }

    @Override
    public String handle(String chargerId, JsonNode payload) {
        // Handle the Heartbeat: Update the lastHeartbeat timestamp for the charger
        ChargingStation charger = chargerRepository.findById(chargerId).orElse(null);
        if (charger != null) {
            // Update the heartbeat timestamp
            charger.setLastHeartbeat(java.time.LocalDateTime.now());
            chargerRepository.save(charger);

            // Return the response in OCPP 1.6 format
            return String.format("{\"status\": \"Accepted\", \"currentTime\": \"%s\"}", java.time.LocalDateTime.now().toString());
        } else {
            return "{\"status\": \"Error\", \"message\": \"Charger not found\"}";
        }
    }
}
