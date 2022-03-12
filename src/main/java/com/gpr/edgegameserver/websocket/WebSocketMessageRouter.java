package com.gpr.edgegameserver.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpr.edgegameserver.signaling.MalformattedSignalingException;
import com.gpr.edgegameserver.signaling.Sdp;
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

    private final ObjectMapper objectMapper;

    public WebSocketMessageRouter(WebRTCBinService webRTCBinService, ObjectMapper objectMapper) {
        this.webRTCBinService = webRTCBinService;
        this.objectMapper = objectMapper;
    }

    public void routeMessage(WebSocketMessage message, WebSocketSession session) throws MalformattedSignalingException {
        LOGGER.info("Routing message: {}", message);
        JsonNode payload = message.getPayload();
        switch (message.getMessageType()) {
            case START:
                webRTCBinService.startSignaling(session);
                break;
            case SDP_ANSWER:
                webRTCBinService.receiveSdpAnswer(DeserializationUtils.toSdp(message.getPayload()), session);
                break;
            case ICE_CANDIDATE:
                webRTCBinService.receiveIceCandidate(DeserializationUtils.toIceCandidate(message.getPayload()), session);
                break;
            default:
                LOGGER.warn("It was not possible to route message: {}", message);
                break;
        }
    }
}
