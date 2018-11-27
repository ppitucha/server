package dev.backend.interview.server.command;

import dev.backend.interview.server.model.Model;
import dev.backend.interview.server.model.ModelProvider;

public abstract class CommandBase implements Command {
    public static final String BYE_MATE_NAME = "BYE MATE!";
    public static final String CONNECT_NAME = "__CONNECT__";
    public static final String ERROR_NAME = "__ERROR__";
    public static final String HI_NAME = "HI, I'M ";
    public static final String ADD_NODE_NAME = "ADD NODE ";
    public static final String ADD_EDGE_NAME = "ADD EDGE ";
    public static final String REMOVE_NODE_NAME = "REMOVE NODE ";
    public static final String REMOVE_EDGE_NAME = "REMOVE EDGE ";
    public static final String SHORTEST_PATH_NAME = "SHORTEST PATH ";
    public static final String CLOSER_THAN_NAME = "CLOSER THAN ";

    protected String body;
    protected int parametersCount;

    Model model = ModelProvider.getModel();

    @Override
    public boolean matching(String input) {
        return input.startsWith(body);
    }

    protected String[] parseParameters(String input) throws CommandParseException {
        String[] parameters = input.substring(input.indexOf(body) + body.length()).split("\\s+");

        if (parameters.length != parametersCount)
            throw new CommandParseException(String.format("Cannot parse command: %s, expected parameters count: %d",
                    input, this.parametersCount));

        return parameters;
    }

    protected boolean notAlphaNumeric(String name) {
        String regex = "^[a-zA-Z0-9\\-]*$";
        return !name.matches(regex);
    }

    protected void validateNodeName(String name) throws CommandParseException {
        if (notAlphaNumeric(name))
            throw new CommandParseException(String.format(
                    "Wrong node name: %s, it has to be alphanumeric + the dash character (-)", name));

    }

    protected void validateClientName(String name) throws CommandParseException {
        if (notAlphaNumeric(name))
            throw new CommandParseException(String.format(
                    "Wrong client name: %s, it has to be alphanumeric + the dash character (-)", name));
    }

    protected void validateWeight(int weight) throws CommandParseException {
        if (weight <= 0)
            throw new CommandParseException(String.format(
                    "Wrong weight: %s, it has to positive integer", weight));
    }
}
