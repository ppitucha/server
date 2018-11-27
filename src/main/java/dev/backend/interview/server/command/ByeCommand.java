package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;

import java.util.Date;

class ByeCommand extends CommandBase {
    ByeCommand() {
        this.body = CommandBase.BYE_MATE_NAME;
    }

    @Override
    public String execute(String input, SessionContext context) {
        return String.format("BYE %s, WE SPOKE FOR %d MS", context.getClientName(),
                (new Date().getTime() - context.getStartTime()));
    }
}
