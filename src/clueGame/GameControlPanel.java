package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {
	private JTextField turn;
	private JTextField roll;
	private JTextField guessDisplay;
	private JTextField guessResult;



	public GameControlPanel()  {
		
		setLayout(new GridLayout(2, 1));
		
		JPanel topPanel = new JPanel(new GridLayout(1, 4));

		JPanel turnPanel = new JPanel();
		turnPanel.add(new JLabel("Whose Turn?"), BorderLayout.CENTER);

		turn = new JTextField(15);

		turnPanel.add(turn, BorderLayout.CENTER);

		topPanel.add(turnPanel, BorderLayout.WEST);

		JPanel rollPanel = new JPanel();
		rollPanel.add(new JLabel("Roll"), BorderLayout.NORTH);

		roll = new JTextField(5);
		rollPanel.add(roll, BorderLayout.CENTER);

		topPanel.add(rollPanel, BorderLayout.CENTER);

		JButton accusationButton = new JButton("Make Accusation"); 
		topPanel.add(accusationButton); 
		
		JButton nextButton = new JButton("Next Turn"); 
		topPanel.add(nextButton);
		
		add(topPanel);
		
		JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
		
		JPanel guessPanel = new JPanel();
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		
		guessDisplay = new JTextField(20);
		guessPanel.add(guessDisplay, BorderLayout.WEST);
		
		
		bottomPanel.add(guessPanel, BorderLayout.WEST);
		
		JPanel resultPanel = new JPanel();
		resultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Result"));
		
		guessResult = new JTextField(20);
		resultPanel.add(guessResult, BorderLayout.WEST);
		
		bottomPanel.add(resultPanel, BorderLayout.CENTER);
		
		add(bottomPanel);
		
		



	}
	
	public void setGuess(String guess) {
		guessDisplay.setText(guess);
	}
	
	
	public void setGuessResult(String result) {
		guessResult.setText(result);
	}
	
	
	public void setTurn(Player player, int givenRoll) {
		turn.setText(player.getName());
		turn.setBackground(player.getColor());
		roll.setText(Integer.toString(givenRoll));
	}

	
	

	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();  // create the panel
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(750, 180);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
		// test filling in the data
		panel.setTurn(new HumanPlayer( "Col. Mustard", Color.ORANGE, "Human", 0, 0), 5);
		panel.setGuess( "I have no guess!");
		panel.setGuessResult( "So you have nothing?");
	}
	

}
