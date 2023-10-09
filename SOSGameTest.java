import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SOSGameTest {

    private SOSGame game;

    @Before
    public void setUp() {
        game = new SOSGame(4);  // Choose a board size
    }

    // Choose a board size
    @Test
    public void testChooseBoardSize() {
        Assert.assertEquals(4, game.getBoardSize());
    }

    // Start a new game
    @Test
    public void testStartNewGame() {
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                Assert.assertEquals(SOSGame.Cell.EMPTY, game.getCell(i, j));
            }
        }
        Assert.assertEquals('S', game.getTurn());
    }

    // Make a move in a simple game
    @Test
    public void testMakeMoveInSimpleGame() {
        game.makeMove(0, 0);
        Assert.assertEquals(SOSGame.Cell.S, game.getCell(0, 0));
        Assert.assertEquals('O', game.getTurn());
        
        game.makeMove(0, 1);
        Assert.assertEquals(SOSGame.Cell.O, game.getCell(0, 1));
        Assert.assertEquals('S', game.getTurn());
    }

    // Invalid moves should not change the turn or affect the board
    @Test
    public void testInvalidMove() {
        game.makeMove(4, 4);  // out of bounds
        Assert.assertEquals('S', game.getTurn());
        Assert.assertNull(game.getCell(4, 4));
        
        game.makeMove(-1, 0);  // out of bounds
        Assert.assertEquals('S', game.getTurn());
        Assert.assertNull(game.getCell(-1, 0));

        game.makeMove(0, 0);
        game.makeMove(0, 0);  // cell already occupied
        Assert.assertEquals('O', game.getTurn());
        Assert.assertEquals(SOSGame.Cell.S, game.getCell(0, 0));
    }

   
}
