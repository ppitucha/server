package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;
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
        return command.execute(context, input);
    }

    private Command getCommand(String input) {
        for (Command command : commands)
            if (command.matching(input))
                return command;
        return new ErrorCommand();
    }

    abstract class CommandBase implements Command {
        protected String body;

        @Override
        public boolean matching(String input) {
            return input.startsWith(body);
        }

        protected String[] parseParameters(String input) {
            return input.substring(input.indexOf(body) + body.length()).split("\\s+");
        }


    }

    class ByeCommand extends CommandBase {
        ByeCommand() {
            this.body = Command.BYE_MATE_NAME;
        }

        @Override
        public String execute(SessionContext context, String input) {
            return "BYE " + context.getClientName() + ", WE SPOKE FOR " +
                    (new Date().getTime() - context.getStartTime()) + " MS";
        }
    }

    class ConnectCommand extends CommandBase {
        ConnectCommand() {
            this.body = Command.CONNECT_NAME;
        }

        @Override
        public String execute(SessionContext context, String input) {
            return "HI, I'M  " + context.getId();
        }
    }

    class ErrorCommand extends CommandBase {
        ErrorCommand() {
            this.body = Command.ERROR_NAME;
        }

        @Override
        public String execute(SessionContext context, String input) {
            return "SORRY, I DIDN'T UNDERSTAND THAT";
        }
    }

    class HiCommand extends CommandBase {
        HiCommand() {
            this.body = Command.HI_NAME;
        }

        @Override
        public String execute(SessionContext context, String input) {
            String[] parameters = parseParameters(input);
            context.setClientName(parameters[0]);

            return "HI " + context.getClientName();
        }
    }

    class AddNodeCommand extends CommandBase {
        AddNodeCommand() {
            this.body = Command.ADD_NODE_NAME;
        }

        @Override
        public String execute(SessionContext context, String input) {
            String[] parameters = parseParameters(input);

            if (context.getModel().addNode(parameters[0]))
                return "NODE ADDED";

            return "ERROR: NODE ALREADY EXISTS";
        }
    }

    class AddEdgeCommand extends CommandBase {
        AddEdgeCommand() {
            this.body = Command.ADD_EDGE_NAME;
        }

        @Override
        public String execute(SessionContext context, String input) {
            String[] parameters = parseParameters(input);
            String node1 = parameters[0];
            String node2 = parameters[1];
            Integer weight = Integer.valueOf(parameters[2]);

            if (context.getModel().addEdge(node1, node2, weight))
                return "EDGE ADDED";
            return "ERROR: NODE NOT FOUND";
        }
    }

    class RemoveNodeCommand extends CommandBase {
        RemoveNodeCommand() {
            this.body = Command.REMOVE_NODE_NAME;
        }

        @Override
        public String execute(SessionContext context, String input) {
            String[] parameters = parseParameters(input);
            String nodeName = parameters[0];

            if (context.getModel().removeNode(nodeName))
                return "NODE REMOVED";

            return "ERROR: NODE NOT FOUND";
        }
    }

    class RemoveEdgeCommand extends CommandBase {
        RemoveEdgeCommand() {
            this.body = Command.REMOVE_EDGE_NAME;
        }

        @Override
        public String execute(SessionContext context, String input) {
            String[] parameters = parseParameters(input);
            String node1 = parameters[0];
            String node2 = parameters[1];

            if (context.getModel().removeEdge(node1, node2))
                return "EDGE REMOVED";
            return "ERROR: NODE NOT FOUND";
        }
    }

    class ShortestPathCommand extends CommandBase {
        ShortestPathCommand() {
            this.body = Command.SHORTEST_PATH_NAME;
        }

        @Override
        public String execute(SessionContext context, String input) {
            String[] parameters = parseParameters(input);
            String node1 = parameters[0];
            String node2 = parameters[1];

            try {
                return String.valueOf(context.getModel().shortestPath(node1, node2));
            } catch (NodeNotFoundException ne) {
                return "ERROR: NODE NOT FOUND";
            }
        }
    }

    class CloserThanCommand extends CommandBase {
        CloserThanCommand() {
            this.body = Command.CLOSER_THAN_NAME;
        }

        @Override
        public String execute(SessionContext context, String input) {
            String[] parameters = parseParameters(input);
            Integer limit = Integer.valueOf(parameters[0]);
            String node = parameters[1];

            try {
                List<String> result = context.getModel().closerThan(node, limit);
                return String.join(",", result);

            } catch (NodeNotFoundException ne) {
                return "ERROR: NODE NOT FOUND";
            }
        }
    }
}
