package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;

class HiCommand extends CommandBase {
    HiCommand() {
        this.body = CommandBase.HI_NAME;
        this.parametersCount = 1;
    }

    @Override
    public String execute(String input, SessionContext context) throws CommandParseException {
        String[] parameters = parseParameters(input);
        final String clientName = parameters[0];

        validateClientName(clientName);
        context.setClientName(clientName);

        return String.format("HI %s", context.getClientName());
    }
}
