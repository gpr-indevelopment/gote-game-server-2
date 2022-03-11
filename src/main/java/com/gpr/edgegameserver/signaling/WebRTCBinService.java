package com.gpr.edgegameserver.signaling;

import com.gpr.edgegameserver.signaling.webrtc.callback.WebRTCCallbackService;
import com.gpr.edgegameserver.streaming.GStreamerVideoService;
import com.gpr.edgegameserver.streaming.WebRTCSession;
import org.freedesktop.gstreamer.SDPMessage;
import org.freedesktop.gstreamer.WebRTCSDPType;
import org.freedesktop.gstreamer.WebRTCSessionDescription;
import org.freedesktop.gstreamer.elements.WebRTCBin;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

@Service
public class WebRTCBinService {

    private final WebRTCSessionService webRTCSessionService;

    public WebRTCBinService(WebRTCSessionService webRTCSessionService) {
        this.webRTCSessionService = webRTCSessionService;
    }

    public void startSignaling(WebSocketSession session) {
        WebRTCSession webRTCSession = webRTCSessionService.loadWebRTCSession(session);
        webRTCSession.getPipeline().play();
    }

    public void receiveSdpAnswer(Sdp sdp, WebSocketSession session) {
        SDPMessage sdpMessage = new SDPMessage();
        sdpMessage.parseBuffer(sdp.getSdp());
        WebRTCSession webRTCSession = webRTCSessionService.loadWebRTCSession(session);
        webRTCSession.getWebRTCBin().setRemoteDescription(new WebRTCSessionDescription(WebRTCSDPType.ANSWER, sdpMessage));
    }
}
