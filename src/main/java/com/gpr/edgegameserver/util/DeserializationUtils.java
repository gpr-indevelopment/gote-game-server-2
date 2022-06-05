package com.gpr.edgegameserver.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.gpr.edgegameserver.signaling.ICECandidate;
import com.gpr.edgegameserver.signaling.MalformattedSignalingException;
import com.gpr.edgegameserver.signaling.Sdp;
import com.gpr.edgegameserver.stats.StreamingStatsRecord;
import com.gpr.edgegameserver.websocket.WebSocketMessage;
import com.gpr.edgegameserver.websocket.WebSocketMessageType;

public class DeserializationUtils {

    public static ICECandidate toIceCandidate(JsonNode jsonNode) throws MalformattedSignalingException {
        try {
            String candidate = jsonNode.get("candidate").asText();
            int sdpMLineIndex = jsonNode.get("sdpMLineIndex").asInt();
            return new ICECandidate(candidate, sdpMLineIndex);
        } catch (Exception e) {
            String message = String.format("Cannot deserialize ICECandidate from JSON node: %s", jsonNode.toString());
            throw new MalformattedSignalingException(message, e);
        }
    }

    public static Sdp toSdp(JsonNode jsonNode) throws MalformattedSignalingException {
        try {
            String type = jsonNode.get("type").asText();
            String sdp = jsonNode.get("sdp").asText();
            return new Sdp(sdp, type);
        } catch (Exception e) {
            String message = String.format("Cannot deserialize SDP from JSON node: %s", jsonNode.toString());
            throw new MalformattedSignalingException(message, e);
        }
    }

    public static WebSocketMessage toWebSocketMessage(JsonNode jsonNode) throws MalformattedSignalingException {
        try {
            WebSocketMessageType messageType = WebSocketMessageType.valueOf(jsonNode.get("messageType").asText());
            JsonNode payload = jsonNode.get("payload");
            return new WebSocketMessage(messageType, payload);
        } catch (Exception e) {
            String message = String.format("Cannot deserialize WebSocketMessage from JSON node: %s", jsonNode.toString());
            throw new MalformattedSignalingException(message, e);
        }
    }

    public static StreamingStatsRecord toStreamingStatsRecord(JsonNode jsonNode) {
        return StreamingStatsRecord
                .builder()
                .timestamp(jsonNode.get("timestamp").asLong())
                .jitter(jsonNode.get("jitter").asDouble())
                .packetsLost(jsonNode.get("packetsLost").asInt())
                .bytesReceived(jsonNode.get("bytesReceived").asLong())
                .framesPerSecond(jsonNode.get("framesPerSecond") == null ? 0 : jsonNode.get("framesPerSecond").asDouble())
                .framesDropped(jsonNode.get("framesDropped").asInt())
                .frameHeight(jsonNode.get("frameHeight").asInt())
                .frameWidth(jsonNode.get("frameWidth").asInt())
                .build();
    }
}
