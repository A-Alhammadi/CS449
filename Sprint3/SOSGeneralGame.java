package SOSgame;
import java.util.Random;

import javax.swing.SwingUtilities;

public class SOSGeneralGame extends SOSGameBase {
    private final char autoPlayer; // 'S' or 'O'
    private final boolean isAgainstComputer;
    private final Random random;
    private boolean isComputerVsComputer;
    
    public SOSGeneralGame(int size, boolean isAgainstComputer, char autoPlayerChar, boolean isComputerVsComputer) {
        super(size, GameMode.GENERAL);
        this.isAgainstComputer = isAgainstComputer;
        this.autoPlayer = autoPlayerChar;
        this.isComputerVsComputer = isComputerVsComputer; // Set this based on the game mode
        this.random = new Random();
        initGame();
        if (this.isAgainstComputer && this.turn == this.autoPlayer) {
            makeFirstAutoMove();
        }
    }


    @Override
    public boolean makeMove(int row, int column, char letter) {
        if (row >= 0 && row < n && column >= 0 && column < n && grid[row][column] == Cell.EMPTY) {
            grid[row][column] = (letter == 'S') ? Cell.S : Cell.O;
            if (super.checkForSOS(row, column, turn)) {
                lastMoveSOS = true;
                scores.put(turn, scores.get(turn) + 1); // Update score for the player who made the SOS.
            } else {
                lastMoveSOS = false;
                // Switch the turn if not playing against computer or if last move didn't result in an SOS
                if (!isAgainstComputer || !wasLastMoveSOS()) {
                    turn = (turn == 'S') ? 'O' : 'S';
                }
            }
            return !isGameOver(); // Return false if game is over, true otherwise.
        }
        return false;
    }

    @Override
    public boolean isGameOver() {
        return isBoardFull(); // Game ends only when the board is full.
    }

    private void makeFirstAutoMove() {
        makeRandomMove(autoPlayer);
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
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (grid[row][col] == Cell.EMPTY) {
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
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (grid[row][col] == Cell.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
}
