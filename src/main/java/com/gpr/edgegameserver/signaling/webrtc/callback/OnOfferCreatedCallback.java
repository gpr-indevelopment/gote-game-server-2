package com.gpr.edgegameserver.signaling.webrtc.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpr.edgegameserver.signaling.Sdp;
import com.gpr.edgegameserver.websocket.WebSocketMessage;
import org.freedesktop.gstreamer.webrtc.WebRTCBin;
import org.freedesktop.gstreamer.webrtc.WebRTCSessionDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static com.gpr.edgegameserver.websocket.WebSocketMessageType.SDP_OFFER;

public class OnOfferCreatedCallback implements WebRTCBin.CREATE_OFFER {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnOfferCreatedCallback.class);

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
        String sdpOffer = offer.getSDPMessage().toString();
        LOGGER.info("Created local WebRTCBin SDP offer: {}. SessionID: {}", sdpOffer, session.getId());
        webRTCBin.setLocalDescription(offer);
        try {
            Sdp sdp = new Sdp(sdpOffer, "offer");
            WebSocketMessage message = new WebSocketMessage(SDP_OFFER, mapper.valueToTree(sdp));
            this.session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            LOGGER.error("Couldn't create WebSocketMessage object for SDP offer: " + offer.getSDPMessage().toString(), e);
        }
    }
}
