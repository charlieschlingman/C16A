package clueGame;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;


public class Board {
	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private Set<BoardCell> doors;
	private int roomCount;
	private int doorCount;


	/*
	 * variable and methods used for singleton pattern
	 */
	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {
		super() ;
	}
	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}
	/*
	 * initialize the board (since we are using singleton pattern)
	 */
	public void initialize()
	{
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} 
		catch (BadConfigFormatException e) {
			System.err.println("Configuration error: " + e.getMessage());
		}
	}

	// Sets configuration files
	public void setConfigFiles(String csv, String txt) {
		this.layoutConfigFile = "data/" + csv;
		this.setupConfigFile = "data/" + txt;
	}

	// Load configuration files
	public void loadSetupConfig() throws BadConfigFormatException {
		roomMap = new HashMap<>();

		try (Scanner in = new Scanner(new File(setupConfigFile))) {
			while (in.hasNextLine()) {
				String line = in.nextLine().trim();
				if (line.isEmpty() || line.startsWith("//")) continue;

				String[] parts = line.split(",");
				if (parts.length != 3) {
					throw new BadConfigFormatException("Invalid setup line: " + line);
				}

				String type = parts[0].trim();
				String name = parts[1].trim();
				char initial = parts[2].trim().charAt(0);

				if (!type.equals("Room") && !type.equals("Space")) {
					throw new BadConfigFormatException("Unknown room type: " + type);
				}

				roomMap.put(initial, new Room(name, null, null));
			}
		} 
		catch (FileNotFoundException e) {
			System.err.println("Setup file not found: " + setupConfigFile);
		}
	}

	public void loadLayoutConfig() throws BadConfigFormatException {
		
		// Save all the lines from the file to an arrayList
		List<String[]> lines = new ArrayList<>();

		// Add all the lines from the file to the list
		try (Scanner in = new Scanner(new File(layoutConfigFile))) {
			while (in.hasNextLine()) {
				String line = in.nextLine().trim();
				if (line.isEmpty()) continue;
				lines.add(line.split(","));
			}
		} 
		catch (FileNotFoundException e) {
			System.err.println("Layout file not found: " + layoutConfigFile);
			return;
		}

		// Set the parameters of the board based on the inputed lines
		numRows = lines.size();
		numColumns = lines.get(0).length;

		// Validate consistent column counts
		for (String[] row : lines) {
			if (row.length != numColumns) {
				throw new BadConfigFormatException("Inconsistent column count in layout file.");
			}
		}
		
		setUpBoard(lines);
	}
	
	
	// Sets up the full board, assigning proper cells the proper characteristics
	private void setUpBoard(List<String[]> lines) throws BadConfigFormatException {

		// Set up the grid, as well as room and door counts, unique rooms, and the door locations
		grid = new BoardCell[numRows][numColumns];
		roomCount = 0;
		doorCount = 0;
		Set<Character> uniqueRooms = new HashSet<>();
		doors = new HashSet<>();

		
		// Loop through the amount of rows and columns, checking to see if a cell has a certain characteristic
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numColumns; c++) {
				String cellCode = lines.get(r)[c].trim();
				if (cellCode.length() == 0) {
					throw new BadConfigFormatException("Empty cell at (" + r + "," + c + ")");
				}

				char initial = cellCode.charAt(0);
				if (!roomMap.containsKey(initial)) {
					throw new BadConfigFormatException("Invalid room initial: " + initial);
				}

				// Set the characteristics of the cell based on cell character setup/letters
				BoardCell cell = new BoardCell(r, c);
				cell.setInitial(initial);
				cell.setRoom(initial != 'W' && initial != 'X');
				cell.setOccupied(false);
				cell.setRoomCenter(false);
				setDoorAttributes(cell, cellCode);
				setSpecialCells(cell, cellCode, initial);

				grid[r][c] = cell;

				// Count rooms and doors for testing
				if (cell.room()) uniqueRooms.add(initial);
				if (cell.isDoorway()) doorCount++;
			}
		}
		roomCount = uniqueRooms.size();
		calcAdjacencies();
	}

	// Assign door directions
	private void setDoorAttributes(BoardCell cell, String code) {
		cell.setDoorDirection(DoorDirection.NONE);
		if (code.length() > 1) {
			char symbol = code.charAt(1);
			switch (symbol) {
			case '^':
				cell.setDoorDirection(DoorDirection.UP);
				doors.add(cell);
				break;
			case 'v':
				cell.setDoorDirection(DoorDirection.DOWN);
				doors.add(cell);
				break;
			case '<':
				cell.setDoorDirection(DoorDirection.LEFT);
				doors.add(cell);
				break;
			case '>':
				cell.setDoorDirection(DoorDirection.RIGHT);
				doors.add(cell);
				break;
			default:
				break;
			}
		}
	}

	// Assign Special Cells
	private void setSpecialCells(BoardCell cell, String code, char initial) {
		if (code.length() > 1) {
			char symbol = code.charAt(1);
			switch (symbol) {
			case '*':
				cell.setRoomCenter(true);
				roomMap.get(initial).setCenterCell(cell);
				break;
			case '#':
				cell.setLabel(true);
				roomMap.get(initial).setLabelCell(cell);
				break;
			default:
				break;
			}
		}
		if (code.length() == 2) {
			char secondChar = code.charAt(1);
			if (Character.isLetter(secondChar)) {
				cell.setSecretPassage(secondChar);
				Room room = roomMap.get(initial);
				room.setSecretPassage(secondChar);
			}
		}
	}

	private void calcAdjacencies() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				BoardCell cell = grid[row][col];
				Set<BoardCell> adj = new HashSet<>();

				// Skip non-walkway cells unless they are doorways or centers
				if (!cell.isWalkway() && !cell.isDoorway() && !cell.isRoomCenter()) {
					cell.getAdjList().clear();
					continue;
				}
				if (cell.isRoomCenter()) {
					Room room = getRoom(cell);
					if (room.getSecretPassage() != 0) {
						Room destRoom = roomMap.get(room.getSecretPassage());
						BoardCell destRoomCell = destRoom.getCenterCell();
						adj.add(destRoomCell);
					}
					Set<BoardCell> addDoors = getRoomDoors(cell.getInitial());
					for (BoardCell cells : addDoors) {
						adj.add(cells);
					}
				}

				// Check each of the four directions
				addAdjacencyIfValid(adj, row - 1, col, DoorDirection.UP, cell);  // Up
				addAdjacencyIfValid(adj, row + 1, col, DoorDirection.DOWN, cell);    // Down
				addAdjacencyIfValid(adj, row, col - 1, DoorDirection.LEFT, cell); // Left
				addAdjacencyIfValid(adj, row, col + 1, DoorDirection.RIGHT, cell);  // Right

				// Store it
				cell.getAdjList().clear();
				cell.getAdjList().addAll(adj);
			}
		}
	}

	//private boolean isWalkway(BoardCell cell) {
	//	return cell.getInitial() == 'W';
	//}

	private void addAdjacencyIfValid(Set<BoardCell> adj, int row, int col, DoorDirection neededDoorDir, BoardCell origin) {
		if (row < 0 || row >= numRows || col < 0 || col >= numColumns) return;

		BoardCell other = grid[row][col];
		
		// Handle walkways
		if (other.isWalkway()) {
			adj.add(other);
		}
		// Handle doors
		else if (other.isDoorway()) {
			adj.add(other);
		}
		// Handle doorway cells themselves
		else if (origin.isDoorway() && origin.getDoorDirection() == neededDoorDir) {
			BoardCell thisRoom = other;
			thisRoom.getInitial();
			Room doorRoom = roomMap.get(thisRoom.getInitial());
			BoardCell centerCell = doorRoom.getCenterCell();
			adj.add(centerCell);
		}
	}

	private Set<BoardCell> getRoomDoors(char roomInitial) {
		Set<BoardCell> roomDoors = new HashSet<>();
		for (BoardCell door : doors) {
			BoardCell roomCell = grid[door.getRow()][door.getColumn()];
			switch(door.getDoorDirection()) {
			case UP:
				roomCell = grid[door.getRow() - 1][door.getColumn()];
				break;
			case DOWN:
				roomCell = grid[door.getRow() + 1][door.getColumn()];
				break;
			case LEFT:
				roomCell = grid[door.getRow()][door.getColumn() - 1];
				break;
			case RIGHT:
				roomCell = grid[door.getRow()][door.getColumn() + 1];
				break;
			default:
				break;
			}
			if (roomInitial == roomCell.getInitial()) {
				roomDoors.add(door);
			}
		}
		return roomDoors;
	}

	// Getters for numRows and numCols
	public int getNumRows() {
		return this.numRows;
	}

	public int getNumColumns() {
		return this.numColumns;
	}

	// Get the room
	public Room getRoom(char roomInitial) {
		Room room = roomMap.get(roomInitial);
		if (room == null) {
			return new Room("Test", null, null);
		}
		return room;
	}

	public Room getRoom(BoardCell cell) {
		char initial = cell.getInitial(); // Assumes BoardCell has this method
		return roomMap.get(initial);
	}

	// Gets adjacency list
	public Set<BoardCell> getAdjList(int row, int col) {
		return grid[row][col].getAdjList();
	}


	// Returns cell at row and col
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}

	// Start target calculation
	public void calcTargets(BoardCell startCell, int pathlength) {
		targets = new HashSet<>();
		visited = new HashSet<>();
		visited.add(startCell);
		findAllTargets(startCell, pathlength);
	}

	// Target calculation method
	private void findAllTargets(BoardCell thisCell, int numSteps) {
		for (BoardCell adjCell : thisCell.getAdjList()) {
			// Skip visited cells
			if (visited.contains(adjCell))
				continue;

			// Skip occupied doorways if in room
			if (adjCell.getOccupied() && thisCell.isRoomCenter())
				continue;

			visited.add(adjCell);

			// If it's a room center, we can always enter it and stop there
			if (adjCell.isRoomCenter()) {
				targets.add(adjCell);
			}
			// If it's a doorway that leads into a room, allow entering the room
			else if (adjCell.isDoorway()) {
				Room room = getRoom(adjCell);
				BoardCell center = room.getCenterCell();
				if (center != null) {
					targets.add(center);
				}
				// You can step onto the doorway if it’s in range
				if (numSteps == 1) {
					targets.add(adjCell);
				} 
				else {
					findAllTargets(adjCell, numSteps - 1);
				}
			}
			// Skip occupied cells
			if (adjCell.getOccupied())
				continue;

			if (adjCell.room()) {
				targets.add(adjCell);
			} 
			else if (numSteps == 1) {
				targets.add(adjCell);
			} 
			else {
				findAllTargets(adjCell, numSteps - 1);
			}

			visited.remove(adjCell);
		}
	}

	// Getter for targets (returns empty set if not initialized)
	public Set<BoardCell> getTargets() {
		if (targets != null) {
			return targets;
		}
		else {
			return new HashSet<>();
		}
	}

	// Getter for Room Count
	public int getRoomCount() {
		return roomCount;
	}

	// Getter for Door Count
	public int getDoorCount() {
		return doorCount;
	}

	public Set<BoardCell> getDoors() {
		return doors;
	}
}