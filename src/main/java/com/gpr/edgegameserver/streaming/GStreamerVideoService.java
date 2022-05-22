package com.gpr.edgegameserver.streaming;

import org.freedesktop.gstreamer.*;
import org.freedesktop.gstreamer.webrtc.WebRTCBin;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class GStreamerVideoService {

    private static final String VIDEO_BIN_DESCRIPTION = "dxgiscreencapsrc cursor=true ! capsfilter caps=\"video/x-raw,framerate=60/1\" ! queue ! nvh264enc bitrate=2250 rc-mode=vbr gop-size=-1 qos=true preset=low-latency-hq ! capsfilter caps=\"video/x-h264,profile=high\" ! queue ! rtph264pay ! capsfilter caps=\"application/x-rtp,media=video,encoding-name=H264,width=1280,height=720,payload=123\"";

    @PreDestroy
    private void destroy() {
        Gst.quit();
    }

    public WebRTCSession prepareWebRTCVideoBin() {
        Gst.init(new Version(1, 14), "gote-game-server-2");
        WebRTCBin webRTCBin = new WebRTCBin("gstreamer-webrtc-server");
        Bin video = Gst.parseBinFromDescription(VIDEO_BIN_DESCRIPTION, true);
        Pipeline pipeline = new Pipeline();
        pipeline.addMany(webRTCBin, video);
        video.link(webRTCBin);
        setMediaDirection(webRTCBin);
        webRTCBin.setTurnServer("turn://webrtc%40live.com:muazkh@numb.viagenie.ca");
        webRTCBin.setStunServer("stun:stun.services.mozilla.com");
        return new WebRTCSession(webRTCBin, pipeline);
    }

    // Forcing transmission to be unidirectional (send only)
    private void setMediaDirection(WebRTCBin webRTCBin) {
        GstObject videoTransceiver = webRTCBin.emit(GstObject.class, "get-transceiver", 0);
        videoTransceiver.set("direction", 2);
//        GstObject audioTransceiver = webRTCBin.emit(GstObject.class, "get-transceiver", 1);
//        audioTransceiver.set("direction", 2);
    }
}
