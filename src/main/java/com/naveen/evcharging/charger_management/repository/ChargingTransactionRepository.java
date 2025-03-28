package com.naveen.evcharging.charger_management.repository;

import com.naveen.evcharging.charger_management.document.ChargingTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargingTransactionRepository extends MongoRepository<ChargingTransaction, String> {
}
