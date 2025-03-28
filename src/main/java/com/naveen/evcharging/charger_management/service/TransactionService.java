package com.naveen.evcharging.charger_management.service;

import com.naveen.evcharging.charger_management.model.TransactionResponse;
import com.naveen.evcharging.charger_management.repository.ChargingTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private final ChargingTransactionRepository transactionRepository;

    public TransactionService(ChargingTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionResponse> getTransactions(LocalDateTime startTime, LocalDateTime endTime) {
        List<TransactionResponse> responses = new ArrayList<>();

        transactionRepository.findByStartTimeBetween(startTime, endTime).forEach(
                        (transaction) -> responses.add(
                                new TransactionResponse(transaction.getId(), transaction.getStatus(), transaction.getEndTime())
                        ));

        return responses;
    }

}
