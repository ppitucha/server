package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;

public interface Command {

    boolean matching(String input);

    String execute(String input, SessionContext context) throws CommandParseException;

}
