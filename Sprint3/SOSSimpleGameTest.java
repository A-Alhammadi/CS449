package SOSgame;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import SOSgame.SOSGameBase.Cell;

public class SOSSimpleGameTest {

    private SOSSimpleGame game;

    @BeforeEach
    public void setup() {
        game = new SOSSimpleGame(3); // Assuming a 3x3 grid for simplicity.
    }

    @Test
    public void testInitialBoardEmpty() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(Cell.EMPTY, game.getCell(row, col));
            }
        }
    }

    @Test
    public void testMakeMove() {
        assertFalse(game.makeMove(0, 0, 'S'));
        assertEquals(Cell.S, game.getCell(0, 0));
    }

    @Test
    public void testInvalidMoveOutsideBoard() {
        assertFalse(game.makeMove(3, 3, 'S'));
    }

    @Test
    public void testInvalidMoveOnFilledCell() {
        game.makeMove(1, 1, 'S');
        assertFalse(game.makeMove(1, 1, 'O'));
    }

    @Test
    public void testGameEndsOnSOSFormation() {
        game.makeMove(0, 0, 'S');
        game.makeMove(0, 1, 'O');
        assertTrue(game.makeMove(0, 2, 'S'));
        assertTrue(game.isGameOver());
    }

    @Test
    public void testGameContinuesWithoutSOSFormation() {
        game.makeMove(0, 0, 'S');
        game.makeMove(0, 1, 'O');
        assertFalse(game.makeMove(1, 0, 'S'));
        assertFalse(game.isGameOver());
    }

    @Test
    public void testGameEndsWhenBoardIsFull() {
        game.makeMove(0, 0, 'S');
        game.makeMove(0, 1, 'O');
        game.makeMove(0, 2, 'S');
        game.makeMove(1, 0, 'S');
        game.makeMove(1, 1, 'O');
        game.makeMove(1, 2, 'S');
        game.makeMove(2, 0, 'S');
        game.makeMove(2, 1, 'O');
        game.makeMove(2, 2, 'S');
        assertTrue(game.isGameOver());
    }
}
