package com.gpr.edgegameserver.websocket;

import com.fasterxml.jackson.databind.JsonNode;

public class WebSocketMessage {

    private final WebSocketMessageType messageType;

    private final JsonNode payload;

    public WebSocketMessage(WebSocketMessageType messageType,
                            JsonNode payload) {
        this.messageType = messageType;
        this.payload = payload;
    }

    public WebSocketMessageType getMessageType() {
        return messageType;
    }

    public JsonNode getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "WebSocketPayload{" +
                "messageType=" + messageType.name() +
                ", payload=" + payload +
                '}';
    }
}
