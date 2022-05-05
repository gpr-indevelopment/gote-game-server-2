package com.gpr.edgegameserver.stats;

public class StreamingStatsRecord {

    private int frameWidth;

    private int frameHeight;

    private double framesPerSecond;

    private int framesDropped;

    private double jitter;

    private int packetsLost;

    private long bytesReceived;

    private long timestamp;

    private String sessionId;

    public double getFramesPerSecond() {
        return framesPerSecond;
    }

    public void setFramesPerSecond(double framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
    }

    public int getFramesDropped() {
        return framesDropped;
    }

    public void setFramesDropped(int framesDropped) {
        this.framesDropped = framesDropped;
    }

    public double getJitter() {
        return jitter;
    }

    public void setJitter(double jitter) {
        this.jitter = jitter;
    }

    public int getPacketsLost() {
        return packetsLost;
    }

    public void setPacketsLost(int packetsLost) {
        this.packetsLost = packetsLost;
    }

    public long getBytesReceived() {
        return bytesReceived;
    }

    public void setBytesReceived(long bytesReceived) {
        this.bytesReceived = bytesReceived;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    @Override
    public String toString() {
        return "StreamingStatsRecord{" +
                "frameWidth=" + frameWidth +
                ", frameHeight=" + frameHeight +
                ", framesPerSecond=" + framesPerSecond +
                ", framesDropped=" + framesDropped +
                ", jitter=" + jitter +
                ", packetsLost=" + packetsLost +
                ", bytesReceived=" + bytesReceived +
                ", timestamp=" + timestamp +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final StreamingStatsRecord record = new StreamingStatsRecord();

        private Builder() {
        }

        public Builder frameHeight(int height) {
            record.setFrameHeight(height);
            return this;
        }

        public Builder frameWidth(int width) {
            record.setFrameWidth(width);
            return this;
        }

        public Builder packetsLost(int packetsLost) {
            record.setPacketsLost(packetsLost);
            return this;
        }

        public Builder jitter(double jitter) {
            record.setJitter(jitter);
            return this;
        }

        public Builder framesDropped(int framesDropped) {
            record.setFramesDropped(framesDropped);
            return this;
        }

        public Builder framesPerSecond(double fps) {
            record.setFramesPerSecond(fps);
            return this;
        }

        public Builder timestamp(long timestamp) {
            record.setTimestamp(timestamp);
            return this;
        }

        public Builder bytesReceived(long bytesReceived) {
            record.setBytesReceived(bytesReceived);
            return this;
        }

        public Builder sessionId(String sessionId) {
            record.setSessionId(sessionId);
            return this;
        }

        public StreamingStatsRecord build() {
            return record;
        }
    }
}
