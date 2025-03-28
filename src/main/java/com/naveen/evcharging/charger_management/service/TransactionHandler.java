package com.naveen.evcharging.charger_management.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.naveen.evcharging.charger_management.document.ChargingStation;
import com.naveen.evcharging.charger_management.document.ChargingTransaction;
import com.naveen.evcharging.charger_management.exception.InvalidInputException;
import com.naveen.evcharging.charger_management.repository.ChargingStationRepository;
import com.naveen.evcharging.charger_management.repository.ChargingTransactionRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;

public abstract class TransactionHandler {

    protected final ChargingStationRepository chargingStationRepository;
    protected final ChargingTransactionRepository chargingTransactionRepository;

    public TransactionHandler(ChargingStationRepository chargingStationRepository, ChargingTransactionRepository chargingTransactionRepository) {
        this.chargingStationRepository = chargingStationRepository;
        this.chargingTransactionRepository = chargingTransactionRepository;
    }

    // Common method to parse and validate timestamp
    public LocalDateTime parseTimestamp(JsonNode payload) {
        try {
            return LocalDateTime.parse(payload.path("timestamp").asText(null));
        } catch (DateTimeException ex) {
            throw new InvalidInputException("timestamp is not in proper format. Provide ISO 8601 combined date and time representation");
        }
    }

    // Common method to validate and parse energy readings
    public double parseEnergyConsumed(JsonNode payload) {
        try {
            return Double.parseDouble(payload.path("meterReading").asText("0"));
        } catch (NumberFormatException ex) {
            throw new InvalidInputException("Invalid meter reading value");
        }
    }

    // Common method to validate and fetch charger and transaction data from repositories
    public ChargingStation fetchChargingStation(String chargerId) {
        ChargingStation charger = chargingStationRepository.findById(chargerId).orElse(null);
        if (charger == null) {
            throw new InvalidInputException("Charger ID doesn't exist");
        }
        return charger;
    }

    public ChargingTransaction fetchChargingTransaction(String transactionId) {
        return chargingTransactionRepository.findById(transactionId).orElseThrow(() -> new InvalidInputException("Transaction not found"));
    }

    // Method to save ChargingStation and ChargingTransaction
    public void saveChargingEntities(ChargingStation charger, ChargingTransaction transaction) {
        chargingStationRepository.save(charger);
        chargingTransactionRepository.save(transaction);
    }
}
