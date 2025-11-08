package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Player {

	private String name;
	private Color playerColor;
	private String playerType;
	private Integer row;
	private Integer col;
	private List<Card> myCards;
	
	//Constructor
	public Player(String name, Color playerColor, String playerType, Integer row, Integer col) {
		this.name = name;
		this.playerColor = playerColor;
		this.playerType = playerType;
		this.row = row;
		this.col = col;
		myCards = new ArrayList<>();
		
	}
	//Adds a card to the players hand
	public void addCard(Card card) {
		myCards.add(card);
	}
	//Gets the row the player is at
	public Integer getRow() {
		return row;
	}
	//Gets the column the player is at
	public Integer getCol() {
		return col;
	}
	//Gets the name of the player
	public String getName() {
		return name;
	}
	//Gets the color of the player
	public Color getColor() {
		return playerColor;
	}
	//Gets the type of player that the player is
	public String getPlayerType() {
		return playerType;
	}
	//Gets the cards that are in the players hand
	public List<Card> getMyCards() {
		return myCards;
	}
	

	
	
	
}
