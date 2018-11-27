package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;

import java.util.ArrayList;
import java.util.List;

public class CommandController {
    private List<Command> commands = new ArrayList<>();

    public CommandController() {
        commands.add(new ConnectCommand());
        commands.add(new HiCommand());
        commands.add(new ByeCommand());
        commands.add(new AddNodeCommand());
        commands.add(new RemoveNodeCommand());
        commands.add(new AddEdgeCommand());
        commands.add(new RemoveEdgeCommand());
        commands.add(new ShortestPathCommand());
        commands.add(new CloserThanCommand());
    }

    public String execute(String input, SessionContext context) {
        Command command = getCommand(input);
        try {
            return command.execute(input, context);
        } catch (CommandParseException cpe) {
            return new ErrorCommand().execute(null, context);
        }
    }

    protected Command getCommand(String input) {
        for (Command command : commands)
            if (command.matching(input))
                return command;
        return new ErrorCommand();
    }
}