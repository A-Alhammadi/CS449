import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SOSGameTest {

    private SOSGame game;

    @Before
    public void setUp() {
        game = new SOSGame(4);
    }

    @Test
    public void testInitialBoardSize() {
        Assert.assertEquals(4, game.getBoardSize());
    }

    @Test
    public void testInitialTurn() {
        Assert.assertEquals('S', game.getTurn());
    }

    @Test
    public void testInitialCellState() {
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                Assert.assertEquals(SOSGame.Cell.EMPTY, game.getCell(i, j));
            }
        }
    }

    @Test
    public void testValidMove() {
        game.makeMove(1, 1);
        Assert.assertEquals(SOSGame.Cell.S, game.getCell(1, 1));
        Assert.assertEquals('O', game.getTurn());

        game.makeMove(2, 2);
        Assert.assertEquals(SOSGame.Cell.O, game.getCell(2, 2));
        Assert.assertEquals('S', game.getTurn());
    }

    @Test
    public void testInvalidMoveOutOfBounds() {
        game.makeMove(-1, 1);
        Assert.assertEquals('S', game.getTurn());

        game.makeMove(4, 4);
        Assert.assertEquals('S', game.getTurn());
    }

    @Test
    public void testInvalidMoveOnFilledCell() {
        game.makeMove(1, 1);
        game.makeMove(1, 1);
        Assert.assertEquals(SOSGame.Cell.S, game.getCell(1, 1));
        Assert.assertEquals('O', game.getTurn());
    }

    @Test
    public void testGetCellOutOfBounds() {
        Assert.assertNull(game.getCell(-1, 0));
        Assert.assertNull(game.getCell(0, 4));
        Assert.assertNull(game.getCell(4, 4));
    }
}
