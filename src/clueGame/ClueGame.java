package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClueGame extends JPanel {
	
	public ClueGame() {

	    setLayout(new BorderLayout());


	    JPanel board = new JPanel();
	    board.setBackground(Color.black);
	    add(board, BorderLayout.CENTER);


	    CardControlPanel cardpanel = new CardControlPanel();
	    add(cardpanel, BorderLayout.EAST);


	    GameControlPanel gamepanel = new GameControlPanel();
	    add(gamepanel, BorderLayout.SOUTH);
		
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		ClueGame cluegame = new ClueGame();  // create the panel
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(cluegame); // put the panel in the frame
		frame.setSize(1600, 900);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
	}
	
	
	
	
}
