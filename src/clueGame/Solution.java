package clueGame;

import java.util.ArrayList;
import java.util.List;

public class Solution {
	
	private Card room;
	private Card weapon;
	private Card person;

	public Solution (Card room, Card weapon, Card person) {
		this.room = room;
		this.weapon = weapon;
		this.person = person;
	}
	
	public List<Card> theAnswer() {
		List<Card> theAnswer = new ArrayList<>();
		theAnswer.add(room);
		theAnswer.add(weapon);
		theAnswer.add(person);
		
		return theAnswer;
	}

	public Card getRoom() {
		return room;
	}

	public Card getWeapon() {
		return weapon;
	}

	public Card getPerson() {
		return person;
	}
	
}
