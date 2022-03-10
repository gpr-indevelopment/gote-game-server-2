package com.gpr.edgegameserver.signaling.webrtc.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.freedesktop.gstreamer.elements.WebRTCBin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class WebRTCCallbackService {

    Logger logger = LoggerFactory.getLogger(WebRTCCallbackService.class);

    private final ObjectMapper mapper;

    public WebRTCCallbackService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void setupWebRtc(WebRTCBin webRTCBin, WebSocketSession session) {
        webRTCBin.connect(new OnNegotiationNeededCallback(webRTCBin, mapper, session));
        webRTCBin.connect(new OnIceCandidateCallback(mapper, session));
    }
}
