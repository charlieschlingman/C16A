package clueGame;

import java.util.HashSet;
import java.util.Set;

public class Solution {
	
	private Card room;
	private Card weapon;
	private Card person;

	public Solution (Card room, Card weapon, Card person) {
		this.room = room;
		this.weapon = weapon;
		this.person = person;
	}
	
	public Set<Card> theAnswer() {
		Set<Card> theAnswer = new HashSet<>();
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
