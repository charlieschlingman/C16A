package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.*;

public class Player {

	private String name;
	private Color playerColor;
	private String playerType;
	protected Integer row;
	protected Integer col;
	protected List<Card> myCards;
	
	//Constructor
	public Player(String name, Color playerColor, String playerType, Integer row, Integer col) {
		this.name = name;
		this.playerColor = playerColor;
		this.playerType = playerType;
		this.row = row;
		this.col = col;
		myCards = new ArrayList<>();
		
	}
	
	
	public Card disproveSuggestion(List<Card> suggestion) {
		// Get a list of the cards that are matching (in case there are multiple)
		List<Card> matching = new ArrayList<Card>();
		//For each card in the suggestion, check to see if it is in the cards the player has
		for (Card card: suggestion) {
			if (myCards.contains(card)) { matching.add(card); }
		}
		//If there are more than one card, choose a random card to pick
		Random rand = new Random();
		if (matching.size() != 0){
			int showingCard = rand.nextInt(matching.size());
			return matching.get(showingCard);
		} else {
			return null;
		}
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
	
	// Draw Player
	public void draw(Graphics g, int cellSize) {
	    int x = col * cellSize;
	    int y = row * cellSize;

	    g.setColor(playerColor);
	    g.fillOval(x, y, cellSize, cellSize);
	    g.setColor(Color.BLACK);
	    g.drawOval(x, y, cellSize, cellSize);
	}
	
	
}
