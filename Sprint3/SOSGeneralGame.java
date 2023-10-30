package SOSgame;
public class SOSGeneralGame extends SOSGameBase {

    public SOSGeneralGame(int n) {
        super(n, GameMode.GENERAL);
    }

    @Override
    public boolean makeMove(int row, int column, char letter) {
        if (row >= 0 && row < n && column >= 0 && column < n && grid[row][column] == Cell.EMPTY) {
            grid[row][column] = (letter == 'S') ? Cell.S : Cell.O;
            if (super.checkForSOS(row, column, letter)) {
                lastMoveSOS = true;
                scores.put(turn, scores.get(turn) + 1);
                return true;
            } else {
                lastMoveSOS = false;
                turn = (turn == 'S') ? 'O' : 'S';
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isGameOver() {
        return isBoardFull(); // Game ends only when the board is full.
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
