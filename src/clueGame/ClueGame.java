package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ClueGame extends JFrame {

	private BoardPanel board;
	private CardControlPanel cardpanel;
	private GameControlPanel gamepanel;
	private Boolean answerGuessed = false;
	private Integer turn = 0;
	private static boolean playerTurn = false;
	private static ClueGame instance;

	public static ClueGame getInstance() { 
		return instance; 
	}

	public ClueGame() {
		super("Clue Game");
		instance = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1600, 900);
		setLayout(new BorderLayout());

		// Board
		board = BoardPanel.getInstance();  // Singleton Board
		board.initialize();
		board.setBoardClickListener((row, col) -> handleBoardClick(row, col));
		add(board, BorderLayout.CENTER);

		// Right Panel
		cardpanel = new CardControlPanel();
		add(cardpanel, BorderLayout.EAST);

		// Bottom Panel
		gamepanel = new GameControlPanel();
		add(gamepanel, BorderLayout.SOUTH);
	}

	public void startGame(ClueGame game, Board theBoard) {
		for (Card card: theBoard.getThisPlayer("Mr. Tomato").getMyCards()) {
			game.cardpanel.addHandCard(card);
		}
	}

	public Integer getTurn() {
		return turn;
	}

	public void setTurn(Integer turn) {
		this.turn = turn;
	}

	public static void setPlayerTurn(boolean value) {
		playerTurn = value;
	}

	private void handleBoardClick(int row, int col) {
		// If human already moved this turn, block additional moves
		if (!playerTurn) {
			JOptionPane.showMessageDialog(this,
					"You have already moved this turn. Press NEXT.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Board theBoard = board.getBoard();
		Player human = theBoard.getPlayers().get(0);

		// Only respond during human turn
		if (turn != 0) {
			JOptionPane.showMessageDialog(this,
					"It is not your turn!",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Must have valid targets displayed
		Set<BoardCell> targets = board.getTargets();
		BoardCell clickedCell = theBoard.getCell(row, col);

		// Invalid move
		if (!targets.contains(clickedCell)) {
			JOptionPane.showMessageDialog(this,
					"You must click a highlighted target.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Mark old cell unoccupied
		BoardCell oldCell = theBoard.getCell(human.getRow(), human.getCol());
		oldCell.setOccupied(false);

		// Move player
		human.setLocation(row, col);

		// Mark new cell occupied
		BoardCell newCell = theBoard.getCell(row, col);
		newCell.setOccupied(true);


		// Clear highlights
		board.setTargets(new HashSet<>());

		// Mark human move complete
		setPlayerTurn(false);

		// If entering room, show suggestion dialog
		if (clickedCell.isRoomCenter()) {
			// TODO: show suggestion dialog
		}

		// Redraw
		board.repaint();
	}



	public static void main(String[] args) {
		// Start the game, setting up the game as well as the board
		ClueGame game = new ClueGame();
		Board theBoard = game.board.getBoard();
		game.startGame(game, theBoard);

		// Set the roll, the player lists, and the human player
		Random rand = new Random();
		Integer roll;
		List<Player> players = theBoard.getPlayers();
		Player humanPlayer = players.get(0);

		//Set the targets
		Set<BoardCell> targets = null;

		//Make the Board visible and display the popup
		game.setVisible(true);
		JOptionPane.showMessageDialog(null, "You are Mr. Tomato. Can you find the solution before the computer players do?", "Welcome To Clue", JOptionPane.INFORMATION_MESSAGE);

		// Check to see if the answer has been guessed. If it hasn't, don't end the game
		while (game.answerGuessed != true) {

			// Set the roll and display the current turn and their roll to the board
			roll = rand.nextInt(1, 7);
			game.gamepanel.setTurn(players.get(game.getTurn()), roll);

			// If the turn is the human player, display the targets based on the current cell they are on
			if (game.getTurn() == 0) {
				setPlayerTurn(true);   // human has not moved yet

				BoardCell currentCell = theBoard.getCell(humanPlayer.getRow(), humanPlayer.getCol());
				theBoard.calcTargets(currentCell, roll);
				targets = theBoard.getTargets();
				game.board.setTargets(targets);

				// Wait here until they click a valid target
				while (playerTurn) {
					try { Thread.sleep(50); } 
					catch (InterruptedException e) {}
				}
			}

			// If it is not the human player, make it so there are no targets available
			else {
				targets.clear();
				game.board.setTargets(targets);
			}
			
			// While the next button hasn't been clicked, loop through this
			while (game.gamepanel.nextClicked != true) {
				// Wait 50 milliseconds before updating the board
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Set the turn to the next player
			if (game.getTurn() == 5) {
				game.setTurn(0);
			} 
			else {game.setTurn((game.getTurn() + 1));}

			// Set the next button to not clicked
			game.gamepanel.nextClicked = false;

		}   
	}	
}
