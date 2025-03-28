package com.naveen.evcharging.charger_management.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.naveen.evcharging.charger_management.model.ServerResponse;

@FunctionalInterface
public interface ActionHandler {
    ServerResponse handle(String chargerId, JsonNode payload);
}
