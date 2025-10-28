package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row;
	private int column;
	private boolean room;
	private boolean occupied;
	private boolean roomCenter;
	private boolean label;
	private char initial;
	private char secretPassage;
	private DoorDirection doorDirection;
	private Set<BoardCell> adjacencyList;

	// Constructor
	public BoardCell(int row, int column) {
		this.row = row;
		this.column = column;
		this.adjacencyList = new HashSet<>();
		this.doorDirection = DoorDirection.NONE;
	}

	// Add this setter
	public void setInitial(char initial) {
		this.initial = initial;
	}

	// Get initial
	public char getInitial() {
		return this.initial;
	}

	// Get secretPassage
	public char getSecretPassage() {
		return this.secretPassage;
	}

	// Set the doorDirection
	public void setDoorDirection(DoorDirection dir) {
		doorDirection = dir;
	}

	// Get the doorDirection
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	// Add a cell to the adjacency list
	public void addAdjacency(BoardCell cell) {
		adjacencyList.add(cell);
	}

	// Return the adjacency list
	public Set<BoardCell> getAdjList() {
		return adjacencyList;
	}

	// Set whether the cell is part of a room or not
	public void setRoom(boolean room) {
		this.room = room;
	}
	

	// Get whether the cell is part of a room or not
	public boolean room() {
		return room;
	}

	// Set whether the cell is occupied or not
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	// Get whether the cell is occupied or not
	public boolean getOccupied() {
		return occupied;
	}

	// Set secret passage
	public void setSecretPassage(char ch) {
		this.secretPassage = ch;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	// Manage cell type
	public void setRoomCenter(boolean val) { 
		this.roomCenter = val;
	}
	public boolean isRoomCenter() {
		return roomCenter;
	}

	public void setLabel(boolean val) {
		this.label = val;
	}
	public boolean isLabel() { 
		return label; 
	}

	public boolean isDoorway() {
		return doorDirection != DoorDirection.NONE;
	}

	// Override toString
    @Override
    public String toString() {
        return "[" + row + ", " + column + "]";
    }
	

}