package clueGame;

public class Room {
    private String name;
    private char SecretPassage;
    private BoardCell centerCell;
    private BoardCell labelCell;

    // Default constructor
    public Room() {
        this.name = "";
        this.centerCell = null;
        this.labelCell = null;
    }

    // Parameterized constructor
    public Room(String name, BoardCell centerCell, BoardCell labelCell) {
        this.name = name;
        this.centerCell = centerCell;
        this.labelCell = labelCell;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for centerCell
    public BoardCell getCenterCell() {
        return centerCell;
    }

    public void setCenterCell(BoardCell centerCell) {
        this.centerCell = centerCell;
    }

    // Getter and Setter for labelCell
    public BoardCell getLabelCell() {
        return labelCell;
    }

    public void setLabelCell(BoardCell labelCell) {
        this.labelCell = labelCell;
    }

    // Getter and Setter for secretPassage
	public char getSecretPassage() {
		return SecretPassage;
	}

	public void setSecretPassage(char secretPassage) {
		this.SecretPassage = secretPassage;
	}
}