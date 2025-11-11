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
		Card room = new Card(CardType.ROOM, "Arcade");
		Card weapon = new Card(CardType.WEAPON, "Glock-19");
		Card person = new Card(CardType.PERSON, "Mr. Mustard");
		
		List<Card> customSolution = new ArrayList<Card>();
		customSolution.add(room);
		customSolution.add(weapon);
		customSolution.add(person);
		
		
		board.setTheAnswer(customSolution);
		assertTrue(board.testAccusation(room, weapon, person));
		
		assertThrows(Exception.class, () -> {
			board.testAccusation(room, room, person);
		});
		assertThrows(Exception.class, () -> {
			board.testAccusation(person, weapon, person);
		});
		assertThrows(Exception.class, () -> {
			board.testAccusation(room, weapon, weapon);
		});
		
		Card wrongPerson = new Card(CardType.PERSON, "Lt. Mayo");
		assertFalse(board.testAccusation(room, weapon, wrongPerson));
	}
	
	@Test
	public void testDisproveSuggestion() {
		Player player = new Player("Player", Color.black, "Human", 0, 0);
		Card room = new Card(CardType.ROOM, "Arcade");
		Card weapon = new Card(CardType.WEAPON, "Glock-19");
		Card person = new Card(CardType.PERSON, "Mr. Mustard");
		
		player.addCard(room);
		player.addCard(weapon);
		player.addCard(person);
		
		Card wrongPerson = new Card(CardType.PERSON, "Lt. Mayo");
		Card wrongRoom = new Card(CardType.ROOM, "Master Bedroom");
		Card wrongWeapon = new Card(CardType.WEAPON, "AK-47");
		
		List<Card> suggestion = new ArrayList<Card>();
		suggestion.add(wrongPerson);
		suggestion.add(wrongRoom);
		suggestion.add(weapon);
		
		assertTrue((player.disproveSuggestion(suggestion).equals(weapon)));
		
		suggestion.clear();
		suggestion.add(person);
		suggestion.add(wrongRoom);
		suggestion.add(wrongWeapon);
		
		assertTrue(player.disproveSuggestion(suggestion).equals(person));
		
		suggestion.clear();
		suggestion.add(wrongPerson);
		suggestion.add(room);
		suggestion.add(wrongWeapon);
		
		assertTrue(player.disproveSuggestion(suggestion).equals(room));
		
		suggestion.clear();
		suggestion.add(wrongPerson);
		suggestion.add(wrongRoom);
		suggestion.add(wrongWeapon);
		
		assertDoesNotThrow(() -> player.disproveSuggestion(suggestion));
		
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
	
	

	
}
