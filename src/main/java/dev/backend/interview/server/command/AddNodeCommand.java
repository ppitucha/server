package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;

class AddNodeCommand extends CommandBase {
    AddNodeCommand() {
        this.body = CommandBase.ADD_NODE_NAME;
        this.parametersCount = 1;
    }

    @Override
    public String execute(String input, SessionContext context) throws CommandParseException {
        String[] parameters = parseParameters(input);
        final String nodeName = parameters[0];

        validateNodeName(nodeName);
        if (model.addNode(nodeName))
            return "NODE ADDED";

        return "ERROR: NODE ALREADY EXISTS";
    }
}
