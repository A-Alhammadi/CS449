
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

@SuppressWarnings("serial")
public class SosGUI extends JFrame {
	//private JButton startButton;
    private JButton[][] boardButtons;  // Board buttons
    private JButton blueS, blueO, redS, redO;
    private JTextField boardSizeField;
   // private JButton simpleGameButton, generalGameButton; // Buttons to choose game mode
    private SOSGameBase game;  // Game logic
    private JLabel turnLabel, scoreLabel; // Display game information
    private int boardSize = 4;  // Default size
    private JPanel boardPanel;  // Holds board buttons
    private char currentChoice;
    private boolean isAgainstComputer;
  //  private JButton playerButton, computerButton;
   // private char autoPlayer; // Represents the computer player ('S' or 'O')
    private JLabel modeSelectionLabel;
    private JComboBox<String> modeSelectionComboBox;
    private JComboBox<String> computerColorComboBox;


    public SosGUI() {
        super("SOS");  // Main window setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        currentChoice = 'S';

        // Set up top control panel
        JPanel topPanel = new JPanel(new FlowLayout());

        // Combined mode selection components
        modeSelectionLabel = new JLabel("Select Mode:");
        modeSelectionComboBox = new JComboBox<>(new String[]{
            "Simple Player vs Player", 
            "Simple Player vs Computer", 
            "Simple Computer vs Computer",
            "General Player vs Player", 
            "General Player vs Computer", 
            "General Computer vs Computer"
        });
        modeSelectionComboBox.addActionListener(e -> {
            restartGame();
        });
        topPanel.add(modeSelectionLabel);
        topPanel.add(modeSelectionComboBox);

        computerColorComboBox = new JComboBox<>(new String[]{"Computer as Blue", "Computer as Red"});
        computerColorComboBox.addActionListener(e -> {
            restartGame();
        });
        
        topPanel.add(new JLabel("Computer Color:"));
        topPanel.add(computerColorComboBox);
        
        // Board size configuration
        boardSizeField = new JTextField(5);
        boardSizeField.setText(String.valueOf(boardSize));
        JButton applyBoardSizeButton = new JButton("Apply Board Size");
        applyBoardSizeButton.addActionListener(e -> reconstructBoardUI());
        topPanel.add(new JLabel("Board Size:"));
        topPanel.add(boardSizeField);
        topPanel.add(applyBoardSizeButton);

        // Initialize board with buttons
        boardPanel = new JPanel(new GridLayout(boardSize, boardSize));
        boardButtons = new JButton[boardSize][boardSize];
        initBoardButtons();

        // Set up bottom control panel
        JPanel bottomPanel = new JPanel();
        blueS = new JButton("S");
        blueO = new JButton("O");
        redS = new JButton("S");
        redO = new JButton("O");
        blueS.addActionListener(e -> currentChoice = 'S');
        blueO.addActionListener(e -> currentChoice = 'O');
        redS.addActionListener(e -> currentChoice = 'S');
        redO.addActionListener(e -> currentChoice = 'O');
        bottomPanel.add(new JLabel("Blue Player:"));
        bottomPanel.add(blueS);
        bottomPanel.add(blueO);
        bottomPanel.add(new JLabel("Red Player:"));
        bottomPanel.add(redS);
        bottomPanel.add(redO);
        turnLabel = new JLabel("Turn: Blue");
        turnLabel.setForeground(Color.BLUE);
        bottomPanel.add(turnLabel);
        scoreLabel = new JLabel("Blue: 0 | Red: 0");
        bottomPanel.add(scoreLabel);

        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
        updateMode(); // Start with the default selected mode
        }

    // Update game mode based on selection
    private void updateMode() {
        String selectedMode = (String) modeSelectionComboBox.getSelectedItem();
        isAgainstComputer = selectedMode.contains("Computer");

        char computerChar = 'S'; // Default values
        char playerChar = 'O';   // Default values

        if (selectedMode.contains("Player vs Computer")) {
            boolean isComputerBlue = computerColorComboBox.getSelectedItem().equals("Computer as Blue");
            computerChar = isComputerBlue ? 'S' : 'O';
            playerChar = isComputerBlue ? 'O' : 'S';
        } 

        boardSize = parseBoardSize();
        game = selectedMode.startsWith("Simple") ? 
               new SOSSimpleGame(boardSize, isAgainstComputer, computerChar, playerChar, isComputerVsComputer()) :
               new SOSGeneralGame(boardSize, isAgainstComputer, computerChar, playerChar, isComputerVsComputer());
    }

    
    private void restartGame() {
        updateMode();
        reconstructBoardUI();
        startGame();
    }

    // Parse board size with error handling
    private int parseBoardSize() {
        try {
            int size = Integer.parseInt(boardSizeField.getText().trim());
            if (size <= 2) {
                JOptionPane.showMessageDialog(this, "Board size must be greater than 2.");
                return boardSize; // Keep previous size on error
            }
            return size;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid board size format.");
            return boardSize; // Keep previous size on error
        }
    }

