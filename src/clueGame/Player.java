package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class Player {

	private String name;
	private Color playerColor;
	private String playerType;
	private Integer row;
	private Integer col;
	private Set<Card> myCards;
	
	public Player(String name, Color playerColor, String playerType, Integer row, Integer col) {
		this.name = name;
		this.playerColor = playerColor;
		this.playerType = playerType;
		this.row = row;
		this.col = col;
		
	}
	
	public void addCard(Card card) {
		myCards = new HashSet<>();
		myCards.add(card);
	}

	public Integer getRow() {
		return row;
	}

	public Integer getCol() {
		return col;
	}

	public String getName() {
		return name;
	}

	public Color getColor() {
		return playerColor;
	}

	public String getPlayerType() {
		return playerType;
	}

	
	
	
}
