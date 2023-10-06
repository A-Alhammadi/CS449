
import java.util.Scanner;

public class SOSConsole {
    private SOSGame game;

    public SOSConsole(SOSGame game) {
        this.game = game;
    }

    public void displayBoard() {
        for (int row = 0; row < game.getBoardSize(); row++) {
            for (int col = 0; col < game.getBoardSize(); col++) {
                System.out.print("|" + symbol(game.getCell(row, col)));
            }
            System.out.println("|");
        }
        for (int i = 0; i < game.getBoardSize(); i++) {
            System.out.print("----");
        }
    }

    private char symbol(SOSGame.Cell cell) {
        if (cell == SOSGame.Cell.S) return 'S';
        else if (cell == SOSGame.Cell.O) return 'O';
        else return ' ';
    }

    public void play() {
        Scanner in = new Scanner(System.in);
        while (true) {
            displayBoard();
            int row, column;
            System.out.println("\nCurrent player: " + game.getTurn());
            System.out.print("Move at row: ");
            row = in.nextInt();
            System.out.print("Move at column: ");
            column = in.nextInt();
            if (row < 0 || row >= game.getBoardSize() || column < 0 || column >= game.getBoardSize())
                System.out.println("Invalid move at (" + row + "," + column + ")");
            else {
                game.makeMove(row, column);
            }
        }
    }

    public static void main(String[] args) {
        SOSConsole console = new SOSConsole(new SOSGame(4));  // for a 4x4 board
        console.play();
    }
}
