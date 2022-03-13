package com.gpr.edgegameserver.signaling;

import com.gpr.edgegameserver.streaming.WebRTCSession;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.elements.WebRTCBin;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class WebRTCBinRegistry {

    private final Map<String, WebRTCSession> sessionMap = new HashMap<>();

    public void registerSession(String id, WebRTCBin bin, Pipeline pipeline) {
        registerSession(id, new WebRTCSession(bin, pipeline));
    }

    public void registerSession(String id, WebRTCSession webRTCSession) {
        sessionMap.put(id, webRTCSession);
    }

    public Optional<WebRTCSession> retrieveSession(String id) {
        return Optional.ofNullable(sessionMap.get(id));
    }

    public void removeSession(String id) {
        sessionMap.remove(id);
    }
}
