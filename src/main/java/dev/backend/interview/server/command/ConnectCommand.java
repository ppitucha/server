package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;

class ConnectCommand extends CommandBase {
    ConnectCommand() {
        this.body = CommandBase.CONNECT_NAME;
    }

    @Override
    public String execute(String input, SessionContext context) {
        return String.format("HI, I'M %s", context.getSessionId());
    }
}
