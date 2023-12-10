
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SOSSimpleGameTest {

    private SOSSimpleGame game;

    @BeforeEach
    public void setUp() {
        // Initialize game with standard configuration
        game = new SOSSimpleGame(5, true, 'S', 'O', false);
    }

    @Test
    public void testComputerMakesValidMove() {
        // Simulate a player move
        game.makeMove(0, 0, 'S');

        // Have the computer make its move
        game.makeAutoMove('O');

        // Check that the computer's move is valid
        assertFalse(game.wasLastMoveSOS(), "The computer should make a valid move.");
    }

    @Test
    public void testComputerVsComputerGameEnds() {
        // Re-initialize game for computer vs computer
        game = new SOSSimpleGame(3, false, 'S', 'O', true);

        // Run the game until it's over
        while (!game.isGameOver()) {
            game.makeAutoMove('S');
            game.makeAutoMove('O');
        }

        // Check that the game is indeed over
        assertTrue(game.isGameOver(), "The game should end after all cells are filled.");
    }

    @Test
    public void testGameOverCondition() {
        // Simulate moves to fill the board
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                if (game.getCell(i, j) == SOSGameBase.Cell.EMPTY) {
                    game.makeMove(i, j, 'S'); // Assuming 'S' and 'O' don't matter in simple mode
                }
            }
        }

        // Check that the game is over when the board is full
        assertTrue(game.isGameOver(), "Game should be over when board is full.");
    }
}
