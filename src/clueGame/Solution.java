package clueGame;

import java.util.ArrayList;
import java.util.List;

public class Solution {
	
	private Card room;
	private Card weapon;
	private Card person;
	
	//Constructor
	public Solution (Card room, Card weapon, Card person) {
		this.room = room;
		this.weapon = weapon;
		this.person = person;
	}
	//Returns the answer of the current game
	public List<Card> theAnswer() {
		List<Card> theAnswer = new ArrayList<>();
		theAnswer.add(room);
		theAnswer.add(weapon);
		theAnswer.add(person);
		
		return theAnswer;
	}
	//Gets the room of the answer
	public Card getRoom() {
		return room;
	}
	//Gets the weapon of the answer
	public Card getWeapon() {
		return weapon;
	}
	//Gets the person of the answer
	public Card getPerson() {
		return person;
	}
	
}
