package clueGame;

public class Card {

	private CardType cardType;
	private String cardName;
	
	//Constructor
	public Card(CardType cardType, String cardName) {
		this.cardType = cardType;
		this.cardName = cardName;
		
	}

	//Getter for Card Type
	public CardType getCardType() {
		return cardType;
	}

	//Getter for Card Name
	public String getCardName() {
		return cardName;
	}
	
	
}