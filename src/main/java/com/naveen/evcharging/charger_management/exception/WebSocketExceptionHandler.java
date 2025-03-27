package com.naveen.evcharging.charger_management.exception;

import com.naveen.evcharging.charger_management.exception.InvalidInputException;
import org.springframework.web.socket.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class WebSocketExceptionHandler {

    // Handles InvalidInputException and other exceptions
    public void handleInvalidInputException(WebSocketSession session, InvalidInputException ex) throws IOException {
        sendErrorResponse(session, "InvalidInputException", ex.getMessage());
    }

    // Handle other exceptions in a generic way
    public void handleGenericException(WebSocketSession session, Exception ex) throws IOException {
        sendErrorResponse(session, "Exception", "An unexpected error occurred: " + ex.getMessage());
    }

    // Sends error response in OCPP format
    private void sendErrorResponse(WebSocketSession session, String errorType, String errorMessage) throws IOException {
        String response = String.format(
                "{\"action\": \"%s\", \"status\": \"Error\", \"message\": \"%s\"}",
                errorType, errorMessage
        );
        session.sendMessage(new TextMessage(response));
    }
}
