package dev.backend.interview.server.dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;

public interface Command {
    boolean matching(String input);

    String execute(SessionContext context, String input);

}
