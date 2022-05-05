package com.gpr.edgegameserver.stats;

import java.util.List;

public interface StatsRepository {

    StreamingStatsRecord save(StreamingStatsRecord record);

    List<StreamingStatsRecord> findAll();

    List<StreamingStatsRecord> findAllBySessionId(String sessionId);
}
