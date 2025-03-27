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

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class StartTransactionHandler implements ActionHandler{

    private final ChargingStationRepository chargingStationRepository;
    private final ChargingTransactionRepository chargingTransactionRepository;

    public StartTransactionHandler(ChargingStationRepository chargingStationRepository, ChargingTransactionRepository chargingTransactionRepository) {
        this.chargingStationRepository = chargingStationRepository;
        this.chargingTransactionRepository = chargingTransactionRepository;
    }

    @Override
    public ServerResponse handle(String chargerId, JsonNode payload) {
        String status = payload.path("status").asText(null);
        String id = payload.path("id").asText(UUID.randomUUID().toString());
        double energyConsumed = Double.parseDouble(payload.path("meterReading").asText("-1"));
        LocalDateTime timestamp;

        try{
            timestamp = LocalDateTime.parse(payload.path("timestamp").asText(null));
        } catch (DateTimeException ex){
            throw new InvalidInputException("timestamp is not in proper format. Provide ISO 8601 combined date and time representation");
        }

        ChargingStation charger = chargingStationRepository.findById(chargerId).orElse(null);
        if(charger!=null){
            ChargingTransaction transaction = new ChargingTransaction();
            transaction.setId(id);

            if(energyConsumed>=0){
                transaction.setStartTime(timestamp);
            }
            transaction.setChargerId(chargerId);
            transaction.setEnergyConsumed(energyConsumed);
            transaction.setUpdatedAt(LocalDateTime.now());

            charger.setStatus(status);
            charger.setLastStatusNotification(timestamp);

            chargingStationRepository.save(charger);
            chargingTransactionRepository.save(transaction);
            return new TransactionResponse(id, "Accepted", LocalDateTime.now());
        } else {
            throw new InvalidInputException("Charger ID doesn't exist");
        }

    }
}
