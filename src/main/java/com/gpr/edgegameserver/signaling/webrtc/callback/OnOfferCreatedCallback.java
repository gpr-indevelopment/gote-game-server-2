package com.gpr.edgegameserver.signaling.webrtc.callback;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.freedesktop.gstreamer.WebRTCSessionDescription;
import org.freedesktop.gstreamer.elements.WebRTCBin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class OnOfferCreatedCallback implements WebRTCBin.CREATE_OFFER {

    Logger logger = LoggerFactory.getLogger(OnOfferCreatedCallback.class);

    private final WebRTCBin webRTCBin;

    private final ObjectMapper mapper;

    private final WebSocketSession session;

    public OnOfferCreatedCallback(WebRTCBin webRTCBin, ObjectMapper mapper, WebSocketSession session) {
        this.webRTCBin = webRTCBin;
        this.mapper = mapper;
        this.session = session;
    }

    @Override
    public void onOfferCreated(WebRTCSessionDescription offer) {
        webRTCBin.setLocalDescription(offer);
        try {
            JsonNode rootNode = mapper.createObjectNode();
            JsonNode sdpNode = mapper.createObjectNode();
            ((ObjectNode) sdpNode).put("type", "offer");
            ((ObjectNode) sdpNode).put("sdp", offer.getSDPMessage().toString());
            ((ObjectNode) rootNode).set("sdp", sdpNode);
            String json = mapper.writeValueAsString(rootNode);
            logger.info("Sending offer:\n{}", json);
            this.session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            logger.error("Couldn't write JSON", e);
        }
    }
}
