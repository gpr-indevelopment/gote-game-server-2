package com.gpr.edgegameserver.signaling;

public class Sdp {

    private final String body;

    public Sdp(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Sdp{" +
                "body='" + body + '\'' +
                '}';
    }
}
