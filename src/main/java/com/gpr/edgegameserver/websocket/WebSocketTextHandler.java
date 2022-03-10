package com.gpr.edgegameserver.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpr.edgegameserver.signaling.MalformattedSignalingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketTextHandler extends TextWebSocketHandler {

    Logger logger = LoggerFactory.getLogger(WebSocketTextHandler.class);

    private final ObjectMapper objectMapper;

    private final WebSocketMessageRouter webSocketMessageRouter;

    public WebSocketTextHandler(ObjectMapper objectMapper, WebSocketMessageRouter webSocketMessageRouter) {
        this.objectMapper = objectMapper;
        this.webSocketMessageRouter = webSocketMessageRouter;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage incomingMessage) throws Exception {
        logger.info("Received message: {}", incomingMessage);
        try {
            WebSocketMessage message = deserializeMessage(incomingMessage);
            webSocketMessageRouter.routeMessage(message, session);
        } catch (MalformattedSignalingException e) {
            logger.error(e.getMessage());
        }
    }

    private WebSocketMessage deserializeMessage(TextMessage message) throws MalformattedSignalingException {
        try {
            return objectMapper.readValue(message.getPayload(), WebSocketMessage.class);
        } catch (JsonProcessingException e) {
            String errorMsg = String.format("MalformattedSignalingException while converting input message from WebSocket to a payload. Message: %s", message.getPayload());
            throw new MalformattedSignalingException(errorMsg, e);
        }
    }
}
