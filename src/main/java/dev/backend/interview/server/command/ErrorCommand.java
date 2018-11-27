package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;

class ErrorCommand extends CommandBase {
    ErrorCommand() {
        this.body = CommandBase.ERROR_NAME;
    }

    @Override
    public String execute(String input, SessionContext context) {
        return "SORRY, I DIDN'T UNDERSTAND THAT";
    }
}
