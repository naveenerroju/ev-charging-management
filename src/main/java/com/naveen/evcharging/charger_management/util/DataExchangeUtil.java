package com.naveen.evcharging.charger_management.util;

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
}
