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

    private final WebRTCSessionRegistry sessionRegistry;

    private final WebRTCCallbackService callbackService;

    public WebRTCSessionService(GStreamerVideoService gStreamerVideoService, WebRTCSessionRegistry sessionRegistry, WebRTCCallbackService callbackService) {
        this.gStreamerVideoService = gStreamerVideoService;
        this.sessionRegistry = sessionRegistry;
        this.callbackService = callbackService;
    }

    public void removeSession(WebSocketSession session) {
        removeSession(session.getId());
    }

    public void removeSession(String id) {
        retrieveWebRTCSession(id).ifPresent(session -> {
            session.getPipeline().stop();
            session.getWebRTCBin().dispose();
            sessionRegistry.removeSession(id);
        });
    }

    public Optional<WebRTCSession> retrieveWebRTCSession(String id) {
        return sessionRegistry.retrieveSession(id);
    }

    public WebRTCSession loadOrCreateWebRTCSession(WebSocketSession webSocketSession) {
        WebRTCSession webRTCSession;
        Optional<WebRTCSession> sessionOpt = retrieveWebRTCSession(webSocketSession.getId());
        if (sessionOpt.isPresent()) {
            webRTCSession = sessionOpt.get();
        } else {
            webRTCSession = gStreamerVideoService.prepareWebRTCVideoBin();
            callbackService.setupWebRtc(webRTCSession.getWebRTCBin(), webSocketSession);
            sessionRegistry.registerSession(webSocketSession.getId(), webRTCSession);
        }
        return webRTCSession;
    }
}
