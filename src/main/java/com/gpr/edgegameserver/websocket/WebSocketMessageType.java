package com.gpr.edgegameserver.websocket;

public enum WebSocketMessageType {
    START, STOP, SDP_ANSWER, SDP_OFFER, ICE_CANDIDATE, STATS_RECORD, START_STATS_DUMP, FINISH_STATS_DUMP
}