    private boolean isComputerVsComputer() {
        String selectedMode = (String) modeSelectionComboBox.getSelectedItem();
        return selectedMode.equals("Simple Computer vs Computer") || selectedMode.equals("General Computer vs Computer");
    }
    // Common method to start a new game
    private void startGame() {
        // Clear the board
        for (JButton[] buttonRow : boardButtons) {
            for (JButton button : buttonRow) {
                button.setText("");
                button.setEnabled(true);
            }
        }

        // Update score and turn labels
        scoreLabel.setText("Blue: 0 | Red: 0");
        turnLabel.setText(game.getTurn() == 'S' ? "Turn: Blue" : "Turn: Red");
        turnLabel.setForeground(game.getTurn() == 'S' ? Color.BLUE : Color.RED);

        if (isComputerVsComputer()) {
            handleComputerVsComputerGame();
        } else if (isAgainstComputer) {
            handleComputerFirstMove();
        }
    }

    private void handleComputerFirstMove() {
        if (isAgainstComputer && !isComputerVsComputer()) {
            game.makeAutoMove();
            SwingUtilities.invokeLater(this::updateBoardAfterComputerMove);
        }
    }

    // Update board UI to reflect the move made by the computer
    private void updateBoardAfterComputerMove() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                SOSGameBase.Cell cell = game.getCell(row, col);
                JButton button = boardButtons[row][col];

                if (cell != SOSGameBase.Cell.EMPTY && !button.getText().equals(cell.toString())) {
                    button.setText(cell == SOSGameBase.Cell.S ? "S" : "O");
                    button.setEnabled(false);
                }
            }
        }
        if (game.isGameOver()) {
            determineWinner();
        } else {
            updateTurnDisplay();
        }
    }
	   // Determine the winner when the game is over
    private void determineWinner() {
        int blueScore = game.getScore('S');
        int redScore = game.getScore('O');
        if (blueScore > redScore) {
           // JOptionPane.showMessageDialog(SosGUI.this, "Blue wins with " + blueScore + " points!");
        	JOptionPane.showMessageDialog(SosGUI.this, "Blue wins");
        } else if (redScore > blueScore) {
            //JOptionPane.showMessageDialog(SosGUI.this, "Red wins with " + redScore + " points!");
        	JOptionPane.showMessageDialog(SosGUI.this, "Red wins");

        } else {
            JOptionPane.showMessageDialog(SosGUI.this, "It's a draw with both players scoring " + blueScore + " points!");
        }
    }
 // Method to handle the computer's move
    private void handleComputerMove() {
        if (isAgainstComputer) {
            game.makeAutoMove();
            SwingUtilities.invokeLater(this::updateBoardAfterComputerMove);
        }
    }

    private void handleComputerVsComputerGame() {
        new Thread(() -> {
            while (!game.isGameOver()) {
                game.makeAutoMove(); // Make an automatic move
                SwingUtilities.invokeLater(() -> {
                    updateBoardAfterComputerMove(); // Update the board UI
                    if (game.isGameOver()) {
                        determineWinner(); // Check if the game is over and determine the winner
                    }
                });
                try {
                    Thread.sleep(500); // Delay between moves for visibility
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }).start();
    }


    private void updateBoardForAutoMove() {
        SwingUtilities.invokeLater(() -> {
            updateBoardAfterComputerMove();
            if (game.isGameOver()) {
                determineWinner();
            }
        });
    }
 // Update turn display
    private void updateTurnDisplay() {
        int blueScore = game.getScore('S');
        int redScore = game.getScore('O');
        scoreLabel.setText("Blue: " + blueScore + " | Red: " + redScore);

        if (game.getTurn() == 'S') {
            SwingUtilities.invokeLater(() -> {
                turnLabel.setText("Turn: Blue");
                turnLabel.setForeground(Color.BLUE);
            });
        } else {
            SwingUtilities.invokeLater(() -> {
                turnLabel.setText("Turn: Red");
                turnLabel.setForeground(Color.RED);
            });
        }
    }
    // Reconstruct board UI
    private void reconstructBoardUI() {
        int newSize = parseBoardSize();
        if (newSize != boardSize) {
            boardSize = newSize;
            rebuildBoardPanel();
            updateMode(); // Start a new game with the updated board size
        }
    }

    // Rebuild board panel
    private void rebuildBoardPanel() {
        remove(boardPanel);
        boardPanel = new JPanel(new GridLayout(boardSize, boardSize));
        boardButtons = new JButton[boardSize][boardSize];
        initBoardButtons();
        add(boardPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Initialize board buttons
    private void initBoardButtons() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                JButton btn = new JButton();
                btn.addActionListener(new ButtonListener(i, j));
                boardPanel.add(btn);
                boardButtons[i][j] = btn;
            }
        }
    }

    private class ButtonListener implements ActionListener {
        private int row, column;

        public ButtonListener(int row, int column) {
            this.row = row;
            this.column = column;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isComputerVsComputer()) {
                return; // Ignore user clicks in computer vs computer mode
            }
            if (game.getCell(row, column) == SOSGameBase.Cell.EMPTY && !game.isGameOver()) {
                boardButtons[row][column].setText(String.valueOf(currentChoice));
                game.makeMove(row, column, currentChoice);
                boardButtons[row][column].setEnabled(false);

                if (isAgainstComputer && !isComputerVsComputer()) {
                    handleComputerMove();
                }
                if (game.isGameOver()) {
                    determineWinner();
                } else {
                    updateTurnDisplay();
                }
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SosGUI::new);
    }
}

