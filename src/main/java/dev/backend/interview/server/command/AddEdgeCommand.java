package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;

class AddEdgeCommand extends CommandBase {
    AddEdgeCommand() {
        this.body = CommandBase.ADD_EDGE_NAME;
        this.parametersCount = 3;
    }

    @Override
    public String execute(String input, SessionContext context) throws CommandParseException {
        String[] parameters = parseParameters(input);
        String node1 = parameters[0];
        String node2 = parameters[1];
        Integer weight = Integer.valueOf(parameters[2]);

        validateNodeName(node1);
        validateNodeName(node2);
        validateWeight(weight);

        if (model.addEdge(node1, node2, weight))
            return "EDGE ADDED";
        return "ERROR: NODE NOT FOUND";
    }
}
