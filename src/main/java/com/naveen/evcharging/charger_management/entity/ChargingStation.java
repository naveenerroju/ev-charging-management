package com.naveen.evcharging.charger_management.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Document(collection = "ChargingStations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChargingStation {

    @Id
    @NotBlank(message = "Charger ID is required")
    private String id;

    @NotBlank(message = "Vendor is required")
    private String vendor;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Status must not be blank")
    private String status;  // Available, Charging, Faulted, etc.

    @NotNull(message = "Last heartbeat time is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastHeartbeat;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime registeredAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastStatusNotification;

}
