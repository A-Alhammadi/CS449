package SOSgame;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import SOSgame.SOSGameBase.Cell;

public class SOSGeneralGameTest {

    private SOSGeneralGame game;

    @BeforeEach
    public void setup() {
        game = new SOSGeneralGame(3); // Assuming a 3x3 grid for simplicity.
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
    public void testGameDoesNotEndOnSOSFormation() {
        game.makeMove(0, 0, 'S');
        game.makeMove(0, 1, 'O');
        game.makeMove(0, 2, 'S');
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
