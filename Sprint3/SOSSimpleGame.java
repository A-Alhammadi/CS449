public class SOSSimpleGame extends SOSGameBase {

    private boolean gameOver;
    
    public SOSSimpleGame(int n) {
        super(n, GameMode.SIMPLE);
    }

    @Override
    public boolean makeMove(int row, int column, char letter) {
        if (row >= 0 && row < n && column >= 0 && column < n && grid[row][column] == Cell.EMPTY) {
            grid[row][column] = (letter == 'S') ? Cell.S : Cell.O; // Use the letter provided in the argument.
            if (super.checkForSOS(row, column, letter)) {
                lastMoveSOS = true;
                gameOver = true; // Game ends when an SOS is formed.
                return true; 
            } else {
                lastMoveSOS = false;
                turn = (turn == 'S') ? 'O' : 'S'; // Switch the turn.
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isGameOver() {
        return gameOver || isBoardFull(); // Game ends when an SOS is formed or the board is full.
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
