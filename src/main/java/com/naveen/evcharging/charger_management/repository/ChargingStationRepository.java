package com.naveen.evcharging.charger_management.repository;

import com.naveen.evcharging.charger_management.entity.ChargingStation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChargingStationRepository extends MongoRepository<ChargingStation, String> {
}
