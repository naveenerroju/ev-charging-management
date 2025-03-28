package com.naveen.evcharging.charger_management.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.naveen.evcharging.charger_management.document.ChargingStation;
import com.naveen.evcharging.charger_management.document.ChargingTransaction;
import com.naveen.evcharging.charger_management.exception.InvalidInputException;
import com.naveen.evcharging.charger_management.model.ServerResponse;
import com.naveen.evcharging.charger_management.model.TransactionResponse;
import com.naveen.evcharging.charger_management.repository.ChargingStationRepository;
import com.naveen.evcharging.charger_management.repository.ChargingTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class StartTransactionHandler extends TransactionHandler implements ActionHandler{

    public StartTransactionHandler(ChargingStationRepository chargingStationRepository, ChargingTransactionRepository chargingTransactionRepository) {
        super(chargingStationRepository, chargingTransactionRepository);
    }

    @Override
    @Transactional
    public ServerResponse handle(String chargerId, JsonNode payload) {
        String status = payload.path("status").asText(null);
        String id = payload.path("id").asText(UUID.randomUUID().toString());
        double energyConsumed = parseEnergyConsumed(payload);
        LocalDateTime timestamp = parseTimestamp(payload);

        ChargingStation charger = fetchChargingStation(chargerId);

        ChargingTransaction transaction = new ChargingTransaction();
        transaction.setId(id);

        if (energyConsumed >= 0) {
            transaction.setStartTime(timestamp);
        }
        transaction.setChargerId(chargerId);
        transaction.setEnergyConsumed(energyConsumed);
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setStatus("Processing");

        charger.setStatus(status);
        charger.setLastStatusNotification(timestamp);

        saveChargingEntities(charger, transaction);

        return new TransactionResponse(id, "Accepted", LocalDateTime.now());
    }
}
