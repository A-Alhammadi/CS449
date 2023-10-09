
public class SOSGame {

    private int n;  // Board size
    public enum Cell { EMPTY, S, O }  // Possible cell values

    private Cell[][] grid;  // Game board
    private char turn;  // Current player's turn (S or O)
    // Constructor: Initializes a new game with given board size
    public SOSGame(int n) {
        this.n = n;
        grid = new Cell[n][n];  // Create board grid
        initGame();  // Initialize game
    }
    // Initialize game board and set starting turn
    private void initGame() {
        // Set all cells to EMPTY
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                grid[row][col] = Cell.EMPTY;
            }
        }
        turn = 'S';  // Start with 'S' player
    }
    // Get board size
    public int getBoardSize() {
        return n;
    }
    // Get cell value at specified row and column
    public Cell getCell(int row, int column) {
        if (row >= 0 && row < n && column >= 0 && column < n) {
            return grid[row][column];
        } else {
            return null;  // Return null for invalid coordinates
        }
    }
    // Get current player's turn
    public char getTurn() {
        return turn;
    }
    // Make a move for the current player at specified row and column
    public void makeMove(int row, int column) {
        if (row >= 0 && row < n && column >= 0 && column < n && grid[row][column] == Cell.EMPTY) {
            grid[row][column] = (turn == 'S') ? Cell.S : Cell.O;  // Set cell value based on turn
            turn = (turn == 'S') ? 'O' : 'S';  // Switch turn
        }
    }
}

