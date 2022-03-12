package com.gpr.edgegameserver.signaling.webrtc.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.elements.WebRTCBin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

public class OnNegotiationNeededCallback implements WebRTCBin.ON_NEGOTIATION_NEEDED {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnNegotiationNeededCallback.class);

    private final WebRTCBin webRTCBin;

    private final ObjectMapper mapper;

    private final WebSocketSession session;

    public OnNegotiationNeededCallback(WebRTCBin webRTCBin, ObjectMapper mapper, WebSocketSession session) {
        this.webRTCBin = webRTCBin;
        this.mapper = mapper;
        this.session = session;
    }

    @Override
    public void onNegotiationNeeded(Element elem) {
        LOGGER.info("Firing callback onNegotiationNeeded on SessionID: {} for element: {}", session.getId(), elem.getName());
        this.webRTCBin.createOffer(new OnOfferCreatedCallback(webRTCBin, mapper, session));
    }
}
