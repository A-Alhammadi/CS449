import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class SosGUITest {

    private SosGUI gui;

    @Before
    public void setUp() {
        // Running GUI code in the EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            gui = new SosGUI();
        });
    }

    // 1. Choose a board size
    @Test
    public void testDefaultBoardSize() {
        assertEquals(4, gui.getBoardSize());
    }

    @Test
    public void testSetInvalidBoardSize() {
        SwingUtilities.invokeLater(() -> {
            gui.getBoardSizeField().setText("a");
            gui.reconstructBoardUI();
            // after attempting to set an invalid board size, it should still be the default
            assertEquals(4, gui.getBoardSize());
        });
    }

    @Test
    public void testSetValidBoardSize() {
        SwingUtilities.invokeLater(() -> {
            gui.getBoardSizeField().setText("5");
            gui.reconstructBoardUI();
            assertEquals(5, gui.getBoardSize());
        });
    }

}
