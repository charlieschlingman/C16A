package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClueGame extends JFrame {

    private BoardPanel board;

    public ClueGame() {
        super("Clue Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        setLayout(new BorderLayout());

        // Board
        board = BoardPanel.getInstance();  // Singleton Board
        board.initialize(); 
        add(board, BorderLayout.CENTER);

        // Right Panel
        CardControlPanel cardpanel = new CardControlPanel();
        add(cardpanel, BorderLayout.EAST);

        // Bottom Panel
        GameControlPanel gamepanel = new GameControlPanel();
        add(gamepanel, BorderLayout.SOUTH);
    }
	
	
	
    public static void main(String[] args) {
        ClueGame game = new ClueGame();
        game.setVisible(true);
    }
	
}
