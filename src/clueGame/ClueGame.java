package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClueGame extends JFrame {

    private BoardPanel board;

    public ClueGame() {
        super("Clue Game");  // Title of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLayout(new BorderLayout());

        // BOARD PANEL
        board = BoardPanel.getInstance();  // Singleton Board
        board.initialize();           // Loads layout + setup
        add(board, BorderLayout.CENTER);

        // RIGHT PANEL
        CardControlPanel cardpanel = new CardControlPanel();
        add(cardpanel, BorderLayout.EAST);

        // BOTTOM PANEL
        GameControlPanel gamepanel = new GameControlPanel();
        add(gamepanel, BorderLayout.SOUTH);
    }
	
	
	
    public static void main(String[] args) {
        ClueGame game = new ClueGame();
        game.setVisible(true);
    }
	
	
	
	
}
