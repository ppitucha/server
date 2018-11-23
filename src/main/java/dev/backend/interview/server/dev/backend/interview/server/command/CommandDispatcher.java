package dev.backend.interview.server.dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandDispatcher {
    private List<Command> commands = new ArrayList<>();

    public CommandDispatcher() {
        commands.add(new HiCommand());
        commands.add(new ByeCommand());
        commands.add(new AddNodeCommand());

    }

    public Command getConnectCommand() {
        return new ConnectCommand();
    }

    public Command getByeCommand() {
        return new ByeCommand();
    }

    public Command getCommand(String inputLine) {
        for (Command command : commands)
            if (command.matching(inputLine))
                return command;
        return new ErrorCommand();
    }

    abstract class CommandBase implements Command {
        @Override
        public boolean matching(String input) {
            return false;
        }

    }

    class ByeCommand extends CommandBase {
        private String text = "BYE MATE!";

        @Override
        public boolean matching(String input) {
            return input.startsWith(text);
        }

        @Override
        public String execute(SessionContext context, String input) {
            return "BYE " + context.getClientName() + ", WE SPOKE FOR " +
                    (new Date().getTime() - context.getStartTime()) + " MS";
        }
    }

    class ConnectCommand extends CommandBase {
        @Override
        public String execute(SessionContext context, String input) {
            return "HI, I'M  " + context.getId();
        }
    }

    class ErrorCommand extends CommandBase {
        @Override
        public String execute(SessionContext context, String input) {
            return "SORRY, I DIDN'T UNDERSTAND THAT";
        }
    }

    class HiCommand extends CommandBase {
        final String text = "HI, I'M ";

        @Override
        public boolean matching(String input) {
            return input.startsWith(text);
        }

        @Override
        public String execute(SessionContext context, String input) {
            context.setClientName(input.substring(input.indexOf(text) + text.length()));
            return "HI " + context.getClientName();
        }
    }

    class AddNodeCommand extends CommandBase {
        private String text = "ADD NODE ";

        @Override
        public boolean matching(String input) {
            return input.startsWith(text);
        }

        @Override
        public String execute(SessionContext context, String input) {
            String nodeName = input.substring(input.indexOf(text) + text.length());

            return "NODE ADDED";
        }
    }

}
