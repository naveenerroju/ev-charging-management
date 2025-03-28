package com.naveen.evcharging.charger_management.cache;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ChargingStationStatusCache {

    private final ConcurrentMap<String, LocalDateTime> chargerStatusMap = new ConcurrentHashMap<>();

    public void updateStatus(String chargerId, LocalDateTime lastStatusUpdateTime) {
        chargerStatusMap.put(chargerId, lastStatusUpdateTime);
    }

    public LocalDateTime getLastStatusUpdateTime(String chargerId) {
        return chargerStatusMap.get(chargerId);
    }

    public void removeCharger(String chargerId) {
        chargerStatusMap.remove(chargerId);
    }

    public ConcurrentMap<String, LocalDateTime> getChargersToMarkUnavailable(LocalDateTime cutoffTime) {
        ConcurrentMap<String, LocalDateTime> chargersToUpdate = new ConcurrentHashMap<>();
        chargerStatusMap.forEach((chargerId, lastStatusUpdateTime) -> {
            if (lastStatusUpdateTime.isBefore(cutoffTime)) {
                chargersToUpdate.put(chargerId, lastStatusUpdateTime);
            }
        });
        return chargersToUpdate;
    }
}