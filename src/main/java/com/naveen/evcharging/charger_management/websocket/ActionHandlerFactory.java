package com.naveen.evcharging.charger_management.websocket;

import com.naveen.evcharging.charger_management.service.ActionHandler;
import com.naveen.evcharging.charger_management.service.BootNotificationHandler;
import com.naveen.evcharging.charger_management.service.HeartbeatHandler;
import com.naveen.evcharging.charger_management.service.StatusNotificationHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ActionHandlerFactory {

    private final Map<String, ActionHandler> actionHandlers;

    public ActionHandlerFactory(BootNotificationHandler bootNotificationHandler,
                                HeartbeatHandler heartbeatHandler,
                                StatusNotificationHandler statusNotificationHandler) {
        this.actionHandlers = Map.of(
                "BootNotification", bootNotificationHandler,
                "Heartbeat", heartbeatHandler,
                "StatusNotification", statusNotificationHandler
        );
    }

    public ActionHandler getHandler(String action) {
        return actionHandlers.get(action);
    }
}
