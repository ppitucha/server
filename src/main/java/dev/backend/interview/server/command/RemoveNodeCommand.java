package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;

class RemoveNodeCommand extends CommandBase {
    RemoveNodeCommand() {
        this.body = CommandBase.REMOVE_NODE_NAME;
        this.parametersCount = 1;
    }

    @Override
    public String execute(String input, SessionContext context) throws CommandParseException {
        String[] parameters = parseParameters(input);
        String nodeName = parameters[0];

        validateNodeName(nodeName);

        if (model.removeNode(nodeName))
            return "NODE REMOVED";

        return "ERROR: NODE NOT FOUND";
    }
}
