package SOSgame;

import java.util.Random;

public class AutoSOSGame extends SOSGameBase {
    private final char autoPlayer; // 'S' or 'O'
    private final Random random;

    public AutoSOSGame(int size, GameMode gameMode, char autoPlayer) {
        super(size, gameMode);
        this.autoPlayer = autoPlayer;
        this.random = new Random();

        // Initialize the game state
        initGame();

        // If it's the computer's turn, make the first move.
        if (this.turn == this.autoPlayer) {
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
                if (gameMode == GameMode.SIMPLE) {
                    return true; // In SIMPLE mode, game ends after first SOS.
                }
            } else {
                lastMoveSOS = false;
                turn = (turn == 'S') ? 'O' : 'S'; // Switch the turn.
            }
            return !isGameOver(); // Return false if game is over, true otherwise.
        }
        return false;
    }

    private boolean isValidMove(int row, int column, char letter) {
        return row >= 0 && row < n && column >= 0 && column < n && 
               grid[row][column] == Cell.EMPTY && (letter == 'S' || letter == 'O');
    }

    private void makeFirstAutoMove() {
        makeRandomMove(autoPlayer);
        // After making the move, switch the turn if needed.
        if (!wasLastMoveSOS() || gameMode == GameMode.SIMPLE) {
            setTurn(autoPlayer == 'S' ? 'O' : 'S');
        }
    }

    @Override
    public boolean isGameOver() {
        if (gameMode == GameMode.SIMPLE) {
            return lastMoveSOS || isBoardFull(); // Game ends when an SOS is formed or the board is full.
        } else { // For GENERAL mode
            return isBoardFull(); // Game ends only when the board is full.
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
    
    public void makeAutoMove() {
        if (!isGameOver() && turn == autoPlayer) {
            makeRandomMove(autoPlayer);
            // Check if the game should continue or switch turns
            if (!wasLastMoveSOS() || gameMode == GameMode.SIMPLE) {
                setTurn(autoPlayer == 'S' ? 'O' : 'S');
            }
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
}
