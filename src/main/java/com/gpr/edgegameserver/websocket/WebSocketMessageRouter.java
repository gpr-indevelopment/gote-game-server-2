package com.gpr.edgegameserver.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.gpr.edgegameserver.signaling.MalformattedSignalingException;
import com.gpr.edgegameserver.signaling.WebRTCBinService;
import com.gpr.edgegameserver.util.DeserializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketMessageRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessageRouter.class);

    private final WebRTCBinService webRTCBinService;

    private final WebSocketStatsService webSocketStatsService;

    public WebSocketMessageRouter(WebRTCBinService webRTCBinService, WebSocketStatsService webSocketStatsService) {
        this.webRTCBinService = webRTCBinService;
        this.webSocketStatsService = webSocketStatsService;
    }

    public void routeMessage(WebSocketMessage message, WebSocketSession session) throws MalformattedSignalingException {
        LOGGER.info("Routing message: {}", message);
        JsonNode payload = message.getPayload();
        switch (message.getMessageType()) {
            case START:
                webRTCBinService.startSignaling(session);
                break;
            case STOP:
                webRTCBinService.stopSignaling(session);
                break;
            case SDP_ANSWER:
                webRTCBinService.receiveSdpAnswer(DeserializationUtils.toSdp(payload), session);
                break;
            case ICE_CANDIDATE:
                webRTCBinService.receiveIceCandidate(DeserializationUtils.toIceCandidate(payload), session);
                break;
            case STATS_RECORD:
                webSocketStatsService.storeStats(DeserializationUtils.toStreamingStatsRecord(payload), session);
                break;
            default:
                LOGGER.warn("It was not possible to route message: {}", message);
                break;
        }
    }
}
