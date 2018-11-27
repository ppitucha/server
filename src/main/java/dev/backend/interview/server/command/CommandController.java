package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;
import dev.backend.interview.server.model.Model;
import dev.backend.interview.server.model.ModelProvider;
import dev.backend.interview.server.model.NodeNotFoundException;

import java.util.ArrayList;
import java.util.Date;
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

    private Command getCommand(String input) {
        for (Command command : commands)
            if (command.matching(input))
                return command;
        return new ErrorCommand();
    }

    abstract class CommandBase implements Command {
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
                throw new CommandParseException();

            return parameters;
        }


    }

    class ByeCommand extends CommandBase {
        ByeCommand() {
            this.body = Command.BYE_MATE_NAME;
        }

        @Override
        public String execute(String input, SessionContext context) {
            return "BYE " + context.getClientName() + ", WE SPOKE FOR " +
                    (new Date().getTime() - context.getStartTime()) + " MS";
        }
    }

    class ConnectCommand extends CommandBase {
        ConnectCommand() {
            this.body = Command.CONNECT_NAME;
        }

        @Override
        public String execute(String input, SessionContext context) {
            return "HI, I'M  " + context.getId();
        }
    }

    class ErrorCommand extends CommandBase {
        ErrorCommand() {
            this.body = Command.ERROR_NAME;
        }

        @Override
        public String execute(String input, SessionContext context) {
            return "SORRY, I DIDN'T UNDERSTAND THAT";
        }
    }

    class HiCommand extends CommandBase {
        HiCommand() {
            this.body = Command.HI_NAME;
            this.parametersCount = 1;
        }

        @Override
        public String execute(String input, SessionContext context) throws CommandParseException {
            String[] parameters = parseParameters(input);
            //TODO validation
            context.setClientName(parameters[0]);

            return "HI " + context.getClientName();
        }
    }

    class AddNodeCommand extends CommandBase {
        AddNodeCommand() {
            this.body = Command.ADD_NODE_NAME;
            this.parametersCount = 1;
        }

        @Override
        public String execute(String input, SessionContext context) throws CommandParseException {
            String[] parameters = parseParameters(input);

            if (model.addNode(parameters[0]))
                return "NODE ADDED";

            return "ERROR: NODE ALREADY EXISTS";
        }
    }

    class AddEdgeCommand extends CommandBase {
        AddEdgeCommand() {
            this.body = Command.ADD_EDGE_NAME;
            this.parametersCount = 3;
        }

        @Override
        public String execute(String input, SessionContext context) throws CommandParseException {
            String[] parameters = parseParameters(input);
            String node1 = parameters[0];
            String node2 = parameters[1];
            Integer weight = Integer.valueOf(parameters[2]);

            if (model.addEdge(node1, node2, weight))
                return "EDGE ADDED";
            return "ERROR: NODE NOT FOUND";
        }
    }

    class RemoveNodeCommand extends CommandBase {
        RemoveNodeCommand() {
            this.body = Command.REMOVE_NODE_NAME;
            this.parametersCount = 1;
        }

        @Override
        public String execute(String input, SessionContext context) throws CommandParseException {
            String[] parameters = parseParameters(input);
            String nodeName = parameters[0];

            if (model.removeNode(nodeName))
                return "NODE REMOVED";

            return "ERROR: NODE NOT FOUND";
        }
    }

    class RemoveEdgeCommand extends CommandBase {
        RemoveEdgeCommand() {
            this.body = Command.REMOVE_EDGE_NAME;
            this.parametersCount = 2;

        }

        @Override
        public String execute(String input, SessionContext context) throws CommandParseException {
            String[] parameters = parseParameters(input);
            String node1 = parameters[0];
            String node2 = parameters[1];

            if (model.removeEdge(node1, node2))
                return "EDGE REMOVED";
            return "ERROR: NODE NOT FOUND";
        }
    }

    class ShortestPathCommand extends CommandBase {
        ShortestPathCommand() {
            this.body = Command.SHORTEST_PATH_NAME;
            this.parametersCount = 2;
        }

        @Override
        public String execute(String input, SessionContext context) throws CommandParseException {
            String[] parameters = parseParameters(input);
            String node1 = parameters[0];
            String node2 = parameters[1];

            try {
                return String.valueOf(model.shortestPath(node1, node2));
            } catch (NodeNotFoundException ne) {
                return "ERROR: NODE NOT FOUND";
            }
        }
    }

    class CloserThanCommand extends CommandBase {
        CloserThanCommand() {
            this.body = Command.CLOSER_THAN_NAME;
            this.parametersCount = 2;
        }

        @Override
        public String execute(String input, SessionContext context) throws CommandParseException {
            String[] parameters = parseParameters(input);
            Integer limit = Integer.valueOf(parameters[0]);
            String node = parameters[1];

            try {
                List<String> result = model.closerThan(node, limit);
                return String.join(",", result);

            } catch (NodeNotFoundException ne) {
                return "ERROR: NODE NOT FOUND";
            }
        }
    }
}
