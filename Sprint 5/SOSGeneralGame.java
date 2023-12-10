
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

//import SOSGameBase.GameMove;

public class SOSGeneralGame extends SOSGameBase {
    private final char autoPlayer; // 'S' or 'O'
    private final boolean isAgainstComputer;
    private final Random random = new Random();
    private final boolean isComputerVsComputer;
   // protected List<GameMove> moveList = new ArrayList<>();


    public SOSGeneralGame(int size, boolean isAgainstComputer, char autoPlayerChar, char playerChar, boolean isComputerVsComputer) {
        super(size, GameMode.GENERAL);
        this.isAgainstComputer = isAgainstComputer;
        this.autoPlayer = autoPlayerChar;
        this.isComputerVsComputer = isComputerVsComputer;
        if (isAgainstComputer) {
            this.turn = playerChar;
        }
        initGame();
        if (this.isAgainstComputer && this.turn == this.autoPlayer) {
            makeFirstAutoMove();
        }
    }
    @Override
    public boolean makeMove(int row, int column, char letter) {
        if (isValidMove(row, column)) {
            grid[row][column] = (letter == 'S') ? Cell.S : Cell.O;
            boolean madeSOS = false;
            if (checkForSOS(row, column, turn)) {
                madeSOS = true;
                scores.put(turn, scores.get(turn) + 1); // Update score for the player who made the SOS.
            } 
            lastMoveSOS = madeSOS;
            turn = (turn == 'S') ? 'O' : 'S'; // Switch turns

            // Record the move
            moveList.add(new GameMove(row, column, letter, madeSOS));

            return true;
        }
        return false;
    }


    private boolean isValidMove(int row, int column) {
        return row >= 0 && row < n && column >= 0 && column < n && grid[row][column] == Cell.EMPTY;
    }

    @Override
    public boolean isGameOver() {
        return isBoardFull();
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

    private void makeFirstAutoMove() {
        if (!isGameOver()) {
            makeRandomMove(autoPlayer);
        }
    }
    
    @Override
    public void makeAutoMove(char currentMove) {
        if (!isGameOver() && turn == currentMove) {
            // Randomly choose between 'S' and 'O' for each auto move
            char randomChoice = random.nextBoolean() ? 'S' : 'O';
            makeRandomMove(randomChoice);
        }
    }


    public boolean isValidMoveForAutoPlayer(char player) {
        // Check if there is any empty cell available
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (grid[row][col] == Cell.EMPTY) {
                    return true;
                }
            }
        }
        return false;
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
    
    public void replayMoves() {
        // Clear the board and reset the game state before replaying
        initGame();
        for (GameMove move : moveList) {
            // Apply each move
            grid[move.row][move.column] = (move.player == 'S') ? Cell.S : Cell.O;
            if (move.madeSOS) {
                scores.put(move.player, scores.get(move.player) + 1);
            }

            // Update the turn for each move
            turn = move.player;
        }
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
	@Override
	public List<GameMove> getMoveList() {
	    return moveList;
	}

}