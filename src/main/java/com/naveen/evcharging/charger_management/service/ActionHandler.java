package com.naveen.evcharging.charger_management.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.naveen.evcharging.charger_management.entity.ChargingStation;

@FunctionalInterface
public interface ActionHandler {
    String handle(String chargerId, JsonNode payload);
}
