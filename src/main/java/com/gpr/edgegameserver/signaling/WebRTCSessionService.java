package com.gpr.edgegameserver.signaling;

import com.gpr.edgegameserver.signaling.webrtc.callback.WebRTCCallbackService;
import com.gpr.edgegameserver.streaming.GStreamerVideoService;
import com.gpr.edgegameserver.streaming.WebRTCSession;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

@Service
public class WebRTCSessionService {

    private final GStreamerVideoService gStreamerVideoService;

    private final WebRTCBinRegistry binRegistry;

    private final WebRTCCallbackService callbackService;

    public WebRTCSessionService(GStreamerVideoService gStreamerVideoService, WebRTCBinRegistry binRegistry, WebRTCCallbackService callbackService) {
        this.gStreamerVideoService = gStreamerVideoService;
        this.binRegistry = binRegistry;
        this.callbackService = callbackService;
    }

    public WebRTCSession loadWebRTCSession(WebSocketSession session) {
        WebRTCSession webRTCSession;
        Optional<WebRTCSession> sessionOpt = binRegistry.retrieveSession(session.getId());
        if (sessionOpt.isPresent()) {
            webRTCSession = sessionOpt.get();
        } else {
            webRTCSession = gStreamerVideoService.prepareWebRTCVideoBin();
            callbackService.setupWebRtc(webRTCSession.getWebRTCBin(), session);
            binRegistry.registerSession(session.getId(), webRTCSession);
        }
        return webRTCSession;
    }
}
