package com.naveen.evcharging.charger_management.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naveen.evcharging.charger_management.entity.ChargingStation;
import com.naveen.evcharging.charger_management.repository.ChargingStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChargerWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> chargingStationSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ChargingStationRepository repository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String chargingStationId = getchargingStationIdFromPath(session);
        chargingStationSessions.put(chargingStationId, session);
        System.out.println("chargingStation connected: " + chargingStationId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String chargingStationId = getchargingStationIdFromPath(session);
        JsonNode root = objectMapper.readTree(message.getPayload());

        String action = root.path("action").asText();

        if ("BootNotification".equalsIgnoreCase(action)) {
            JsonNode payload = root.path("payload");

            ChargingStation chargingStation = new ChargingStation();
            chargingStation.setId(chargingStationId);
            chargingStation.setVendor(payload.path("chargePointVendor").asText("UnknownVendor"));
            chargingStation.setModel(payload.path("chargePointModel").asText("UnknownModel"));
            chargingStation.setStatus("Available");
            chargingStation.setRegisteredAt(LocalDateTime.now());
            chargingStation.setLastHeartbeat(LocalDateTime.now());
            chargingStation.setLastStatusNotification(LocalDateTime.now());

            ChargingStation savedchargingStation = repository.save(chargingStation);

            // Respond with the document ID
            String response = String.format("{\"status\": \"Registered\", \"id\": \"%s\"}", savedchargingStation.getId());
            session.sendMessage(new TextMessage(response));
        } else {
            session.sendMessage(new TextMessage("{\"status\": \"Unsupported action\"}"));
        }
    }

    private String getchargingStationIdFromPath(WebSocketSession session) {
        String path = session.getUri().getPath(); // /ws/chargingStation/chargingStation_001
        return path.substring(path.lastIndexOf("/") + 1);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String chargingStationId = getchargingStationIdFromPath(session);
        chargingStationSessions.remove(chargingStationId);
        System.out.println("chargingStation disconnected: " + chargingStationId);
    }

}
