package com.gpr.edgegameserver.stats;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemStatsRepository implements StatsRepository {

    private final Map<String, List<StreamingStatsRecord>> sessionIdToRecords = new HashMap<>();

    @Override
    public StreamingStatsRecord save(StreamingStatsRecord record) {
        List<StreamingStatsRecord> records = sessionIdToRecords.computeIfAbsent(record.getSessionId(), k -> new ArrayList<>());
        records.add(record);
        return record;
    }

    @Override
    public List<StreamingStatsRecord> findAll() {
        List<StreamingStatsRecord> result = new ArrayList<>();
        for (List<StreamingStatsRecord> storedList : sessionIdToRecords.values()) {
            result.addAll(storedList);
        }
        return result;
    }

    @Override
    public List<StreamingStatsRecord> findAllBySessionId(String sessionId) {
        return sessionIdToRecords.get(sessionId);
    }
}
