package clueGame;

import java.util.HashSet;
import java.util.Set;
import java.awt.*;

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
	
	// Constructor for drawing from CSV
	public BoardCell(int row, int column, String code) {
		this.row = row;
		this.column = column;
		this.adjacencyList = new HashSet<>();
		this.doorDirection = DoorDirection.NONE;
		
		// Read from CSV
	    this.initial = code.charAt(0);

	    // Second character if needed
	    if (code.length() > 1) {
	        char flag = code.charAt(1);

	        switch (flag) {
	            case '*':
	                this.label = true;
	                break;

	            case 'U':
	                this.doorDirection = DoorDirection.UP;
	                break;

	            case 'D':
	                this.doorDirection = DoorDirection.DOWN;
	                break;

	            case 'L':
	                this.doorDirection = DoorDirection.LEFT;
	                break;

	            case 'R':
	                this.doorDirection = DoorDirection.RIGHT;
	                break;
	        }
	    }
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

	// Set secret passage
	public void setSecretPassage(char ch) {
		this.secretPassage = ch;
	}

	// Get the doorDirection
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	// Set the doorDirection
	public void setDoorDirection(DoorDirection dir) {
		doorDirection = dir;
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

	// Return true if cell is Walkway
	public boolean isWalkway() {
		return initial == 'W';
	}

	// Override toString
	@Override
	public String toString() {
		return "[" + row + ", " + column + "]";
	}

	// Draws the Cell
	public void draw(Graphics g, int cellSize) {
		int x = column * cellSize;
		int y = row * cellSize;

		if (isWalkway()) {
			g.setColor(Color.YELLOW);
			g.fillRect(x, y, cellSize, cellSize);
			g.setColor(Color.BLACK);
			g.drawRect(x, y, cellSize, cellSize);
		}
		else {
			// Room tile
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x, y, cellSize, cellSize);
		}

		// Draw door indicator
		if (isDoorway()) {
			g.setColor(Color.BLUE);
			int thickness = Math.max(4, cellSize / 8);

			switch (doorDirection) {
			case UP:
				g.fillRect(x, y, cellSize, thickness);
				break;
			case DOWN:
				g.fillRect(x, y + cellSize - thickness, cellSize, thickness);
				break;
			case LEFT:
				g.fillRect(x, y, thickness, cellSize);
				break;
			case RIGHT:
				g.fillRect(x + cellSize - thickness, y, thickness, cellSize);
				break;
			default:
				break;
			}
		}
	}

}