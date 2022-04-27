package com.gpr.edgegameserver.signaling.webrtc.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpr.edgegameserver.signaling.ICECandidate;
import com.gpr.edgegameserver.websocket.WebSocketMessage;
import org.freedesktop.gstreamer.webrtc.WebRTCBin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static com.gpr.edgegameserver.websocket.WebSocketMessageType.ICE_CANDIDATE;

public class OnIceCandidateCallback implements WebRTCBin.ON_ICE_CANDIDATE {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnIceCandidateCallback.class);

    private final ObjectMapper mapper;

    private final WebSocketSession session;

    public OnIceCandidateCallback(ObjectMapper mapper, WebSocketSession session) {
        this.mapper = mapper;
        this.session = session;
    }

    @Override
    public void onIceCandidate(int sdpMLineIndex, String candidate) {
        LOGGER.info("Generated local ICE Candidate: {} on SessionID: {}. Will not send to remote.", candidate, session.getId());
        // Since this stream is one-way, there is no need for the peer to know this server's ICE candidates.
        // Call below function if client requires ICE candidates from this server.
        // sendLocalIceCandidate(sdpMLineIndex, candidate);
    }

    private void sendLocalIceCandidate(int sdpMLineIndex, String candidate) {
        ICECandidate candidateObj = new ICECandidate(candidate, sdpMLineIndex);
        try {
            WebSocketMessage message = new WebSocketMessage(ICE_CANDIDATE, mapper.valueToTree(candidateObj));
            this.session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            LOGGER.error("Couldn't create WebSocketMessage object for ICE Candidate: " + candidate, e);
        }
    }
}
