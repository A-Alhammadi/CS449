
public class SOSGame {
    private int n;  // Board size
    public enum Cell { EMPTY, S, O }

    private Cell[][] grid;
    private char turn;

    public SOSGame(int n) {
        this.n = n;
        grid = new Cell[n][n];
        initGame();
    }

    private void initGame() {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                grid[row][col] = Cell.EMPTY;
            }
        }
        turn = 'S';  // Start with the player placing an 'S'
    }

    public int getBoardSize() {
        return n;
    }

    public Cell getCell(int row, int column) {
        if (row >= 0 && row < n && column >= 0 && column < n) {
            return grid[row][column];
        } else {
            return null;
        }
    }

    public char getTurn() {
        return turn;
    }

    public void makeMove(int row, int column) {
        if (row >= 0 && row < n && column >= 0 && column < n && grid[row][column] == Cell.EMPTY) {
            grid[row][column] = (turn == 'S') ? Cell.S : Cell.O;
            turn = (turn == 'S') ? 'O' : 'S';
        }
    }
}
