package com.naveen.evcharging.charger_management.document;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "charging_transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChargingTransaction {

    @Id
    private String id;
    @NotBlank(message = "Charging station Id cannot be null")
    private String stationId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double energyConsumed;  // in kWh
    private double cost;  // in currency
    @NotBlank(message = "Status cannot be null")
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
