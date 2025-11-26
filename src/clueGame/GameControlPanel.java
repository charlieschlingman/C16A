package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	public boolean nextClicked = false;
	



	public GameControlPanel()  {
		
		// Set the layout of the display
		setLayout(new GridLayout(2, 1));
		
		// Set the top part of the panel
		JPanel topPanel = new JPanel(new GridLayout(1, 4));

		
		// Add a turn panel with text saying Whose Turn? and the person whose turn it is
		JPanel turnPanel = new JPanel();
		turnPanel.add(new JLabel("Whose Turn?"), BorderLayout.CENTER);

		turn = new JTextField(15);

		turnPanel.add(turn, BorderLayout.CENTER);
		
		// Add it to the top panel

		topPanel.add(turnPanel, BorderLayout.WEST);

		
		// Do the same for the roll panel to display the roll
		JPanel rollPanel = new JPanel();
		rollPanel.add(new JLabel("Roll"), BorderLayout.NORTH);

		roll = new JTextField(5);
		rollPanel.add(roll, BorderLayout.CENTER);
		
		// Add to top panel

		topPanel.add(rollPanel, BorderLayout.CENTER);

		// Add a accusation button
		JButton accusationButton = new JButton("Make Accusation"); 
		topPanel.add(accusationButton); 
		
		// Add a next button
		JButton nextButton = new JButton("Next Turn"); 
		topPanel.add(nextButton);
		
		nextButton.addActionListener(new nextListener());
		
		// Add the top panel to the display
		add(topPanel);
		
		// Set the bottom panel
		JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
		
		// Set the guess panel to have a border
		JPanel guessPanel = new JPanel();
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		
		// Add the guess that the person makes
		guessDisplay = new JTextField(20);
		guessPanel.add(guessDisplay, BorderLayout.WEST);
		
		// Add it to the bottom panel
		bottomPanel.add(guessPanel, BorderLayout.WEST);
		
		// Add a result panel with the border that says result
		JPanel resultPanel = new JPanel();
		resultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Result"));
		
		// Add a guess result to the panel
		guessResult = new JTextField(20);
		resultPanel.add(guessResult, BorderLayout.WEST);
		
		// Add it to the bottom panel
		bottomPanel.add(resultPanel, BorderLayout.CENTER);
		
		// Add the bottom panel
		add(bottomPanel);
		
		



	}
	
	
	// Inner class to handle the button action.
	private class nextListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Button clicked!");
			nextClicked = true;
		}
	}
	
	// Set the guess given a string
	public void setGuess(String guess) {
		guessDisplay.setText(guess);
	}
	
	// Set the result given a result
	public void setGuessResult(String result) {
		guessResult.setText(result);
	}
	
	// Set the turn text and background given a player, and a roll given a roll count
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
		panel.setTurn(new HumanPlayer( "Col. Mustard", Color.ORANGE, "Human", 0, 0), 4);
		panel.setGuess( "I have no guess!");
		panel.setGuessResult( "So you have nothing?");
	}
	

}
