package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;
import clueGame.Solution;

public class GameSolutionTest {

	
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
	}
	
	@Test
	public void TestAccusation() throws Exception {
		
		// Get 3 cards, one of each type, and make a custom solution that we can test from
		Card room = new Card(CardType.ROOM, "Arcade");
		Card weapon = new Card(CardType.WEAPON, "Glock-19");
		Card person = new Card(CardType.PERSON, "Mr. Mustard");
		
		List<Card> customSolution = new ArrayList<Card>();
		customSolution.add(room);
		customSolution.add(weapon);
		customSolution.add(person);
		
		//Set the custom solution to the answer
		board.setTheAnswer(customSolution);
		//Test to make sure that if the proper room, weapon, and person are inputted, that testAccusation returns true
		assertTrue(board.testAccusation(room, weapon, person));
		
		//Make sure all the exceptions throw
		assertThrows(Exception.class, () -> {
			board.testAccusation(room, room, person);
		});
		assertThrows(Exception.class, () -> {
			board.testAccusation(person, weapon, person);
		});
		assertThrows(Exception.class, () -> {
			board.testAccusation(room, weapon, weapon);
		});
		// If a wrong card is inputted as the accusation, return false
		Card wrongPerson = new Card(CardType.PERSON, "Lt. Mayo");
		assertFalse(board.testAccusation(room, weapon, wrongPerson));
	}
	
	@Test
	public void testDisproveSuggestion() {
		// Add a player with 3 cards
		Player player = new Player("Player", Color.black, "Human", 0, 0);
		Card room = new Card(CardType.ROOM, "Arcade");
		Card weapon = new Card(CardType.WEAPON, "Glock-19");
		Card person = new Card(CardType.PERSON, "Mr. Mustard");
		
		player.addCard(room);
		player.addCard(weapon);
		player.addCard(person);
		
		// Make 3 cards that are not the same as the players cards
		Card wrongPerson = new Card(CardType.PERSON, "Lt. Mayo");
		Card wrongRoom = new Card(CardType.ROOM, "Master Bedroom");
		Card wrongWeapon = new Card(CardType.WEAPON, "AK-47");
		
		// Add 2 cards to a suggestion, and test to see if given the right weapon, it will give the weapon as the output
		List<Card> suggestion = new ArrayList<Card>();
		suggestion.add(wrongPerson);
		suggestion.add(wrongRoom);
		suggestion.add(weapon);
		
		assertTrue((player.disproveSuggestion(suggestion).equals(weapon)));
		
		// Do the same but with the right person
		suggestion.clear();
		suggestion.add(person);
		suggestion.add(wrongRoom);
		suggestion.add(wrongWeapon);
		
		assertTrue(player.disproveSuggestion(suggestion).equals(person));
		
		// Do the same but with the right room
		suggestion.clear();
		suggestion.add(wrongPerson);
		suggestion.add(room);
		suggestion.add(wrongWeapon);
		
		assertTrue(player.disproveSuggestion(suggestion).equals(room));
		
		//Make sure if the input is not in the players hand, return null
		suggestion.clear();
		suggestion.add(wrongPerson);
		suggestion.add(wrongRoom);
		suggestion.add(wrongWeapon);
		
		assertDoesNotThrow(() -> player.disproveSuggestion(suggestion));
		
		//Test to make sure given 2 cards that the player has, it returns a random one out of them (and also returns both of the cards eventually)
		suggestion.clear();
		suggestion.add(room);
		suggestion.add(person);
		suggestion.add(wrongWeapon);
		
		List<Card> cardsThatDisprove = new ArrayList<Card>();
		while (cardsThatDisprove.size() < 2) {
			Card cardThatDisproves = player.disproveSuggestion(suggestion);
			if (!cardsThatDisprove.contains(cardThatDisproves)) {
				cardsThatDisprove.add(cardThatDisproves);
			}
		}
		assertTrue(cardsThatDisprove.contains(room));
		assertTrue(cardsThatDisprove.contains(person));
	}
	
	@Test
	public void testMakeSuggestion() throws Exception {
		// Make 3 players with 3 cards, one of each type
		Player player1 = new Player("Player1", Color.black, "Human", 0, 0);
		Card room = new Card(CardType.ROOM, "Arcade");
		Card weapon = new Card(CardType.WEAPON, "Glock-19");
		Card person = new Card(CardType.PERSON, "Mr. Mustard");
		
		player1.addCard(room);
		player1.addCard(weapon);
		player1.addCard(person);
		
		Player player2 = new Player("Player2", Color.cyan, "Human", 0, 0);
		Card room2 = new Card(CardType.ROOM, "Master Bedroom");
		Card weapon2 = new Card(CardType.WEAPON, "AK-47");
		Card person2 = new Card(CardType.PERSON, "Lt. Mayo");
		
		player2.addCard(room2);
		player2.addCard(weapon2);
		player2.addCard(person2);
		
		Player player3 = new Player("Player3", Color.white, "Human", 0, 0);
		Card room3 = new Card(CardType.ROOM, "Guest Bedroom");
		Card weapon3 = new Card(CardType.WEAPON, "Lightsaber");
		Card person3 = new Card(CardType.PERSON, "Mr. Tomato");
		
		player3.addCard(room3);
		player3.addCard(weapon3);
		player3.addCard(person3);
		
		// Make 3 cards that are not in any of the 3 players hands
		Card wrongRoom = new Card(CardType.ROOM, "Den");
		Card wrongWeapon = new Card(CardType.WEAPON, "Wandstick");
		Card wrongPerson = new Card(CardType.PERSON, "Mrs. Orange");
		
		// Add the players to a list, and set those players to the players of the game
		List<Player> players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		
		board.setPlayers(players);
		
		// Make sure given a right room, weapon, or person, it returns the card. Test one type of card and one player each time. 
		assertTrue(board.makeSuggestion(wrongRoom, wrongWeapon, person).equals(person));
		assertTrue(board.makeSuggestion(room2, wrongWeapon, wrongPerson).equals(room2));
		assertTrue(board.makeSuggestion(wrongRoom, weapon3, wrongPerson).equals(weapon3));
		
		// Test to make sure the exceptions throw
		assertThrows(Exception.class, () -> {
			board.makeSuggestion(room, room, person);
		});
		assertThrows(Exception.class, () -> {
			board.makeSuggestion(person, weapon, person);
		});
		assertThrows(Exception.class, () -> {
			board.makeSuggestion(room, weapon, weapon);
		});
		
		// Test that given a couple of right cards, it only returns on, being the one of the first player
		assertTrue(board.makeSuggestion(room, wrongWeapon, person2).equals(room));
		assertTrue(board.makeSuggestion(room2, weapon, person3).equals(weapon));
		assertTrue(board.makeSuggestion(room3, wrongWeapon, person).equals(person));
		
	}

	
}
