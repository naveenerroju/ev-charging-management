package com.naveen.evcharging.charger_management.config;

import com.naveen.evcharging.charger_management.websocket.ChargerWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChargerWebSocketHandler chargerHandler;

    public WebSocketConfig(ChargerWebSocketHandler chargerHandler) {
        this.chargerHandler = chargerHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(chargerHandler, "ws/station/{stationId}")
                .setAllowedOrigins("*"); // Allow all origins for testing
    }
}