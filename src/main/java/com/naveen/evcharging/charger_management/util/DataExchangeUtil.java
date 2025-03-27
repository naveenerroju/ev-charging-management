package com.naveen.evcharging.charger_management.util;

import org.springframework.web.socket.WebSocketSession;

public class DataExchangeUtil {

    public static String getchargingStationIdFromPath(WebSocketSession session) {
        String path = session.getUri().getPath(); // /ws/chargingStation/chargingStation_001
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
