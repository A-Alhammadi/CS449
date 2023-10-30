package SOSgame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SOSGeneralGameTest {

    private SOSGeneralGame game;

    @BeforeEach
    public void setUp() {
        game = new SOSGeneralGame(3); // Let's create a 3x3 board for simplicity.
    }

    @Test
    public void testInitialGameState() {
        assertFalse(game.isGameOver());
        assertEquals('S', game.getTurn());
        assertFalse(game.wasLastMoveSOS());
    }

    @Test
    public void testValidMove() {
        assertTrue(game.makeMove(0, 0, 'S'));
        assertEquals('O', game.getTurn()); // Turn should change after a valid move.
        assertEquals(SOSGameBase.Cell.S, game.getCell(0, 0));
    }

    @Test
    public void testInvalidMove() {
        game.makeMove(0, 0, 'S');
        assertFalse(game.makeMove(0, 0, 'O')); // Attempting to place in an occupied cell should return false.
    }

    @Test
    public void testFormingSOS() {
        game.makeMove(0, 0, 'S');
        game.makeMove(0, 1, 'O');
        assertTrue(game.makeMove(0, 2, 'S'));
        assertTrue(game.wasLastMoveSOS()); // An SOS was formed, so this should be true.
    }

    @Test
    public void testNotFormingSOS() {
        game.makeMove(0, 0, 'S');
        game.makeMove(0, 1, 'S');
        game.makeMove(0, 2, 'S');
        assertFalse(game.wasLastMoveSOS()); // No SOS formed, should be false.
    }

    @Test
    public void testGameIsOver() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                game.makeMove(i, j, (i+j) % 2 == 0 ? 'S' : 'O'); // Fill up the board.
            }
        }
        assertTrue(game.isGameOver()); // The board is full, so the game should be over.
    }
}
