package clueGame;

public class Card {

	private CardType cardType;
	private String cardName;

	public Card(CardType cardType, String cardName) {
		this.cardType = cardType;
		this.cardName = cardName;
		
	}

	public CardType getCardType() {
		return cardType;
	}

	public String getCardName() {
		return cardName;
	}
	
	
}