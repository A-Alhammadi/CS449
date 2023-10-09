
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SOSConsoleTest {

    private SOSGame game;
    private SOSConsole console;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        game = new SOSGame(4);
        console = new SOSConsole(game);
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    // Choose a board size
    @Test
    public void testDisplayBoardInitial() {
        console.displayBoard();
        String expectedOutput = "| | | | |\n| | | | |\n| | | | |\n| | | | |\n---- ---- ---- ---- ";  // This is the expected display of an empty 4x4 board.
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    // Start a new game
    @Test
    public void testStartNewGame() {
        game.makeMove(0, 0);
        game.makeMove(1, 1);
        console.displayBoard();
        String expectedOutput = "|S| | | |\n| |O| | |\n| | | | |\n| | | | |\n---- ---- ---- ---- ";  // After two moves.
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    // Make a move in a simple game (indirectly testing this via the board display)
    @Test
    public void testMakeMoveInSimpleGame() {
        game.makeMove(2, 2);
        game.makeMove(3, 3);
        console.displayBoard();
        String expectedOutput = "| | | | |\n| | | | |\n| | |S| |\n| | | |O|\n---- ---- ---- ---- ";  // After two moves in the middle and bottom right.
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    // ... additional tests ...

}
