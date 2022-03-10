package com.gpr.edgegameserver.signaling;

public class MalformattedSignalingException extends Exception {

    public MalformattedSignalingException(String message) {
        super(message);
    }

    public MalformattedSignalingException(String message, Throwable cause) {
        super(message, cause);
    }
}
