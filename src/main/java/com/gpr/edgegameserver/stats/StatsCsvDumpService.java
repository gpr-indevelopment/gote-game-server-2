package com.gpr.edgegameserver.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class StatsCsvDumpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsCsvDumpService.class);

    private final StatsRepository statsRepository;

    public StatsCsvDumpService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public StatsCsvDumpResult dumpSession(String sessionId) throws IOException {
        Path csvFile = Files.createTempFile(sessionId + "-", ".csv");
        StringBuilder sb = prepareCsvHeaders();
        List<StreamingStatsRecord> foundRecords = statsRepository.findAllBySessionId(sessionId);
        if (foundRecords != null) {
            foundRecords.forEach(record -> appendRecordToCsv(sb, record));
            Files.write(csvFile, sb.toString().getBytes(StandardCharsets.UTF_8));
            LOGGER.info("Saved streaming stats CSV dump to path: {}", csvFile);
            return buildResult(sessionId, foundRecords, csvFile);
        }
        return buildEmptyResult(sessionId);
    }

    private StatsCsvDumpResult buildEmptyResult(String sessionId) {
        return new StatsCsvDumpResult(null, sessionId, 0, 0L);
    }

    private StatsCsvDumpResult buildResult(String sessionId, List<StreamingStatsRecord> foundRecords, Path csvFile) {
        int totalRecords = foundRecords.size();
        long deltaTimestamp = 0L;
        if (totalRecords >= 1) {
            StreamingStatsRecord firstRecord = foundRecords.get(0);
            StreamingStatsRecord lastRecord = foundRecords.get(foundRecords.size()-1);
            deltaTimestamp = lastRecord.getTimestamp()-firstRecord.getTimestamp();
        }
        StatsCsvDumpResult result = new StatsCsvDumpResult(csvFile.toAbsolutePath().toString(), sessionId, totalRecords, deltaTimestamp);
        LOGGER.info("CSV dump result for SessionID: {}: {}", sessionId, result);
        return result;
    }

    private void appendRecordToCsv(StringBuilder sb, StreamingStatsRecord record) {
        sb
                .append(record.getTimestamp())
                .append(",")
                .append(record.getSessionId())
                .append(",")
                .append(record.getFrameWidth())
                .append(",")
                .append(record.getFrameHeight())
                .append(",")
                .append(record.getFramesPerSecond())
                .append(",")
                .append(record.getFramesDropped())
                .append(",")
                .append(record.getJitter())
                .append(",")
                .append(record.getPacketsLost())
                .append(",")
                .append(record.getBytesReceived())
                .append("\n");
    }

    private StringBuilder prepareCsvHeaders() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("timestamp")
                .append(",")
                .append("sessionId")
                .append(",")
                .append("frameWidth")
                .append(",")
                .append("frameHeight")
                .append(",")
                .append("framesPerSecond")
                .append(",")
                .append("framesDropped")
                .append(",")
                .append("jitter")
                .append(",")
                .append("packetsLost")
                .append(",")
                .append("bytesReceived")
                .append("\n");
        return sb;
    }
}
