package SOSgame;
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
    private char autoPlayer; // Represents the computer player ('S' or 'O')
    private JLabel modeSelectionLabel;
    private JComboBox<String> modeSelectionComboBox;

 // Constructor: Set up GUI components
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
            updateMode();
        });
        topPanel.add(modeSelectionLabel);
        topPanel.add(modeSelectionComboBox);


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

    private void updateMode() {
        String selectedMode = (String) modeSelectionComboBox.getSelectedItem();
        isAgainstComputer = selectedMode.contains("Computer");
        char computerChar = determineComputerChar();

        if (selectedMode.startsWith("Simple")) {
            createSimpleGame(isAgainstComputer, computerChar);
        } else {
            createGeneralGame(isAgainstComputer, computerChar);
        }
    }
 // Create a new simple game instance
    private void createSimpleGame(boolean isAgainstComputer, char computerChar) {
        boardSize = Integer.parseInt(boardSizeField.getText().trim());
        boolean isCompVsComp = isComputerVsComputer();
        game = new SOSSimpleGame(boardSize, isAgainstComputer, computerChar, isCompVsComp);
        startGame();
    }

    // Create a new general game instance
    private void createGeneralGame(boolean isAgainstComputer, char computerChar) {
        boardSize = Integer.parseInt(boardSizeField.getText().trim());
        boolean isCompVsComp = isComputerVsComputer();
        game = new SOSGeneralGame(boardSize, isAgainstComputer, computerChar, isCompVsComp);
        startGame();
    }

    private boolean isComputerVsComputer() {
        String selectedMode = (String) modeSelectionComboBox.getSelectedItem();
        return selectedMode.equals("Simple Computer vs Computer") || selectedMode.equals("General Computer vs Computer");
    }
 // Method to determine the character for the computer
    private char determineComputerChar() {
        // If the player's choice is available, use the opposite for the computer
        // If not, randomly select 'S' or 'O' for the computer
        Random random = new Random();
        return random.nextBoolean() ? 'S' : 'O';
    }
    // Common method to start a new game
    private void startGame() {
        // Update isAgainstComputer flag based on the current selection
        String selectedMode = (String) modeSelectionComboBox.getSelectedItem();
        isAgainstComputer = selectedMode.contains("Computer");

        // Set board size and update game mode label
        boardSize = Integer.parseInt(boardSizeField.getText().trim());

        // Reconstruct the UI with the new board size
        reconstructBoardUI();

        // If the game is against the computer, handle the computer's first move
        if (isAgainstComputer) {
            handleComputerFirstMove();
        }
    }
   
    private void restartGame() {
        updateMode();
    }
    
    private void handleComputerFirstMove() {
        if (isAgainstComputer) {
            game.makeAutoMove();
            SwingUtilities.invokeLater(() -> {
                updateBoardAfterComputerMove();
                if (isComputerVsComputer() && !game.isGameOver()) {
                    handleComputerMove();
                }
            });
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
            // In computer vs computer mode, handle moves on a separate thread
            if (isComputerVsComputer()) {
                new Thread(() -> {
                    while (!game.isGameOver()) {
                        game.makeAutoMove();
                        
                        // Update the board UI after each move on the EDT
                        SwingUtilities.invokeLater(this::updateBoardAfterComputerMove);

                        // Pause briefly between moves for better visibility
                        try {
                            Thread.sleep(500); // adjust the delay as needed
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    // Handle game over on the EDT
                    SwingUtilities.invokeLater(this::determineWinner);
                }).start();
            } else {
                // For single computer player, make a move and update the UI
                game.makeAutoMove();
                SwingUtilities.invokeLater(this::updateBoardAfterComputerMove);
            }
        }
    }



	private void setTurn(char letter) {
        if (game != null) {
            game.setTurn(letter);
            updateTurnDisplay();
        }
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
 // Update board UI when board size changes
    public void reconstructBoardUI() {
        int newSize;
        try {
            newSize = Integer.parseInt(boardSizeField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid board size.");
            return;
        }
        if (newSize <= 2) {
            JOptionPane.showMessageDialog(this, "Please enter a board size greater than 2.");
            return;
        }

        if (newSize != boardSize) {
            boardSize = newSize;
            updateGame();
        }

        SwingUtilities.invokeLater(this::rebuildBoardPanel);
    }
    private void updateGame() {
        String selectedMode = (String) modeSelectionComboBox.getSelectedItem();
        isAgainstComputer = selectedMode.contains("Computer");
        char computerChar = determineComputerChar();

        if (selectedMode.startsWith("Simple")) {
            createSimpleGame(isAgainstComputer, computerChar);
        } else {
            createGeneralGame(isAgainstComputer, computerChar);
        }
    }

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
                JButton btn = new JButton("");
                btn.addActionListener(new ButtonListener(i, j));  // Button listener for game moves
                boardPanel.add(btn);
                boardButtons[i][j] = btn;
            }
        }
    }
 // Listener for board buttons
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

                if (isAgainstComputer) {
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


 // Main method: Launch the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SosGUI();
        });
    }
    }

