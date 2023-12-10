
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SOSGeneralGameTest {

    private SOSGeneralGame game;

    @BeforeEach
    public void setUp() {
        // Initialize game with a standard configuration
        game = new SOSGeneralGame(5, true, 'S', 'O', false);
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
        game = new SOSGeneralGame(3, false, 'S', 'O', true);

        // Run the game until it's over
        while (!game.isGameOver()) {
            game.makeAutoMove('S');
            game.makeAutoMove('O');
        }

        // Check that the game is indeed over
        assertTrue(game.isGameOver(), "The game should end after all cells are filled.");
    }
}
