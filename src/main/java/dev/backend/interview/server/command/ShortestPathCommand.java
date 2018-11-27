package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;
import dev.backend.interview.server.model.NodeNotFoundException;

class ShortestPathCommand extends CommandBase {
    ShortestPathCommand() {
        this.body = CommandBase.SHORTEST_PATH_NAME;
        this.parametersCount = 2;
    }

    @Override
    public String execute(String input, SessionContext context) throws CommandParseException {
        String[] parameters = parseParameters(input);
        String node1 = parameters[0];
        String node2 = parameters[1];

        validateNodeName(node1);
        validateNodeName(node2);

        try {
            return String.valueOf(model.shortestPath(node1, node2));
        } catch (NodeNotFoundException ne) {
            return "ERROR: NODE NOT FOUND";
        }
    }
}
