
import java.util.HashMap;
import java.util.Map;

public abstract class SOSGameBase {
    public enum Cell { EMPTY, S, O }
    public enum GameMode { SIMPLE, GENERAL }

    protected int n; // Board size
    protected Cell[][] grid; // Game board
    @SuppressWarnings("unused")
	private char blueChoice;
    @SuppressWarnings("unused")
	private char redChoice;
    protected char turn; // Current player's turn (S or O)
    protected GameMode gameMode; // Game mode
    protected Map<Character, Integer> scores; // Map to store player scores

    public SOSGameBase(int n, GameMode gameMode) {
        this.n = n;
        this.gameMode = gameMode;
        blueChoice = 'S'; // Blue player's initial choice
        redChoice = 'O';  // Red player's initial choice
        grid = new Cell[n][n];
        turn = 'S';
        scores = new HashMap<>();
        scores.put('S', 0);
        scores.put('O', 0);
        initGame();
    }

    protected void initGame() {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                grid[row][col] = Cell.EMPTY;
            }
        }
    }

    public int getBoardSize() {
        return n;
    }

    public Cell getCell(int row, int column) {
        if (row >= 0 && row < n && column >= 0 && column < n) {
            return grid[row][column];
        } else {
            return null;
        }
    }
    public void setBlueChoice(char choice) {
        if (choice == 'S' || choice == 'O') {
            blueChoice = choice;
        }
    }
    public void setRedChoice(char choice) {
        if (choice == 'S' || choice == 'O') {
            redChoice = choice;
        }
    }
    public char getTurn() {
        return turn;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
    
    public void setTurn(char newTurn) {
        if (newTurn == 'S' || newTurn == 'O') {
            this.turn = newTurn;
        }
    }
    public abstract void makeMove(int row, int column, char letter);

    public abstract boolean isGameOver();

    public int getScore(char player) {
        return scores.get(player);
    }
}
