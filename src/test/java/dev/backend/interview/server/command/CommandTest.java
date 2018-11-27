package dev.backend.interview.server.command;

import dev.backend.interview.server.SessionContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommandTest {

    private SessionContext sessionContext;
    private String clientName = UUID.randomUUID().toString();
    private long sessionStart = new Date().getTime();

    @Before
    public void init() {
        this.sessionContext = new SessionContext();
        this.sessionContext.setStartTime(sessionStart);
    }

    @Test
    public void executeSimple() throws CommandParseException {
        final HiCommand hiCommand = new HiCommand();
        String input = CommandBase.HI_NAME + clientName;
        String result = hiCommand.execute(input, sessionContext);
        assertEquals(sessionContext.getClientName(), clientName);

        assertEquals(result, "HI " + clientName);
        final ByeCommand byeCommand = new ByeCommand();
        input = CommandBase.BYE_MATE_NAME;
        result = byeCommand.execute(input, sessionContext);
        assertTrue(result.startsWith(String.format("BYE %s, WE SPOKE FOR ", sessionContext.getClientName())));
    }

    @Test(expected = CommandParseException.class)
    public void parseNameError() throws CommandParseException {
        final HiCommand hiCommand = new HiCommand();
        String input = CommandBase.HI_NAME + "not valid name";
        hiCommand.execute(input, sessionContext);
    }

    @Test(expected = CommandParseException.class)
    public void parseNodeNameError() throws CommandParseException {
        final AddNodeCommand addNodeCommand = new AddNodeCommand();
        String input = CommandBase.ADD_NODE_NAME + "not valid name";
        addNodeCommand.execute(input, sessionContext);
    }

    @Test(expected = CommandParseException.class)
    public void parseAddEdgeNodeNameError() throws CommandParseException {
        final AddEdgeCommand addEdgeCommand = new AddEdgeCommand();
        String input = CommandBase.ADD_EDGE_NAME + "not valid name1 name2";
        addEdgeCommand.execute(input, sessionContext);
    }

    @Test(expected = CommandParseException.class)
    public void parseAddEdgeWeightFormatError() throws CommandParseException {
        final AddEdgeCommand addEdgeCommand = new AddEdgeCommand();
        String input = CommandBase.ADD_EDGE_NAME + "not valid name";
        addEdgeCommand.execute(input, sessionContext);
    }

    @Test(expected = CommandParseException.class)
    public void parseCloserThanError() throws CommandParseException {
        final CloserThanCommand closerThanCommand = new CloserThanCommand();
        String input = CommandBase.CLOSER_THAN_NAME + "not valid";
        closerThanCommand.execute(input, sessionContext);
    }

    @Test(expected = CommandParseException.class)
    public void shortesPathParseError() throws CommandParseException {
        final ShortestPathCommand shortestPathCommand = new ShortestPathCommand();
        String input = CommandBase.SHORTEST_PATH_NAME + "not valid1 not valid2";
        shortestPathCommand.execute(input, sessionContext);
    }
}