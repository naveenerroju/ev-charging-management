package com.naveen.evcharging.charger_management.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class DataExchangeUtil {

    /**
     * This is a Utils class. This cannot be instantiated. Hence making the constructor private.
     */
    private DataExchangeUtil(){

    }

    public static String getchargingStationIdFromPath(WebSocketSession session) {
        String path = session.getUri().getPath(); // /ws/chargingStation/chargingStation_001
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static TextMessage sendOcppResponse(String action, String messageId, String responsePayload){
        // Create the response message in OCPP format
        String responseMessage = String.format(
                "{\"action\": \"%s\", \"messageId\": \"%s\", \"payload\": %s}",
                action, messageId, responsePayload
        );
        return new TextMessage(responseMessage);
    }

    public static String convertObjectToJsonString(Object o){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }
}
