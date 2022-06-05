package com.gpr.edgegameserver.streaming;

import org.freedesktop.gstreamer.*;
import org.freedesktop.gstreamer.webrtc.WebRTCBin;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class GStreamerVideoService {

    private static final String VIDEO_BIN_DESCRIPTION = "dxgiscreencapsrc cursor=true ! videoconvert ! queue ! nvh264enc bitrate=2250 rc-mode=vbr gop-size=-1 qos=true preset=low-latency-hq ! capsfilter caps=\"video/x-h264,profile=high\" ! queue ! rtph264pay ! capsfilter caps=\"application/x-rtp,media=video,encoding-name=H264,width=1280,height=720,payload=123\"";

    private static final String STUN_SERVER = "stun://stun.l.google.com:19302";

    @PreDestroy
    private void destroy() {
        Gst.quit();
    }

    public WebRTCSession prepareWebRTCVideoBin() {
        Gst.init(new Version(1, 19), "gote-game-server-2");
        WebRTCBin webRTCBin = new WebRTCBin("gstreamer-webrtc-server");
        Bin video = Gst.parseBinFromDescription(VIDEO_BIN_DESCRIPTION, true);
        Pipeline pipeline = new Pipeline();
        pipeline.addMany(webRTCBin, video);
        video.link(webRTCBin);
        webRTCBin.setStunServer(STUN_SERVER);
        return new WebRTCSession(webRTCBin, pipeline);
    }

    // Forcing transmission to be unidirectional (send only)
    // There is a bug here that prevents the unidirectional stream from working between NATs
    private void setMediaDirection(WebRTCBin webRTCBin) {
        GstObject videoTransceiver = webRTCBin.emit(GstObject.class, "get-transceiver", 0);
        videoTransceiver.set("direction", 2);
//        GstObject audioTransceiver = webRTCBin.emit(GstObject.class, "get-transceiver", 1);
//        audioTransceiver.set("direction", 2);
    }
}
