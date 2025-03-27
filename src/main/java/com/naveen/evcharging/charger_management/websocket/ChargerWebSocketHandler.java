package com.naveen.evcharging.charger_management.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naveen.evcharging.charger_management.service.ActionHandler;
import com.naveen.evcharging.charger_management.util.DataExchangeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ChargerWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> chargerSessions = new ConcurrentHashMap<>();

    private final ActionHandlerFactory actionHandlerFactory;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChargerWebSocketHandler(ActionHandlerFactory actionHandlerFactory) {
        this.actionHandlerFactory = actionHandlerFactory;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String chargerId = DataExchangeUtil.getchargingStationIdFromPath(session);
        JsonNode root = objectMapper.readTree(message.getPayload());

        String action = root.path("action").asText();

        log.info("New message received from {} for action: {}", chargerId, action);

        ActionHandler handler = actionHandlerFactory.getHandler(action);

        if (handler != null) {
            String response = handler.handle(chargerId, root.path("payload"));
            session.sendMessage(new TextMessage(response));
        } else {
            session.sendMessage(new TextMessage("{\"status\": \"Error\", \"message\": \"Unsupported action\"}"));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String chargerId = DataExchangeUtil.getchargingStationIdFromPath(session);
        chargerSessions.put(chargerId, session);
        log.info("New connection is established with {}", chargerId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        String chargerId = DataExchangeUtil.getchargingStationIdFromPath(session);
        chargerSessions.put(chargerId, session);
        log.info("Connection is closed with {}", chargerId);
    }

}
