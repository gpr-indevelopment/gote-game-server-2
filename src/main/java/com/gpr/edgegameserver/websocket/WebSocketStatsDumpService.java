package com.gpr.edgegameserver.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpr.edgegameserver.stats.StatsCsvDumpResult;
import com.gpr.edgegameserver.stats.StatsCsvDumpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static com.gpr.edgegameserver.websocket.WebSocketMessageType.FINISH_STATS_DUMP;

@Service
public class WebSocketStatsDumpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketStatsDumpService.class);

    private final StatsCsvDumpService statsCsvDumpService;

    private final ObjectMapper mapper;

    public WebSocketStatsDumpService(StatsCsvDumpService statsCsvDumpService, ObjectMapper mapper) {
        this.statsCsvDumpService = statsCsvDumpService;
        this.mapper = mapper;
    }

    public void dumpSession(WebSocketSession session) {
        try {
            StatsCsvDumpResult result = statsCsvDumpService.dumpSession(session.getId());
            if (result.getTotalRecords() == 0) {
                LOGGER.info("Dump has no records for SessionID: {}. Will ignore.", session.getId());
                return;
            }
            LOGGER.info("Sending csv dump result of SessionID: {} back to WS with FINISH_STATS_DUMP type.", session.getId());
            WebSocketMessage message = new WebSocketMessage(FINISH_STATS_DUMP, mapper.valueToTree(result));
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            String message = String.format("IOException while dumping stats session from SessionID: %s. Will ignore", session.getId());
            LOGGER.warn(message, e);
        }
    }
}
