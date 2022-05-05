package com.gpr.edgegameserver.websocket;

import com.gpr.edgegameserver.stats.InMemStatsRepository;
import com.gpr.edgegameserver.stats.StreamingStatsRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class WebSocketStatsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketStatsService.class);

    private final InMemStatsRepository statsRepository;

    public WebSocketStatsService(InMemStatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public void storeStats(StreamingStatsRecord streamingStatsRecord, WebSocketSession session) {
        streamingStatsRecord.setSessionId(session.getId());
        StreamingStatsRecord savedRecord = statsRepository.save(streamingStatsRecord);
        LOGGER.debug("Successfully persisted StreamingStatsRecord {} on SessionID: {}", savedRecord, session.getId());
    }
}
