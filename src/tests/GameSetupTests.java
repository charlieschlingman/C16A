package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.Player;



public class GameSetupTests {
	
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
	}
	
	@Test
	public void testLoadingPlayers() {
		//Start by loading in all of the players, then checking to see if the proper amount were loaded
		List<Player> players = board.getPlayers();
		assertEquals(players.size(), 6);
		
		//Check to see if all of the players were added given the name
		assertTrue(players.contains(board.getThisPlayer("Mr. Tomato")));
		assertTrue(players.contains(board.getThisPlayer("Prof. Blueberry")));
		assertTrue(players.contains(board.getThisPlayer("Mrs. Orange")));
		assertTrue(players.contains(board.getThisPlayer("Ms. Lettuce")));
		assertTrue(players.contains(board.getThisPlayer("Mr. Mustard")));
		assertTrue(players.contains(board.getThisPlayer("Ltn. Mayo")));
		
		//Check to see if all of the players were added given the color
		assertTrue(players.contains(board.getThisPlayer(Color.red)));
		assertTrue(players.contains(board.getThisPlayer(Color.blue)));
		assertTrue(players.contains(board.getThisPlayer(Color.orange)));
		assertTrue(players.contains(board.getThisPlayer(Color.green)));
		assertTrue(players.contains(board.getThisPlayer(Color.yellow)));
		assertTrue(players.contains(board.getThisPlayer(Color.white)));
		
		//Checks to see if all of the players have the proper starting row
		assertEquals(board.getThisPlayer("Mr. Tomato").getRow(), 25);
		assertEquals(board.getThisPlayer("Prof. Blueberry").getRow(), 17);
		assertEquals(board.getThisPlayer("Mrs. Orange").getRow(), 9);
		assertEquals(board.getThisPlayer("Ms. Lettuce").getRow(), 0);
		assertEquals(board.getThisPlayer("Mr. Mustard").getRow(), 0);
		assertEquals(board.getThisPlayer("Ltn. Mayo").getRow(), 8);
		
		//Checks to see if all of the players have the proper starting column
		assertEquals(board.getThisPlayer("Mr. Tomato").getCol(), 7);
		assertEquals(board.getThisPlayer("Prof. Blueberry").getCol(), 0);
		assertEquals(board.getThisPlayer("Mrs. Orange").getCol(), 0);
		assertEquals(board.getThisPlayer("Ms. Lettuce").getCol(), 6);
		assertEquals(board.getThisPlayer("Mr. Mustard").getCol(), 17);
		assertEquals(board.getThisPlayer("Ltn. Mayo").getCol(), 23);
		
		
	}
	
	
	@Test
	public void testLoadingWeapons() {
		//Start by loading in all of the weapons and seeing if the correct number of weapons were loaded
		List<String> weapons = board.getWeapons();
		assertEquals(weapons.size(), 6);
		
		//Check to see if all of the weapons are loaded given the name
		assertTrue(weapons.contains(board.getWeapon("AK-47")));
		assertTrue(weapons.contains(board.getWeapon("Glock 19")));
		assertTrue(weapons.contains(board.getWeapon("Medieval Sword")));
		assertTrue(weapons.contains(board.getWeapon("Lightsaber")));
		assertTrue(weapons.contains(board.getWeapon("Wandstick")));
		assertTrue(weapons.contains(board.getWeapon("Broom")));
		
	}
	
	
	@Test
	public void testLoadingCards() {
		//Load all of the cards, then test to see if all of them have the proper amount
		List<Card> allCards = board.getAllCards();
		List<Card> roomCards = board.getRoomCards();
		List<Card> weaponCards = board.getWeaponCards();
		List<Card> personCards = board.getPersonCards();
		
		assertEquals(allCards.size(), 21);
		assertEquals(roomCards.size(), 9);
		assertEquals(weaponCards.size(), 6);
		assertEquals(personCards.size(), 6);
		
	}
	
	@Test
	public void testDealingCards() {
		//Deal all the cards, then check to see if the answer has 3 cards
		List<Card> theAnswer = board.getTheAnswer();
		assertEquals(3, theAnswer.size());
		//Check to see if after dealing, there are no more cards left that haven't been dealt
		List<Card> allDealableCards = board.getAllDealableCards();
		assertEquals(allDealableCards.size(), 0);
		
		//Check to see if all of the players have the proper amount of cards (should all be 3 because we have 18 cards that need to be dealt and 18/6=3)
		assertEquals(board.getThisPlayer("Mr. Tomato").getMyCards().size(), 3);
		assertEquals(board.getThisPlayer("Prof. Blueberry").getMyCards().size(), 3);
		assertEquals(board.getThisPlayer("Mrs. Orange").getMyCards().size(), 3);
		assertEquals(board.getThisPlayer("Ms. Lettuce").getMyCards().size(), 3);
		assertEquals(board.getThisPlayer("Mr. Mustard").getMyCards().size(), 3);
		assertEquals(board.getThisPlayer("Ltn. Mayo").getMyCards().size(), 3);
		
		
		
		//Check to make sure that the answer is different for every instance (if this fails while grading I'm buying a lottery ticket, it's a 0.075% chance)
		Board board2 = Board.getInstance();
		board2.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board2.initialize();
		List<Card> theAnswer2 = board2.getTheAnswer();
		
		assertNotEquals(theAnswer2, theAnswer);
	}

}
