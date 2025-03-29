package com.naveen.evcharging.charger_management.service;

import com.naveen.evcharging.charger_management.cache.ChargingStationStatusCache;
import com.naveen.evcharging.charger_management.document.ChargingStation;
import com.naveen.evcharging.charger_management.repository.ChargingStationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class ChargingStationStatusMonitor {

    private final ChargingStationStatusCache statusCache;
    private final ChargingStationRepository stationRepository;

    public ChargingStationStatusMonitor(ChargingStationStatusCache statusCache, ChargingStationRepository stationRepository) {
        this.statusCache = statusCache;
        this.stationRepository = stationRepository;
    }

    @Scheduled(cron = "0 */2 * * * *") //cron for 5 minutes
    public void surveyStations(){
        log.error("Executing scheduler");

        long cutofftime = 2; //in minutes
        LocalDateTime cutOffTime = LocalDateTime.now().minusMinutes(cutofftime);
        ConcurrentMap<String, LocalDateTime> unavailableStations = statusCache.getChargersToMarkUnavailable(cutOffTime);
        log.info("{} charging stations are now unavailable.",unavailableStations.size());

        for (String id : unavailableStations.keySet()){
            ChargingStation station =  stationRepository.findById(id).orElse(null);
            if(station!=null){
                station.setStatus("Unavailable");
                stationRepository.save(station);
            }

        }
    }
}
