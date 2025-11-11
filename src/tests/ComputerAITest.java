package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.Room;
import clueGame.Solution;

public class ComputerAITest {

	private static Board board;
	private static ComputerPlayer compPlayer;

	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
		compPlayer = new ComputerPlayer("COM", Color.black, "Computer", 3, 2);
	}

	@Test
	public void testSelectTarget_NoRooms_SelectsRandomly() {
		// given only walkway targets
		Set<BoardCell> targets = new HashSet<>();
		targets.add(board.getCell(1, 6));
		targets.add(board.getCell(3, 6));
		targets.add(board.getCell(2, 7));

		Set<BoardCell> chosen = new HashSet<>();
		for (int i = 0; i < 100; i++) {
			BoardCell target = compPlayer.selectTargetAI(targets, board);
			chosen.add(target);
		}

		assertEquals(3, chosen.size());
	}

	@Test
	public void testSelectTarget_UnseenRoom_SelectedImmediately() {
		Set<BoardCell> targets = new HashSet<>();
		targets.add(board.getCell(3, 2));
		targets.add(board.getCell(5, 6));

		compPlayer.getSeenCards().clear();
		
		BoardCell selected = compPlayer.selectTargetAI(targets, board);
		assertEquals(board.getCell(3, 2), selected);
	}

	@Test
	public void testSelectTarget_SeenRoom_SelectsRandomly() {
		Set<BoardCell> targets = new HashSet<>();
        targets.add(board.getCell(3, 2));
        targets.add(board.getCell(5, 6));
        targets.add(board.getCell(3, 6));
        targets.add(board.getCell(1, 6));

        compPlayer.getSeenCards().clear();
        Room r = board.getRoom(board.getCell(3, 2));
        compPlayer.seeCard(new Card(CardType.ROOM, r.getName()));

        Set<BoardCell> chosen = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            BoardCell target = compPlayer.selectTargetAI(targets, board);
            chosen.add(target);
        }

        assertEquals(4, chosen.size());
	}

	@Test
	public void testCreateSuggestion_RoomMatchesLocation() {
		Solution suggestion = compPlayer.createSuggestion(board);
		assertEquals("Den", suggestion.getRoom().getCardName());
	}

	@Test
	public void testCreateSuggestion_OnlyOneUnseenWeaponOrPerson() {
		compPlayer.getSeenCards().clear();
        compPlayer.getMyCards().clear();

        List<Card> allCards = board.getAllCards();
        Card unseenWeapon = null;
        Card unseenPerson = null;

        for (Card c : allCards) {
            if (c.getCardType() == CardType.WEAPON && unseenWeapon == null) {
                unseenWeapon = c;
            } 
            else if (c.getCardType() == CardType.PERSON && unseenPerson == null) {
                unseenPerson = c;
            } 
            else {
                compPlayer.seeCard(c);
            }
        }

        Solution suggestion = compPlayer.createSuggestion(board);

        assertEquals(unseenWeapon.getCardName(), suggestion.getWeapon().getCardName());
        assertEquals(unseenPerson.getCardName(), suggestion.getPerson().getCardName());
        assertEquals("Den", suggestion.getRoom().getCardName());
	}

	@Test
	public void testCreateSuggestion_MultipleUnseen_PicksRandomly() {
		compPlayer.getSeenCards().clear();
        compPlayer.getMyCards().clear();

        Set<String> peopleChosen = new HashSet<>();
        Set<String> weaponsChosen = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            Solution s = compPlayer.createSuggestion(board);
            peopleChosen.add(s.getPerson().getCardName());
            weaponsChosen.add(s.getWeapon().getCardName());
        }

        // Expect at least some variety
        assertTrue(peopleChosen.size() > 1);
        assertTrue(weaponsChosen.size() > 1);
	}
}
