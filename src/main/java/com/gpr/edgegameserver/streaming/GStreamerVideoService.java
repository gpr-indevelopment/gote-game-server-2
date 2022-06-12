package com.gpr.edgegameserver.streaming;

import org.freedesktop.gstreamer.*;
import org.freedesktop.gstreamer.webrtc.WebRTCBin;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class GStreamerVideoService {

    private static final String VIDEO_BIN_DESCRIPTION = "dxgiscreencapsrc cursor=true ! videoscale ! capsfilter caps=\"video/x-raw,width=1280,height=720,framerate=60/1\" ! queue ! nvh264enc bitrate=4000 rc-mode=cbr gop-size=-1 qos=true preset=low-latency spatial-aq=true nonref-p=true vbv-buffer-size=67 ! capsfilter caps=\"video/x-h264,profile=high\" ! queue ! rtph264pay ! capsfilter caps=\"application/x-rtp,media=video,encoding-name=H264,width=1280,height=720,payload=123\"";

    private static final String PIPELINE_DESCRIPTION
            = "videotestsrc is-live=true pattern=ball ! videoconvert ! queue ! vp8enc deadline=1 ! rtpvp8pay"
            + " ! queue ! application/x-rtp,media=video,encoding-name=VP8,payload=97 ! webrtcbin. "
            + "audiotestsrc is-live=true wave=sine ! audioconvert ! audioresample ! queue ! opusenc ! rtpopuspay"
            + " ! queue ! application/x-rtp,media=audio,encoding-name=OPUS,payload=96 ! webrtcbin. "
            + "webrtcbin name=webrtcbin bundle-policy=max-bundle stun-server=stun://stun.l.google.com:19302 ";

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
