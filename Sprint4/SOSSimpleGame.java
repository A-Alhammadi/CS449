
import java.util.Random;
import javax.swing.SwingUtilities;

public class SOSSimpleGame extends SOSGameBase {
    private boolean gameOver;
    private final char autoPlayer; // 'S' or 'O'
    private final boolean isAgainstComputer;
    private final Random random = new Random();
    private final boolean isComputerVsComputer;

    public SOSSimpleGame(int size, boolean isAgainstComputer, char autoPlayerChar, boolean isComputerVsComputer) {
        super(size, GameMode.SIMPLE);
        this.isAgainstComputer = isAgainstComputer;
        this.autoPlayer = autoPlayerChar;
        this.isComputerVsComputer = isComputerVsComputer;

        initGame();
        if (this.isAgainstComputer && this.turn == this.autoPlayer) {
            makeFirstAutoMove();
        }
    }

    @Override
    public boolean makeMove(int row, int column, char letter) {
        if (isValidMove(row, column)) {
            grid[row][column] = (letter == 'S') ? Cell.S : Cell.O;
            if (checkForSOS(row, column, turn)) {
                lastMoveSOS = true;
                gameOver = true;
                scores.put(turn, scores.get(turn) + 1); // Update score
                return true;
            } else {
                lastMoveSOS = false;
                turn = (turn == 'S') ? 'O' : 'S'; // Switch turns
                return false;
            }
        }
        return false;
    }

    private boolean isValidMove(int row, int column) {
        return row >= 0 && row < n && column >= 0 && column < n && grid[row][column] == Cell.EMPTY;
    }

    @Override
    public boolean isGameOver() {
        return gameOver || isBoardFull();
    }

    private void makeFirstAutoMove() {
        if (!isGameOver()) {
            makeRandomMove(autoPlayer);
        }
    }

    @Override
    public void makeAutoMove() {
        if (!isGameOver() && turn == autoPlayer) {
            makeRandomMove(autoPlayer);
        }
    }

    private void makeRandomMove(char player) {
        int emptyCells = getNumberOfEmptyCells();
        if (emptyCells > 0) {
            int moveIndex = random.nextInt(emptyCells);
            applyMoveAtIndex(moveIndex, player);
        }
    }

    private int getNumberOfEmptyCells() {
        int count = 0;
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell == Cell.EMPTY) {
                    count++;
                }
            }
        }
        return count;
    }

    private void applyMoveAtIndex(int moveIndex, char player) {
        int index = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (grid[row][col] == Cell.EMPTY) {
                    if (index == moveIndex) {
                        makeMove(row, col, player);
                        return;
                    }
                    index++;
                }
            }
        }
    }

    private boolean isBoardFull() {
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell == Cell.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
}

