package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;
import dev.backend.interview.server.model.NodeNotFoundException;

import java.util.List;

class CloserThanCommand extends CommandBase {
    CloserThanCommand() {
        this.body = CommandBase.CLOSER_THAN_NAME;
        this.parametersCount = 2;
    }

    @Override
    public String execute(String input, SessionContext context) throws CommandParseException {
        String[] parameters = parseParameters(input);
        Integer limit = getIntegerValue(parameters[0]);
        String node = parameters[1];

        validateNodeName(node);

        try {
            List<String> result = model.closerThan(node, limit);
            return String.join(",", result);

        } catch (NodeNotFoundException ne) {
            return "ERROR: NODE NOT FOUND";
        }
    }
}
