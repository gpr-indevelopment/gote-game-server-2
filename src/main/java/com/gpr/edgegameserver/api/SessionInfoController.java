package com.gpr.edgegameserver.api;

import com.gpr.edgegameserver.signaling.WebRTCSessionService;
import org.freedesktop.gstreamer.GstObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionInfoController {

    private final WebRTCSessionService webRTCSessionService;

    public SessionInfoController(WebRTCSessionService webRTCSessionService) {
        this.webRTCSessionService = webRTCSessionService;
    }

    @GetMapping("/sessions/{id}/info")
    public void getInfo(@PathVariable String id) {
        GstObject transceiver = webRTCSessionService.retrieveWebRTCSession(id).get().getWebRTCBin().emit(GstObject.class, "get-transceiver", 0);
        GstObject gstObject = (GstObject) transceiver.get("sender");
        System.out.println(gstObject.toString());
    }
}
