package com.gpr.edgegameserver.api;

import com.gpr.edgegameserver.stats.StatsCsvDumpResult;
import com.gpr.edgegameserver.stats.StatsCsvDumpService;
import com.gpr.edgegameserver.stats.StatsRepository;
import com.gpr.edgegameserver.stats.StreamingStatsRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class StreamingStatsController {

    private final StatsRepository statsRepository;

    private final StatsCsvDumpService statsCsvDumpService;

    public StreamingStatsController(StatsRepository statsRepository, StatsCsvDumpService statsCsvDumpService) {
        this.statsRepository = statsRepository;
        this.statsCsvDumpService = statsCsvDumpService;
    }

    @GetMapping("/sessions/{id}/streaming-stats")
    public List<StreamingStatsRecord> getSessionStats(@PathVariable String id) {
        return statsRepository.findAllBySessionId(id);
    }

    @PostMapping("/sessions/{id}/streaming-stats/csv-dump")
    public StatsCsvDumpResult dumpStatsCsv(@PathVariable String id) throws IOException {
        return statsCsvDumpService.dumpSession(id);
    }
}
