import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SosGUI {

    private JFrame frame;
    private JButton[][] boardButtons;
    private JButton blueS, blueO, redS, redO, simpleGame, generalGame;
    private JTextField boardSizeField;
    private SOSGame game;
    private JLabel turnLabel;
    private int boardSize = 4;  // default board size
    private JPanel boardPanel;  // Declare this outside to access it easily

    public SosGUI() {
        game = new SOSGame(boardSize);

        frame = new JFrame("SOS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        JPanel topPanel = new JPanel();
        simpleGame = new JButton("Simple Game");
        generalGame = new JButton("General Game");
        boardSizeField = new JTextField(5);
        boardSizeField.setText(String.valueOf(boardSize));

        JButton applyBoardSizeButton = new JButton("Apply Board Size");
        applyBoardSizeButton.addActionListener(e -> reconstructBoardUI());

        topPanel.add(new JLabel("SOS"));
        topPanel.add(simpleGame);
        topPanel.add(generalGame);
        topPanel.add(new JLabel("Board Size:"));
        topPanel.add(boardSizeField);
        topPanel.add(applyBoardSizeButton);

        boardPanel = new JPanel(new GridLayout(boardSize, boardSize));
        boardButtons = new JButton[boardSize][boardSize];
        initBoardButtons();

        JPanel bottomPanel = new JPanel();
        blueS = new JButton("S");
        blueO = new JButton("O");
        redS = new JButton("S");
        redO = new JButton("O");
        bottomPanel.add(new JLabel("Blue Player:"));
        bottomPanel.add(blueS);
        bottomPanel.add(blueO);
        bottomPanel.add(new JLabel("Red Player:"));
        bottomPanel.add(redS);
        bottomPanel.add(redO);
        
        turnLabel = new JLabel("Turn: Blue");
        turnLabel.setForeground(Color.BLUE);
        bottomPanel.add(turnLabel);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void reconstructBoardUI() {
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
        
        frame.remove(boardPanel);
        boardPanel = new JPanel(new GridLayout(boardSize, boardSize));
        boardButtons = new JButton[boardSize][boardSize];
        initBoardButtons();

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
        
        toggleTurn();
    }

    private void initBoardButtons() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                JButton btn = new JButton("");
                btn.addActionListener(new ButtonListener(i, j));
                boardPanel.add(btn);
                boardButtons[i][j] = btn;
            }
        }
    }

    private class ButtonListener implements ActionListener {
        private int row;
        private int column;

        public ButtonListener(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeMove(row, column);
            boardButtons[row][column].setText(String.valueOf(game.getTurn()));
            toggleTurn();
            boardButtons[row][column].setEnabled(false);
        }
    }

    private void toggleTurn() {
        if (game.getTurn() == 'S') {
            turnLabel.setText("Turn: Red");
            turnLabel.setForeground(Color.RED);
        } else {
            turnLabel.setText("Turn: Blue");
            turnLabel.setForeground(Color.BLUE);
        }
    }

    public static void main(String[] args) {
        new SosGUI();
    }
}
