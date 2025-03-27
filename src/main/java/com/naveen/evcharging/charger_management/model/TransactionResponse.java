package com.naveen.evcharging.charger_management.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse extends ServerResponse{
    private String transactionId;

    public TransactionResponse(String transactionId, String status, LocalDateTime timestamp) {
        this.transactionId=transactionId;
        super.setStatus(status);
        super.setCurrentTime(timestamp);
    }
}
