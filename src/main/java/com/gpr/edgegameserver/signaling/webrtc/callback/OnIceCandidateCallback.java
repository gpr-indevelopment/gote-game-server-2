package com.gpr.edgegameserver.signaling.webrtc.callback;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.freedesktop.gstreamer.elements.WebRTCBin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class OnIceCandidateCallback implements WebRTCBin.ON_ICE_CANDIDATE {

    Logger logger = LoggerFactory.getLogger(OnIceCandidateCallback.class);

    private final ObjectMapper mapper;

    private final WebSocketSession session;

    public OnIceCandidateCallback(ObjectMapper mapper, WebSocketSession session) {
        this.mapper = mapper;
        this.session = session;
    }

    @Override
    public void onIceCandidate(int sdpMLineIndex, String candidate) {
        JsonNode rootNode = mapper.createObjectNode();
        JsonNode iceNode = mapper.createObjectNode();
        ((ObjectNode) iceNode).put("candidate", candidate);
        ((ObjectNode) iceNode).put("sdpMLineIndex", sdpMLineIndex);
        ((ObjectNode) rootNode).set("ice", iceNode);

        try {
            String json = mapper.writeValueAsString(rootNode);
            logger.info("ON_ICE_CANDIDATE: " + json);
            this.session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            logger.error("Couldn't write JSON", e);
        }
    }
}
