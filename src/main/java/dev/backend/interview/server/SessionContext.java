package dev.backend.interview.server;

import dev.backend.interview.server.model.Model;
import lombok.Data;

import java.util.UUID;

@Data
public class SessionContext {
    private UUID id;
    private long startTime;
    private String clientName;
    private Model model;
}
