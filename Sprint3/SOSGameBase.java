package SOSgame;

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
    protected boolean lastMoveSOS = false;

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
    
    protected boolean checkForSOS(int row, int column, char letter) {
        int totalSOSFound = 0;

        // Check each direction (Horizontal, Vertical, 2 Diagonals)
        for (int[] direction : new int[][] {{0, 1}, {1, 0}, {1, 1}, {1, -1}}) {
            if (checkDirection(row, column, direction[0], direction[1], letter)) {
                totalSOSFound++;
            }
        }

        if (totalSOSFound > 0) {
            scores.put(letter, scores.get(letter) + totalSOSFound);
            return true;
        }

        return false;
    }

    private boolean checkDirection(int row, int column, int dr, int dc, char letter) {
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
            return Cell.EMPTY; // return EMPTY instead of null for out-of-bounds
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

    public int getScore(char player) {
        return scores.get(player);
    }
}
