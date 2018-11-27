package dev.backend.interview.server;

import lombok.Data;

@Data
public class SessionContext {
    private String sessionId;
    private long startTime;
    private String clientName;
}
