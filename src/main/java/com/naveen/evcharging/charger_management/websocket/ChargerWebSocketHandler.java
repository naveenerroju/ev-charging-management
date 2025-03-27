package com.naveen.evcharging.charger_management.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naveen.evcharging.charger_management.exception.InvalidInputException;
import com.naveen.evcharging.charger_management.exception.WebSocketExceptionHandler;
import com.naveen.evcharging.charger_management.model.ServerResponse;
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

    //This acts like cache. Later, we can move this implementation to cache management
    private final Map<String, WebSocketSession> chargerSessions = new ConcurrentHashMap<>();

    private final ActionHandlerFactory actionHandlerFactory;
    private final WebSocketExceptionHandler webSocketExceptionHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChargerWebSocketHandler(ActionHandlerFactory actionHandlerFactory, WebSocketExceptionHandler webSocketExceptionHandler) {
        this.actionHandlerFactory = actionHandlerFactory;
        this.webSocketExceptionHandler = webSocketExceptionHandler;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String chargerId = DataExchangeUtil.getchargingStationIdFromPath(session);
        JsonNode root = objectMapper.readTree(message.getPayload());

        String action = root.path("action").asText();
        String messageId = root.path("messageId").asText();

        log.info("New message received from {} for action: {}. messageId: {}", chargerId, action, messageId);

        ActionHandler handler = actionHandlerFactory.getHandler(action);

        if (handler != null) {

            try{
                ServerResponse actionStatus = handler.handle(chargerId, root.path("payload"));
                TextMessage response = DataExchangeUtil.sendOcppResponse(action, messageId, DataExchangeUtil.convertObjectToJsonString(actionStatus));
                session.sendMessage(response);
            } catch (InvalidInputException ex){
                webSocketExceptionHandler.handleInvalidInputException(session, ex);
            } catch (RuntimeException ex){
                webSocketExceptionHandler.handleGenericException(session, ex);
            }

        } else {
            throw new InvalidInputException("Invalid Action received");
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
