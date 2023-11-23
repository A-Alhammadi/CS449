
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class SOSGameBase {
    public enum Cell { EMPTY, S, O }
    public enum GameMode { SIMPLE, GENERAL }

    protected int n; // Board size
    protected Cell[][] grid; // Game board
    protected char turn; // Current player's turn (S or O)
    protected GameMode gameMode; // Game mode
    protected Map<Character, Integer> scores; // Map to store player scores
    protected boolean lastMoveSOS = false;

    public SOSGameBase(int n, GameMode gameMode) {
        this.n = n;
        this.gameMode = gameMode;
        this.grid = new Cell[n][n];
        this.turn = 'S'; // Starting turn
        this.scores = new HashMap<>();
        this.scores.put('S', 0);
        this.scores.put('O', 0);
        initGame();
    }

    protected void initGame() {
        for (Cell[] row : grid) {
            Arrays.fill(row, Cell.EMPTY);
        }
    }

    public char getWinner() {
        int scoreS = scores.get('S');
        int scoreO = scores.get('O');
        return (scoreS > scoreO) ? 'S' : (scoreS < scoreO) ? 'O' : ' ';
    }

    protected boolean checkForSOS(int row, int column, char currentPlayer) {
        int totalSOSFound = 0;
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

        for (int[] dir : directions) {
            if (checkDirection(row, column, dir[0], dir[1])) {
                totalSOSFound++;
            }
        }

        if (totalSOSFound > 0) {
            scores.put(currentPlayer, scores.get(currentPlayer) + totalSOSFound);
            return true;
        }
        return false;
    }

    private boolean checkDirection(int row, int column, int dr, int dc) {
        Cell current = getCell(row, column);

        // Check if the current cell is the first in SOS sequence
        if (current == Cell.S && getCell(row + dr, column + dc) == Cell.O && getCell(row + 2*dr, column + 2*dc) == Cell.S) {
            return true;
        }
        
        // Check if the current cell is the second in SOS sequence
        if (current == Cell.O && getCell(row - dr, column - dc) == Cell.S && getCell(row + dr, column + dc) == Cell.S) {
            return true;
        }

        // Check if the current cell is the third in SOS sequence
        if (current == Cell.S && getCell(row - dr, column - dc) == Cell.O && getCell(row - 2*dr, column - 2*dc) == Cell.S) {
            return true;
        }

        return false;
    }

    public int getBoardSize() {
        return n;
    }

    public Cell getCell(int row, int column) {
        if (row >= 0 && row < n && column >= 0 && column < n) {
            return grid[row][column];
        } else {
            return Cell.EMPTY;
        }
    }

    public boolean wasLastMoveSOS() {
        return lastMoveSOS;
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

    public abstract boolean makeMove(int row, int column, char letter);
    
    public abstract boolean isGameOver();
    
    public abstract void makeAutoMove();
    
    public int getScore(char player) {
        return scores.getOrDefault(player, 0);
    }

	protected abstract boolean isValidMoveForAutoPlayer(char currentMove);

}
