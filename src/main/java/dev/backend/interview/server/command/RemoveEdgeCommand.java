package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;

class RemoveEdgeCommand extends CommandBase {
    RemoveEdgeCommand() {
        this.body = CommandBase.REMOVE_EDGE_NAME;
        this.parametersCount = 2;

    }

    @Override
    public String execute(String input, SessionContext context) throws CommandParseException {
        String[] parameters = parseParameters(input);
        String node1 = parameters[0];
        String node2 = parameters[1];

        validateNodeName(node1);
        validateNodeName(node2);

        if (model.removeEdge(node1, node2))
            return "EDGE REMOVED";
        return "ERROR: NODE NOT FOUND";
    }
}
