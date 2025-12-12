package clueGame;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {

	private static ClueGame game;
	private BoardPanel board;
	private CardControlPanel cardpanel;
	private GameControlPanel gamepanel;
	private Boolean answerGuessed = false;
	private Integer turn = 0;
	private static boolean playerTurn = false;
	private static ClueGame instance;
	private static boolean hasMoved = false;
	private static accusationWindow accusationFinal;
	private static List<Player> players;
	
	public List<Player> getPlayers() {
		return players;
	}

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

		// Only respond during human turn
		if (turn != 0) {
			JOptionPane.showMessageDialog(this,
					"It is not your turn!",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// If human already moved this turn, block additional moves
		if (!playerTurn) {
			JOptionPane.showMessageDialog(this,
					"You have already moved this turn. Press NEXT.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Board theBoard = board.getBoard();
		Player human = theBoard.getPlayers().get(0);



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

		hasMoved = true;

	}

	private void computerTurn(ComputerPlayer cpu, int roll, Board theBoard) {
		// If cpu is ready to accuse, do so first
		if (cpu.isReadyToAccuse()) {
			Solution a = cpu.getAccusation();

			boolean correct = (theBoard.getTheAnswer().contains(a.getRoom()) && theBoard.getTheAnswer().contains(a.getPerson()) &&
					theBoard.getTheAnswer().contains(a.getWeapon()));

			// Update GUI
			gamepanel.setGuess(a.getRoom().getCardName() + ", " + a.getWeapon().getCardName() + ", " +a.getPerson().getCardName());
			gamepanel.setGuessResult(correct ? "CPU Accusation Correct!" : "CPU Accusation Incorrect!");

			if (correct) { 
				JOptionPane.showMessageDialog(null, cpu.getName() + " solved the case!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			} 
			else {
				// CPU never guesses incorrectly, so do nothing
			}
			return;
		}

		// Calculate all possible targets
		BoardCell start = theBoard.getCell(cpu.getRow(), cpu.getCol());
		theBoard.calcTargets(start, roll);
		Set<BoardCell> targets = theBoard.getTargets();
		System.out.println(cpu.getName() + "'s suggestion status: " + cpu.gotSuggested);
		if (cpu.gotSuggested) {
			targets.add(start);
			cpu.gotSuggested = false;
		}
		
		for (BoardCell target: targets) {
			System.out.println(target);
		}

		if (targets.isEmpty()) {
			System.out.println(cpu.getName() + " cannot move.");
			return;
		}

		// Select targets
		BoardCell dest = cpu.selectTargetAI(targets, theBoard);

		// Mark old cell unoccupied
		BoardCell oldCell = theBoard.getCell(cpu.getRow(), cpu.getCol());
		oldCell.setOccupied(false);

		// Move the CPU
		cpu.setLocation(dest.getRow(), dest.getColumn());

		// Mark new location occupied
		dest.setOccupied(true);

		// Clear highlights
		board.setTargets(new HashSet<>());

		// Repaint the board
		board.repaint();

		// If CPU enters a room, generate a suggestion
		if (dest.isRoomCenter()) {

			// Make suggestion
			Solution suggestion = cpu.createSuggestion(theBoard);

			// Set player index so they don’t disprove themselves
			theBoard.setCurrentPlayerIndex(turn);

			// Attempt to disprove
			Card disproved = null;
			try {
				disproved = theBoard.makeSuggestion(suggestion.getRoom(), suggestion.getWeapon(), suggestion.getPerson());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Update GUI with the cpu's guess
			gamepanel.setGuess(suggestion.getRoom().getCardName() + ", " + suggestion.getWeapon().getCardName() + ", " +
					suggestion.getPerson().getCardName());
			
			for (Player player: players) {
				if (player.getName().equals(suggestion.getPerson().getCardName())) {
					System.out.println("Found suggested player: " + player.getName());
					player.gotSuggested();
					player.setLocation(cpu.getRow(), cpu.getCol());
					game.board.repaint();
				}
			}

			if (disproved == null) {
				// cpu is ready to accuse
				gamepanel.setGuessResult("Suggestion Not Disproved");

				// Only accuse if the cpu has narrowed it to 3 unknown cards
			    List<Card> allCards = theBoard.getAllCards(); 
			    List<Card> seen = cpu.getSeenCards();
			    List<Card> cpuCards = cpu.getMyCards();

			    List<Card> unseen = new ArrayList<>();
			    for (Card c : allCards) {
			        // Unknown if not seen or in hand
			        if (!cpuCards.contains(c) && !seen.contains(c)) {
			            unseen.add(c);
			        }
			    }

			    // If there are exactly 3 unseen cards, they must be the solution
			    if (unseen.size() == 3) {
			        Card roomCard = null;
			        Card personCard = null;
			        Card weaponCard = null;

			        for (Card c : unseen) {
			            if (c.getCardType() == CardType.ROOM) {
			                roomCard = c;
			            } 
			            else if (c.getCardType() == CardType.PERSON) {
			                personCard = c;
			            } 
			            else if (c.getCardType() == CardType.WEAPON) {
			                weaponCard = c;
			            }
			        }

			        // Only if one of each type, then set accusation
			        if (roomCard != null && personCard != null && weaponCard != null) {
			            Solution acc = new Solution(roomCard, weaponCard, personCard);
			            cpu.setAccusation(acc);
			            cpu.setReadyToAccuse(true);
			        }
			    }
			}
			else {
				Player disprover = theBoard.getLastDisprovingPlayer();
				gamepanel.setGuessResult("Suggestion Disproved by " + disprover.getName());
				// CPU learns from disproval
				cpu.seeCard(disproved);
			}
		}
	}




	public static void main(String[] args) throws Exception {
		// Start the game, setting up the game as well as the board
		game = new ClueGame();
		Board theBoard = game.board.getBoard();
		game.startGame(game, theBoard);

		// Set the roll, the player lists, and the human player
		Random rand = new Random();
		Integer roll;
		players = theBoard.getPlayers();
		Player humanPlayer = players.get(0);

		//Set the targets
		Set<BoardCell> targets = null;

		//Make the Board visible and display the popup
		game.setVisible(true);
		JOptionPane.showMessageDialog(null, "You are Mr. Tomato. Can you find the solution before the computer players do?", "Welcome To Clue", JOptionPane.INFORMATION_MESSAGE);

		for (Card card: theBoard.getTheAnswer()) {
			System.out.println(card.getCardName());
		}
		// Check to see if the answer has been guessed. If it hasn't, don't end the game
		game_loop:
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
					if (humanPlayer.gotSuggested) {
						targets.add(currentCell);
						humanPlayer.gotSuggested = false;
					}
					game.board.setTargets(targets);

					// Wait here until they click a valid target
					while (playerTurn) {
						try { Thread.sleep(50); } 
						catch (InterruptedException e) {}

						// If the player tries to hit next when they haven't moved, an error pops up
						if (!hasMoved && game.gamepanel.nextClicked) {
							JOptionPane.showMessageDialog(null, "You must move before clicking next", "Error", JOptionPane.ERROR_MESSAGE);
							game.gamepanel.nextClicked = false;
						}


						// If the accusation button is clicked, make an accusation
						if (game.gamepanel.accusationClicked) {
							// Open a new accusation window
							accusationWindow accusation = new accusationWindow(theBoard.getRoomCards(), theBoard.getPersonCards(), theBoard.getWeaponCards());
							accusation.setVisible(true);
							// Wait until the accusation window isnt open
							while (accusation.isOpen) {
								try {
									Thread.sleep(50);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							// If the roomGuess is null, that means the player did not make an accusation, so only do the following code if there is a room guess
							if (accusation.roomGuess != null) {
								// Set the accusation window to a variable that can be used by the whole class
								accusationFinal = accusation;
								// Set answered to true
								game.answerGuessed = true;
								// Break out of the big while loop
								break game_loop;
							}
							game.gamepanel.accusationClicked = false;
						}



					}

					// Get the cell the player is at after they move
					BoardCell currentCellAfterMove = theBoard.getCell(humanPlayer.getRow(), humanPlayer.getCol());
					// If it is a room center, run the suggestion logic
					if (currentCellAfterMove.isRoomCenter()) {
						// Same as accusation, set the suggestion window to open and wait until the window is closed
						suggestionWindow suggestion = new suggestionWindow(humanPlayer, theBoard);
						suggestion.setVisible(true);
						while (suggestion.isOpen) {
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						// Same as above, if the player didn't make an accusation, then weaponSuggestion is null, so only do the code below if they made a suggestion
						if (suggestion.weaponSuggestion != null) {
							// Set the current player index of the board to the current players index, so they get skipped when checking the other players cards
							theBoard.setCurrentPlayerIndex(game.getTurn());
							Card suggestedPlayerCard = suggestion.playerSuggestion;
							for (Player player: players) {
								if (player.getName().equals(suggestedPlayerCard.getCardName())) {
									System.out.println("Found Suggested Player: " + player.getName());
									player.gotSuggested();

									System.out.println(player.getName() + "'s Suggested Status: " + player.gotSuggested);
									Player currentPlayerThatSuggested = players.get(game.getTurn());
									player.setLocation(currentPlayerThatSuggested.getRow(), currentPlayerThatSuggested.getCol());
									System.out.println(player.getName() + "'s location set to: " + player.getRow() + ", " + player.getCol());
									game.board.repaint();
								}
							}
							// Set the result of the suggestion
							Card suggestionResult = theBoard.makeSuggestion(suggestion.roomSuggestion, suggestion.weaponSuggestion, suggestion.playerSuggestion);
							game.gamepanel.setGuess(suggestion.roomSuggestion.getCardName() + ", " + suggestion.weaponSuggestion.getCardName() + ", " + suggestion.playerSuggestion.getCardName());

							// If the result is null, print no new clues, if not, set the suggestion result name to the guess result and add the result to the card panel. 
							if (suggestionResult == null) {
								game.gamepanel.setGuessResult("No New Clue");
							} else {
								game.gamepanel.setGuessResult(suggestionResult.getCardName());
								game.cardpanel.addSeenCard(suggestionResult);
							}
							
							
						}

					}


				}

				// If it is not the human player, make it so there are no targets available
				else {
					ComputerPlayer cpu = (ComputerPlayer) players.get(game.getTurn());
					game.computerTurn(cpu, roll, theBoard);
				}

				// While the next button hasn't been clicked, loop through this
				while (game.gamepanel.nextClicked != true) {
					// Gives this error if the player hits the accusation button while it is not their turn
					if ((game.getTurn() != 0) && game.gamepanel.accusationClicked) {
						JOptionPane.showMessageDialog(null, "It must be your turn to make an accusation.", "Error", JOptionPane.ERROR_MESSAGE);
						game.gamepanel.accusationClicked = false;
					}

					// Do the accusation logic again, as they can make an accusation before and after they move. 
					if (game.gamepanel.accusationClicked) {
						accusationWindow accusation = new accusationWindow(theBoard.getRoomCards(), theBoard.getPersonCards(), theBoard.getWeaponCards());
						accusation.setVisible(true);
						while (accusation.isOpen) {
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (accusation.roomGuess != null) {
							game.answerGuessed = true;
							accusationFinal = accusation;
							break game_loop;
						}
						game.gamepanel.accusationClicked = false;
					}



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

				// Set the next button to not clicked, the accusation button to not clicked, and hasMoved to false
				game.gamepanel.nextClicked = false;
				game.gamepanel.accusationClicked = false;
				hasMoved = false;

			}

		// Gather the final guess into a list of cards
		List<Card> finalGuess = new ArrayList<>();
		finalGuess.add(accusationFinal.roomGuess);
		finalGuess.add(accusationFinal.weaponGuess);
		finalGuess.add(accusationFinal.playerGuess);

		boolean answerIsRight = true;

		// Go through each card, and if the board doesn't find that card in the answer, set that the answer is not right
		for (Card card: finalGuess) {
			if (!theBoard.getTheAnswer().contains(card)) {
				answerIsRight = false; 
			} 
		}

		// Display the respective results to the player
		if (answerIsRight) {
			JOptionPane.showMessageDialog(null, "That was the right guess. You Win!", "Final Result", JOptionPane.INFORMATION_MESSAGE);
			game.dispose();
		} else {
			JOptionPane.showMessageDialog(null, "Sorry, that wasn't the right guess. You Lose!", "Final Result", JOptionPane.INFORMATION_MESSAGE);
			game.dispose();
		}
	}	
}
