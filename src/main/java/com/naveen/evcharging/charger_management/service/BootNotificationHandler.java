package com.naveen.evcharging.charger_management.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naveen.evcharging.charger_management.entity.ChargingStation;
import com.naveen.evcharging.charger_management.exception.InvalidInputException;
import com.naveen.evcharging.charger_management.repository.ChargingStationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BootNotificationHandler implements ActionHandler{

    private final ChargingStationRepository chargerRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BootNotificationHandler(ChargingStationRepository chargerRepository) {
        this.chargerRepository = chargerRepository;
    }

    @Override
    public String handle(String chargerId, JsonNode payload) {

        // Validate the required fields: vendor and model
        String vendor = payload.path("chargePointVendor").asText(null);
        String model = payload.path("chargePointModel").asText(null);
        String status = payload.path("status").asText(null);
        LocalDateTime currentTime = LocalDateTime.now();

        if (vendor == null || model == null) {
            throw new InvalidInputException("Both chargePointVendor and chargePointModel are required.");
        }

        ChargingStation charger = chargerRepository.findById(chargerId).orElseGet(() -> {
            ChargingStation newCharger = new ChargingStation();
            newCharger.setId(chargerId);
            newCharger.setRegisteredAt(currentTime);
            return newCharger;
        });

        charger.setVendor(payload.path("chargePointVendor").asText("UnknownVendor"));
        charger.setModel(payload.path("chargePointModel").asText("UnknownModel"));
        charger.setLastHeartbeat(currentTime);
        charger.setStatus(status);
        charger.setLastStatusNotification(currentTime);

        chargerRepository.save(charger);

        // Return the OCPP formatted response
        return String.format("{\"status\": \"Accepted\", \"currentTime\": \"%s\", \"interval\": 10}",
                currentTime);
    }
}
