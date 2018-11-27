package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;

public interface Command {

    boolean matching(String input);

    String execute(String input, SessionContext context) throws CommandParseException;

    String BYE_MATE_NAME = "BYE MATE!";
    String CONNECT_NAME = "__CONNECT__";
    String ERROR_NAME = "__ERROR__";
    String HI_NAME = "HI, I'M ";
    String ADD_NODE_NAME = "ADD NODE ";
    String ADD_EDGE_NAME = "ADD EDGE ";
    String REMOVE_NODE_NAME = "REMOVE NODE ";
    String REMOVE_EDGE_NAME = "REMOVE EDGE ";
    String SHORTEST_PATH_NAME = "SHORTEST PATH ";
    String CLOSER_THAN_NAME = "CLOSER THAN ";
}
