package SOSgame;
import java.util.Random;

import javax.swing.SwingUtilities;

public class SOSSimpleGame extends SOSGameBase {
    private boolean gameOver;
    private final char autoPlayer; // 'S' or 'O'
    private final boolean isAgainstComputer;
    private final Random random;
    private boolean isComputerVsComputer;

    public SOSSimpleGame(int size, boolean isAgainstComputer, char autoPlayerChar, boolean isComputerVsComputer) {
        super(size, GameMode.SIMPLE);
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
            if (super.checkForSOS(row, column, letter)) {
                lastMoveSOS = true;
                gameOver = true; // Game ends when an SOS is formed.
                if (isAgainstComputer) {
                    scores.put(autoPlayer, scores.get(autoPlayer) + 1); // Update score for the computer if it made the SOS.
                }
                return true;
            } else {
                lastMoveSOS = false;
                if (!isAgainstComputer || !wasLastMoveSOS()) {
                    turn = (turn == 'S') ? 'O' : 'S';
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isGameOver() {
        return gameOver || isBoardFull();
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
