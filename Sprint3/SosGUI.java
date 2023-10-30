package SOSgame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class SosGUI extends JFrame {

    private JButton[][] boardButtons;  // Board buttons
    private JButton blueS, blueO, redS, redO;
    private JTextField boardSizeField;
    private JButton simpleGameButton, generalGameButton; // Buttons to choose game mode
    private SOSGameBase game;  // Game logic
    private JLabel turnLabel, gameModeLabel, scoreLabel; // Display game information
    private int boardSize = 4;  // Default size
    private JPanel boardPanel;  // Holds board buttons
    private char currentChoice;

 // Constructor: Set up GUI components
    public SosGUI() {
    	 super("SOS");  // Main window setup
    	 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setSize(600, 600);
         currentChoice = 'S';

        // Set up top control panel
        JPanel topPanel = new JPanel();
        simpleGameButton = new JButton("Simple Game");
        generalGameButton = new JButton("General Game");
        boardSizeField = new JTextField(5);
        boardSizeField.setText(String.valueOf(boardSize));
        JButton applyBoardSizeButton = new JButton("Apply Board Size");
        applyBoardSizeButton.addActionListener(e -> reconstructBoardUI());

        topPanel.add(new JLabel("SOS"));  // Adding components to top panel
        topPanel.add(simpleGameButton);
        topPanel.add(generalGameButton);
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
        bottomPanel.add(new JLabel("Blue Player:"));  // Adding components to bottom panel
        bottomPanel.add(blueS);
        bottomPanel.add(blueO);
        bottomPanel.add(new JLabel("Red Player:"));
        bottomPanel.add(redS);
        bottomPanel.add(redO);
        turnLabel = new JLabel("Turn: Blue");
        turnLabel.setForeground(Color.BLUE);
        bottomPanel.add(turnLabel);
        gameModeLabel = new JLabel("Game Mode: Simple"); // Initialize gameModeLabel
        scoreLabel = new JLabel("Blue: 0 | Red: 0"); // Display player scores

        bottomPanel.add(gameModeLabel);
        bottomPanel.add(scoreLabel);

        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Add listeners for game mode buttons
        simpleGameButton.addActionListener(e -> createSimpleGame());
        generalGameButton.addActionListener(e -> createGeneralGame());
        blueS.addActionListener(e -> currentChoice = 'S');
        blueO.addActionListener(e -> currentChoice = 'O');
        redS.addActionListener(e -> currentChoice = 'S');
        redO.addActionListener(e -> currentChoice = 'O');
        createSimpleGame();  // Start with simple game by default

        setVisible(true);
    }


    // Create a new simple game instance
    private void createSimpleGame() {
        game = new SOSSimpleGame(boardSize);
        gameModeLabel.setText("Game Mode: Simple");
        startGame();
    }

    // Create a new general game instance
    private void createGeneralGame() {
        game = new SOSGeneralGame(boardSize);
        gameModeLabel.setText("Game Mode: General");
        startGame();
    }

    // Common method to start a new game
    private void startGame() {
        boardSize = game.getBoardSize();
        gameModeLabel.setText("Game Mode: " + (game.getGameMode() == SOSGameBase.GameMode.SIMPLE ? "Simple" : "General"));
        reconstructBoardUI();
    }
    
    private void setTurn(char letter) {
        if (game != null) {
            game.setTurn(letter);
            toggleTurn();
        }
    }
    
 // Update turn display
    private void toggleTurn() {
        int blueScore = game.getScore('S');
        int redScore = game.getScore('O');
        scoreLabel.setText("Blue: " + blueScore + " | Red: " + redScore); // Update the score label

        if (game.getTurn() == 'S') {
            SwingUtilities.invokeLater(() -> {
                turnLabel.setText("Turn: Red");
                turnLabel.setForeground(Color.RED);
            });
        } else {
            SwingUtilities.invokeLater(() -> {
                turnLabel.setText("Turn: Blue");
                turnLabel.setForeground(Color.BLUE);
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
        boardSize = newSize;
        game = game.getGameMode() == SOSGameBase.GameMode.SIMPLE
                ? new SOSSimpleGame(boardSize)
                : new SOSGeneralGame(boardSize);

        // Use SwingUtilities.invokeLater to update the GUI components
        SwingUtilities.invokeLater(() -> {
            remove(boardPanel);  // Remove old board
            boardPanel = new JPanel(new GridLayout(boardSize, boardSize));  // Create a new board
            boardButtons = new JButton[boardSize][boardSize];
            initBoardButtons();
            add(boardPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
            toggleTurn();
        });
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

        // Handle button clicks
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.getCell(row, column) == SOSGameBase.Cell.EMPTY && !game.isGameOver()) {
                boardButtons[row][column].setText(String.valueOf(currentChoice));
                game.makeMove(row, column, currentChoice);
                boardButtons[row][column].setEnabled(false);

                if (game.isGameOver()) {
                    determineWinner();
                } else if (game.getGameMode() == SOSGameBase.GameMode.SIMPLE && game.wasLastMoveSOS()) {
                    determineWinner();
                } else {
                    toggleTurn();
                }
            }
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


   

 // Main method: Launch the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SosGUI();
        });
    }
    
}
    




