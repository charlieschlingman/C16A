package clueGame;

import java.awt.Color;
import java.util.*;

public class ComputerPlayer extends Player {

	private Random rand = new Random();
	private List<Card> seenCards;

	// Constructor
	public ComputerPlayer(String name, Color playerColor, String playerType, Integer row, Integer col) {
		super(name, playerColor, playerType, row, col);
	}

	// Update the seenCards list
	public void seeCard(Card newCard) {
		seenCards.add(newCard);
	}

	// Create suggestion based on current room and unseen cards
	public Solution createSuggestion(Board board) {
		// Get current room name
		BoardCell currentCell = board.getCell(row, col);
		Room currentRoom = board.getRoom(currentCell);
		Card roomCard = new Card(CardType.ROOM, currentRoom.getName());

		// Separate unseen people and weapons
		List<Card> unseenPeople = new ArrayList<>();
		List<Card> unseenWeapons = new ArrayList<>();

		for (Card c : board.getAllCards()) {
			if (!myCards.contains(c) && !seenCards.contains(c)) {
				if (c.getCardType() == CardType.PERSON) {
					unseenPeople.add(c);
				} 
				else if (c.getCardType() == CardType.WEAPON) {
					unseenWeapons.add(c);
				}
			}
		}

		// Randomly pick one unseen person and weapon
		Card person = unseenPeople.get(rand.nextInt(unseenPeople.size()));
		Card weapon = unseenWeapons.get(rand.nextInt(unseenWeapons.size()));

		return new Solution(person, roomCard, weapon);
	}

	// Computer player selects target
	public BoardCell selectTargetAI(Set<BoardCell> targets, Board board) {
		List<BoardCell> roomTargets = new ArrayList<>();

		for (BoardCell target : targets) {
			if (target.isRoomCenter()) {
				Room room = board.getRoom(target);
				Card roomCard = new Card(CardType.ROOM, room.getName());
				if (!seenCards.contains(roomCard)) {
					roomTargets.add(target);
				}
			}
		}

		if (!roomTargets.isEmpty()) {
			return roomTargets.get(rand.nextInt(roomTargets.size()));
		}

		// Otherwise pick any random target
		List<BoardCell> allTargets = new ArrayList<>(targets);
		return allTargets.get(rand.nextInt(allTargets.size()));
	}
}