package dev.backend.interview.server.command;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CommandControllerTest {

    @Test
    public void testCommands() {
        final CommandController controller = new CommandController();
        assertTrue(controller.getCommand(CommandBase.HI_NAME) instanceof HiCommand);
        assertTrue(controller.getCommand(CommandBase.BYE_MATE_NAME) instanceof ByeCommand);
        assertTrue(controller.getCommand(CommandBase.CONNECT_NAME) instanceof ConnectCommand);
        assertTrue(controller.getCommand(CommandBase.ADD_EDGE_NAME) instanceof AddEdgeCommand);
        assertTrue(controller.getCommand(CommandBase.ADD_NODE_NAME) instanceof AddNodeCommand);
        assertTrue(controller.getCommand(CommandBase.REMOVE_EDGE_NAME) instanceof RemoveEdgeCommand);
        assertTrue(controller.getCommand(CommandBase.REMOVE_NODE_NAME) instanceof RemoveNodeCommand);
        assertTrue(controller.getCommand(CommandBase.SHORTEST_PATH_NAME) instanceof ShortestPathCommand);
        assertTrue(controller.getCommand(CommandBase.CLOSER_THAN_NAME) instanceof CloserThanCommand);
        assertTrue(controller.getCommand("UNKNOWN") instanceof ErrorCommand);
    }
}