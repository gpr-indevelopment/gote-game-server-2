package com.gpr.edgegameserver.api;

import com.gpr.edgegameserver.stats.StatsRepository;
import com.gpr.edgegameserver.stats.StreamingStatsRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StreamingStatsController {

    private final StatsRepository statsRepository;

    public StreamingStatsController(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @GetMapping("/sessions/{id}/streaming-stats")
    public List<StreamingStatsRecord> getSessionStats(@PathVariable String id) {
        return statsRepository.findAllBySessionId(id);
    }
}
