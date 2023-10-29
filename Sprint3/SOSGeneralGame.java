
public class SOSGeneralGame extends SOSGameBase {

    public SOSGeneralGame(int n) {
        super(n, GameMode.GENERAL);
    }

    @Override
    public void makeMove(int row, int column, char letter) {
        if (row >= 0 && row < n && column >= 0 && column < n && grid[row][column] == Cell.EMPTY) {
            grid[row][column] = (letter == 'S') ? Cell.S : Cell.O;
            if (!checkForSOS(row, column) || getGameMode() == GameMode.SIMPLE) {
                turn = (turn == 'S') ? 'O' : 'S';
            }
        }
    }


    @Override
    public boolean isGameOver() {
        return isBoardFull();
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
    private boolean checkDirection(int row, int column, int rowInc, int colInc) {
        // Check one direction from the cell for an SOS. 
        // If an SOS is found, return true.
        
        try {
            if (grid[row][column] == Cell.S
                && grid[row + rowInc][column + colInc] == Cell.O
                && grid[row + 2*rowInc][column + 2*colInc] == Cell.S) {
                return true;
            }
            if (grid[row][column] == Cell.S
                && grid[row - rowInc][column - colInc] == Cell.O
                && grid[row - 2*rowInc][column - 2*colInc] == Cell.S) {
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // This will catch any index errors (like trying to check outside the grid boundaries).
            return false;
        }
        return false;
    }

    private boolean checkForSOS(int row, int column) {
        // Check horizontally, vertically, and diagonally.
        return checkDirection(row, column, 0, 1)  // Horizontal
            || checkDirection(row, column, 1, 0)  // Vertical
            || checkDirection(row, column, 1, 1)  // Diagonal from top-left to bottom-right
            || checkDirection(row, column, 1, -1); // Diagonal from top-right to bottom-left
    }
}
