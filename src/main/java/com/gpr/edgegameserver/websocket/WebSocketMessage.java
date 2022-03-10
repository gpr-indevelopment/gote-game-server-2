package com.gpr.edgegameserver.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class WebSocketMessage {

    private final WebSocketMessageType messageType;

    private final JsonNode payload;

    public WebSocketMessage(@JsonProperty("messageType") WebSocketMessageType messageType,
                            @JsonProperty("payload") JsonNode payload) {
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
                ", payload=" + payload.toString() +
                '}';
    }
}
