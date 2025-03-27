package com.naveen.evcharging.charger_management.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naveen.evcharging.charger_management.service.ActionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class ChargerWebSocketHandler extends TextWebSocketHandler {

    private final ActionHandlerFactory actionHandlerFactory;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChargerWebSocketHandler(ActionHandlerFactory actionHandlerFactory) {
        this.actionHandlerFactory = actionHandlerFactory;
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String chargerId = getChargerIdFromPath(session);
        JsonNode root = objectMapper.readTree(message.getPayload());

        String action = root.path("action").asText();

        ActionHandler handler = actionHandlerFactory.getHandler(action);

        if (handler != null) {
            String response = handler.handle(chargerId, root.path("payload"));
            session.sendMessage(new TextMessage(response));
        } else {
            session.sendMessage(new TextMessage("{\"status\": \"Error\", \"message\": \"Unsupported action\"}"));
        }
    }

    private String getChargerIdFromPath(WebSocketSession session) {
        String path = session.getUri().getPath(); // e.g., /ws/charger/CHARGER_001
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
