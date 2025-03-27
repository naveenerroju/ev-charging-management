package com.naveen.evcharging.charger_management.repository;

import com.naveen.evcharging.charger_management.document.ChargingStation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargingStationRepository extends MongoRepository<ChargingStation, String> {
}
