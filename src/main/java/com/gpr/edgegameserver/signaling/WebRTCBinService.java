package com.gpr.edgegameserver.signaling;

import com.gpr.edgegameserver.streaming.WebRTCSession;
import org.freedesktop.gstreamer.SDPMessage;
import org.freedesktop.gstreamer.WebRTCSDPType;
import org.freedesktop.gstreamer.WebRTCSessionDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class WebRTCBinService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebRTCBinService.class);

    private final WebRTCSessionService webRTCSessionService;

    public WebRTCBinService(WebRTCSessionService webRTCSessionService) {
        this.webRTCSessionService = webRTCSessionService;
    }

    public void startSignaling(WebSocketSession session) {
        LOGGER.info("Starting signaling process and setting pipeline to play state. SessionID: {}", session.getId());
        WebRTCSession webRTCSession = webRTCSessionService.loadWebRTCSession(session);
        webRTCSession.getPipeline().play();
    }

    public void receiveSdpAnswer(Sdp sdp, WebSocketSession session) {
        LOGGER.info("Received SDP answer: {}. SessionID: {}", sdp, session.getId());
        SDPMessage sdpMessage = new SDPMessage();
        sdpMessage.parseBuffer(sdp.getSdp());
        WebRTCSession webRTCSession = webRTCSessionService.loadWebRTCSession(session);
        webRTCSession.getWebRTCBin().setRemoteDescription(new WebRTCSessionDescription(WebRTCSDPType.ANSWER, sdpMessage));
    }

    public void receiveIceCandidate(ICECandidate candidate, WebSocketSession session) {
        LOGGER.info("Received ICE Candidate: {}. SessionID: {}", candidate, session.getId());
        WebRTCSession webRTCSession = webRTCSessionService.loadWebRTCSession(session);
        webRTCSession.getWebRTCBin().addIceCandidate(candidate.getSdpMLineIndex(), candidate.getCandidate());
    }
}
