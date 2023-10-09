import java.util.Scanner;

public class SOSConsole {

    private SOSGame game;  // Game instance

    // Constructor: Initialize with game instance
    public SOSConsole(SOSGame game) {
        this.game = game;
    }

    // Display the game board on console
    public void displayBoard() {
        // Print each cell in the board
        for (int row = 0; row < game.getBoardSize(); row++) {
            for (int col = 0; col < game.getBoardSize(); col++) {
                System.out.print("|" + symbol(game.getCell(row, col)));
            }
            System.out.println("|");
        }
        // Print board bottom border
        for (int i = 0; i < game.getBoardSize(); i++) {
            System.out.print("----");
        }
    }

    // Convert cell value to display symbol
    private char symbol(SOSGame.Cell cell) {
        if (cell == SOSGame.Cell.S) return 'S';
        else if (cell == SOSGame.Cell.O) return 'O';
        else return ' ';  // Empty cell
    }

    // Main game loop for console-based gameplay
    public void play() {
        Scanner in = new Scanner(System.in);
        while (true) {  // Infinite loop for continuous play
            displayBoard();  // Show current board
            int row, column;
            // Indicate current player's turn
            System.out.println("\nCurrent player: " + game.getTurn());
            // Get player's move coordinates
            System.out.print("Move at row: ");
            row = in.nextInt();
            System.out.print("Move at column: ");
            column = in.nextInt();
            // Check if move is valid
            if (row < 0 || row >= game.getBoardSize() || column < 0 || column >= game.getBoardSize())
                System.out.println("Invalid move at (" + row + "," + column + ")");
            else {
                game.makeMove(row, column);  // Apply move on the board
            }
        }
    }

    // Main method to start the console game
    public static void main(String[] args) {
        SOSConsole console = new SOSConsole(new SOSGame(4));  // Create game for a 4x4 board
        console.play();  // Start the game
    }
}
