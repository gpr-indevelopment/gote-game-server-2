package com.gpr.edgegameserver.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.gpr.edgegameserver.signaling.ICECandidate;
import com.gpr.edgegameserver.signaling.Sdp;
import com.gpr.edgegameserver.websocket.WebSocketMessage;
import com.gpr.edgegameserver.websocket.WebSocketMessageType;

public class DeserializationUtils {

    public static ICECandidate toIceCandidate(JsonNode jsonNode) {
        String candidate = jsonNode.get("candidate").asText();
        int sdpMLineIndex = jsonNode.get("sdpMLineIndex").asInt();
        return new ICECandidate(candidate, sdpMLineIndex);
    }

    public static Sdp toSdp(JsonNode jsonNode) {
        String type = jsonNode.get("type").asText();
        String sdp = jsonNode.get("sdp").asText();
        return new Sdp(sdp, type);
    }

    public static WebSocketMessage toWebSocketMessage(JsonNode jsonNode) {
        WebSocketMessageType messageType = WebSocketMessageType.valueOf(jsonNode.get("messageType").asText());
        JsonNode payload = jsonNode.get("payload");
        return new WebSocketMessage(messageType, payload);
    }
}
