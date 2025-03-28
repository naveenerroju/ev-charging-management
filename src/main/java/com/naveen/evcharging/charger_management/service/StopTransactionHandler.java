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

import java.time.LocalDateTime;

@Service
public class StopTransactionHandler extends TransactionHandler implements ActionHandler{

    public StopTransactionHandler(ChargingStationRepository chargingStationRepository, ChargingTransactionRepository chargingTransactionRepository) {
        super(chargingStationRepository, chargingTransactionRepository);
    }

    /**
     *
     * @param chargerId
     * @param payload
     * @return
     */
    @Override
    public ServerResponse handle(String chargerId, JsonNode payload) {

        String transactionId = payload.path("transactionId").asText(null);
        if (transactionId == null || chargerId == null) {
            throw new InvalidInputException("TransactionId and ChargerId are required");
        }

        String status = payload.path("status").asText(null);
        double energyConsumed = parseEnergyConsumed(payload);
        double cost = Double.parseDouble(payload.path("cost").asText("0"));
        LocalDateTime timestamp = parseTimestamp(payload);

        ChargingStation charger = fetchChargingStation(chargerId);
        ChargingTransaction transaction = fetchChargingTransaction(transactionId);

        charger.setStatus(status);
        transaction.setUpdatedAt(timestamp);
        transaction.setEnergyConsumed(energyConsumed);
        transaction.setCost(cost);
        transaction.setStatus("Completed");

        saveChargingEntities(charger, transaction);

        return new TransactionResponse(transactionId, "Accepted", LocalDateTime.now());
    }
}
