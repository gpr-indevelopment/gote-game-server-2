package com.gpr.edgegameserver.streaming;

import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.elements.WebRTCBin;

public class WebRTCSession {

    private final WebRTCBin webRTCBin;

    private final Pipeline pipeline;

    public WebRTCSession(WebRTCBin webRTCBin, Pipeline pipeline) {
        this.webRTCBin = webRTCBin;
        this.pipeline = pipeline;
    }

    public WebRTCBin getWebRTCBin() {
        return webRTCBin;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }
}
