package com.gpr.edgegameserver.signaling;

public class Sdp {

    private final String sdp;

    private final String type;

    public Sdp(String sdp, String type) {
        this.sdp = sdp;
        this.type = type;
    }

    public String getSdp() {
        return sdp;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Sdp{" +
                "sdp='" + sdp + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
