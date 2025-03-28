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
import java.util.function.Supplier;

@Service
public class StopTransactionHandler implements ActionHandler{

    private final ChargingStationRepository chargingStationRepository;
    private final ChargingTransactionRepository chargingTransactionRepository;

    public StopTransactionHandler(ChargingStationRepository chargingStationRepository, ChargingTransactionRepository chargingTransactionRepository) {
        this.chargingStationRepository = chargingStationRepository;
        this.chargingTransactionRepository = chargingTransactionRepository;
    }

    /**
     *
     * @param chargerId
     * @param payload
     * @return
     */
    @Override
    public ServerResponse handle(String chargerId, JsonNode payload) {

        Supplier<InvalidInputException> throwError = ()-> {
            throw new InvalidInputException("There is no such value exist in Database");
        };

        String transactionId = payload.path("transactionId").asText(null);
        if(transactionId==null || chargerId ==null){
            throwError.get();
        }

        String status = payload.path("status").asText(null);
        double energyConsumed = Double.parseDouble(payload.path("meterReading").asText("0"));
        double cost = Double.parseDouble(payload.path("cost").asText("0"));
        LocalDateTime timestamp;

        try{
            timestamp = LocalDateTime.parse(payload.path("timestamp").asText(null));
        } catch (DateTimeException ex){
            throw new InvalidInputException("timestamp is not in proper format. Provide ISO 8601 combined date and time representation");
        }

        ChargingStation charger = chargingStationRepository.findById(chargerId).orElse(null);
        ChargingTransaction transaction = chargingTransactionRepository.findById(transactionId).orElseThrow(throwError);

        charger.setStatus(status);
        transaction.setUpdatedAt(timestamp);
        transaction.setEnergyConsumed(energyConsumed);
        transaction.setCost(cost);
        transaction.setStatus("Completed");

        chargingStationRepository.save(charger);
        chargingTransactionRepository.save(transaction);

        return new TransactionResponse(transactionId, "Accepted", LocalDateTime.now());
    }
}
