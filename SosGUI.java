import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SosGUI {

    private JFrame frame;  // Main window
    private JButton[][] boardButtons;  // Board buttons
    // Control buttons and fields
    private JButton blueS, blueO, redS, redO, simpleGame, generalGame;
    private JTextField boardSizeField;
    private SOSGame game;  // Game logic
    private JLabel turnLabel;  // Display whose turn
    private int boardSize = 4;  // Default size
    private JPanel boardPanel;  // Holds board buttons

    // Getter methods
    public int getBoardSize() { return boardSize; }
    public SOSGame getGame() { return game; }
    public JLabel getTurnLabel() { return turnLabel; }
    public JTextField getBoardSizeField() { return boardSizeField; }
    
    // Constructor: Set up GUI components
    public SosGUI() {
        game = new SOSGame(boardSize);

        frame = new JFrame("SOS");  // Main window setup
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        // Set up top control panel
        JPanel topPanel = new JPanel();
        simpleGame = new JButton("Simple Game");
        generalGame = new JButton("General Game");
        boardSizeField = new JTextField(5);
        boardSizeField.setText(String.valueOf(boardSize));
        JButton applyBoardSizeButton = new JButton("Apply Board Size");
        applyBoardSizeButton.addActionListener(e -> reconstructBoardUI());

        topPanel.add(new JLabel("SOS"));  // Adding components to top panel
        topPanel.add(simpleGame);
        topPanel.add(generalGame);
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

        // Add panels to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Update board UI when board size changes
    public void reconstructBoardUI() {
        int newSize;
        try {
            newSize = Integer.parseInt(boardSizeField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid board size.");
            return;
        }
        if (newSize <= 2) {
            JOptionPane.showMessageDialog(frame, "Please enter a board size greater than 2.");
            return;
        }
        boardSize = newSize;
        game = new SOSGame(boardSize);
        
        frame.remove(boardPanel);  // Remove old board
        boardPanel = new JPanel(new GridLayout(boardSize, boardSize));  // Create new board
        boardButtons = new JButton[boardSize][boardSize];
        initBoardButtons();
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
        toggleTurn();
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
            game.makeMove(row, column);
            boardButtons[row][column].setText(String.valueOf(game.getTurn()));
            toggleTurn();
            boardButtons[row][column].setEnabled(false);  // Disable button after move
        }
    }

    // Update turn display
    private void toggleTurn() {
        if (game.getTurn() == 'S') {
            turnLabel.setText("Turn: Red");
            turnLabel.setForeground(Color.RED);
        } else {
            turnLabel.setText("Turn: Blue");
            turnLabel.setForeground(Color.BLUE);
        }
    }

    // Main method: Launch the GUI
    public static void main(String[] args) {
        new SosGUI();
    }
}
