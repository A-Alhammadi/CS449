public class SOSSimpleGame extends SOSGameBase {

    private boolean gameOver;
    //private char blueChoice;
    //private char redChoice;
    
    public SOSSimpleGame(int n) {
        super(n, GameMode.SIMPLE);
    }

    @Override
    public void makeMove(int row, int column, char letter) {
        if (row >= 0 && row < n && column >= 0 && column < n && grid[row][column] == Cell.EMPTY) {
            grid[row][column] = (turn == 'S') ? Cell.S : Cell.O;
            if (checkForSOS(row, column)) {
                gameOver = true;  // The game ends as soon as an SOS is formed in Simple mode
            } else {
                turn = (turn == 'S') ? 'O' : 'S';
            }
        }
    }

    @Override
    public boolean isGameOver() {
        return gameOver || isBoardFull();
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

    private boolean checkForSOS(int row, int column) {
        if (checkHorizontalSOS(row, column) || checkVerticalSOS(row, column) || checkDiagonalSOS(row, column)) {
            return true;
        }
        return false;
    }

    private boolean checkHorizontalSOS(int row, int column) {
        if (column < n - 2 &&
            grid[row][column] == Cell.S &&
            grid[row][column + 1] == Cell.O &&
            grid[row][column + 2] == Cell.S) {
            return true;
        }
        return false;
    }

    private boolean checkVerticalSOS(int row, int column) {
        if (row < n - 2 &&
            grid[row][column] == Cell.S &&
            grid[row + 1][column] == Cell.O &&
            grid[row + 2][column] == Cell.S) {
            return true;
        }
        return false;
    }

    private boolean checkDiagonalSOS(int row, int column) {
        if (row < n - 2 && column < n - 2 &&
            grid[row][column] == Cell.S &&
            grid[row + 1][column + 1] == Cell.O &&
            grid[row + 2][column + 2] == Cell.S) {
            return true;
        }

        if (row < n - 2 && column >= 2 &&
            grid[row][column] == Cell.S &&
            grid[row + 1][column - 1] == Cell.O &&
            grid[row + 2][column - 2] == Cell.S) {
            return true;
        }

        return false;
    }


}
