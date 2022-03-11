package com.gpr.edgegameserver.signaling;

public class ICECandidate {

    private final String candidate;

    private final int sdpMLineIndex;

    public ICECandidate(String candidate, int sdpMLineIndex) {
        this.candidate = candidate;
        this.sdpMLineIndex = sdpMLineIndex;
    }

    public String getCandidate() {
        return candidate;
    }

    public int getSdpMLineIndex() {
        return sdpMLineIndex;
    }

    @Override
    public String toString() {
        return "ICECandidate{" +
                "candidate='" + candidate + '\'' +
                ", sdpMLineIndex=" + sdpMLineIndex +
                '}';
    }
}
