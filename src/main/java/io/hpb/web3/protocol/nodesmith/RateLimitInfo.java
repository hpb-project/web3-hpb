package io.hpb.web3.protocol.nodesmith;

import java.time.Instant;
import java.util.Optional;


public class RateLimitInfo {

    private long limit;
    private long remaining;
    private Instant resetTime;

    RateLimitInfo(long limit, long remaining, Instant resetTime) {
        this.limit = limit;
        this.remaining = remaining;
        this.resetTime = resetTime;
    }

    
    public long getTotalAllowedInWindow() {
        return this.limit;
    }

    
    public long getRemainingInWindow() {
        return this.remaining;
    }

    
    public Instant getWindowResetTime() {
        return this.resetTime;
    }

    
    public static Optional<RateLimitInfo> createFromHeaders(
            String limitValue, String remainingValue, String resetTimeValue) {

        try {
            long limit = Long.parseLong(limitValue);
            long remaining = Long.parseLong(remainingValue);
            long resetEpochSeconds = Long.parseLong(resetTimeValue);
            Instant resetTime = Instant.ofEpochSecond(resetEpochSeconds);
            return Optional.of(new RateLimitInfo(limit, remaining, resetTime));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }
}
