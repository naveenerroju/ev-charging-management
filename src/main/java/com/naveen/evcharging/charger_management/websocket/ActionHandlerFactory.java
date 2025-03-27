package com.naveen.evcharging.charger_management.websocket;

import com.naveen.evcharging.charger_management.service.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ActionHandlerFactory {

    private final Map<String, ActionHandler> actionHandlers;

    public ActionHandlerFactory(BootNotificationHandler bootNotificationHandler,
                                HeartbeatHandler heartbeatHandler,
                                StatusNotificationHandler statusNotificationHandler,
                                StartTransactionHandler startTransactionHandler) {
        this.actionHandlers = Map.of(
                "BootNotification", bootNotificationHandler,
                "Heartbeat", heartbeatHandler,
                "StatusNotification", statusNotificationHandler,
                "StartTransaction", startTransactionHandler
        );
    }

    public ActionHandler getHandler(String action) {
        return actionHandlers.get(action);
    }
}
