package com.gpr.edgegameserver.signaling.webrtc.callback;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gpr.edgegameserver.signaling.Sdp;
import com.gpr.edgegameserver.websocket.WebSocketMessage;
import org.freedesktop.gstreamer.WebRTCSessionDescription;
import org.freedesktop.gstreamer.elements.WebRTCBin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static com.gpr.edgegameserver.websocket.WebSocketMessageType.SDP_OFFER;

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
            Sdp sdp = new Sdp(offer.getSDPMessage().toString(), "offer");
            WebSocketMessage message = new WebSocketMessage(SDP_OFFER, mapper.valueToTree(sdp));
            this.session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            logger.error("Couldn't write JSON", e);
        }
    }
}
