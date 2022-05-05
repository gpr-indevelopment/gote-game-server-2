package com.gpr.edgegameserver.stats;

public class StatsCsvDumpResult {

    private final String filePath;

    private final String sessionId;

    private final int totalRecords;

    private final long deltaTimestamp;

    public StatsCsvDumpResult(String filePath, String sessionId, int totalRecords, long deltaTimestamp) {
        this.filePath = filePath;
        this.sessionId = sessionId;
        this.totalRecords = totalRecords;
        this.deltaTimestamp = deltaTimestamp;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public long getDeltaTimestamp() {
        return deltaTimestamp;
    }

    @Override
    public String toString() {
        return "StatsCsvDumpResult{" +
                "filePath='" + filePath + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", totalRecords=" + totalRecords +
                ", deltaTimestamp=" + deltaTimestamp +
                '}';
    }
}
