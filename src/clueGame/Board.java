package clueGame;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Color;



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
	private List<Player> players;
	private List<String> weapons;
	private List<Card> allCards;
	private List<Card> allDealableCards;
	private List<Card> weaponCards;
	private List<Card> roomCards;
	private List<Card> personCards;
	private List<Card> theAnswer;
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
		
		// Mark starting cells as occupied
		for (Player p : players) {
            BoardCell start = getCell(p.getRow(), p.getCol());
            start.setOccupied(true);
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
		players = new ArrayList<>();
		weapons = new ArrayList<>();
		allCards = new ArrayList<>();
		weaponCards = new ArrayList<>();
		roomCards = new ArrayList<>();
		personCards = new ArrayList<>();
		Card thisCard = new Card(null, null);
		

		try (Scanner in = new Scanner(new File(setupConfigFile))) {
			while (in.hasNextLine()) {
				String line = in.nextLine().trim();
				if (line.isEmpty() || line.startsWith("//")) continue;

				String[] parts = line.split(",");

				String type = parts[0].trim();
				
				// If not of correct type, then throws error
				if (!type.equals("Room") && !type.equals("Space") && !type.equals("Player") && !type.equals("Weapon")) {
					throw new BadConfigFormatException("Unknown room type: " + type);
				}
				
				// Handles rooms, if it is a room, it adds it to the room cards list
				if (type.equals("Room") || type.equals("Space")) {
					if (parts.length != 3) {
						throw new BadConfigFormatException("Invalid setup line: " + line);
					}
					String name = parts[1].trim();
					char initial = parts[2].trim().charAt(0);
					roomMap.put(initial, new Room(name, null, null));
					if (type.equals("Room")) {
						thisCard = new Card(CardType.ROOM, name);
						allCards.add(thisCard);
						roomCards.add(thisCard);
					}
				}
				
				// Handles players, assigns color, type, and name, as well as adding it to the player card list
				if (type.equals("Player")) {
					if (parts.length != 6) {
						throw new BadConfigFormatException("Invalid setup line: " + line);
					}
					String colorString = parts[1].trim();
					Color playerColor = new Color(0);
					switch (colorString) {
					case "Red":
						playerColor = Color.red;
						break;
					case "Blue":
						playerColor = Color.blue;
						break;
					case "Orange":
						playerColor = Color.orange;
						break;
					case "Green":
						playerColor = Color.green;
						break;
					case "Yellow":
						playerColor = Color.yellow;
						break;
					case "White":
						playerColor = Color.white;
						break;
					default:
						throw new BadConfigFormatException("Unknown color: " + colorString);
					}
					
					String playerType = parts[2].trim();
					String name = parts[3].trim();
					
					String playerRow = parts[4].trim();
					String playerCol = parts[5].trim();
					int playerRowInt = Integer.parseInt(playerRow);
					int playerColInt = Integer.parseInt(playerCol);

					
					Player player = new Player(name, playerColor, playerType, playerRowInt, playerColInt);
					players.add(player);
					thisCard = new Card(CardType.PERSON, name);
					allCards.add(thisCard);
					personCards.add(thisCard);
					
				}
				
				// Handles weapons, adds it to the weapon card list
				if (type.equals("Weapon")) {
					if (parts.length != 2) {
						throw new BadConfigFormatException("Invalid setup line: " + line);
					}
					String weapon = parts[1].trim();
					weapons.add(weapon);
					thisCard = new Card(CardType.WEAPON, weapon);
					allCards.add(thisCard);
					weaponCards.add(thisCard);
					
				}
				
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
		deal();
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
	
	public void deal() {
		//Deals with the case of no cards being instantiated (in the case of the 306 tests to make them pass)
		if (weaponCards.size() == 0) {
			return;
		}
		// Starts by taking a random number for the room card, the weapon card, and the person card
		theAnswer = new ArrayList<>();
		Random rand = new Random();
		int answerRoomNum = rand.nextInt(roomCards.size());
		int answerWeaponNum = rand.nextInt(weaponCards.size());
		int answerPersonNum = rand.nextInt(personCards.size());
		
		Card answerRoom = roomCards.get(answerRoomNum);
		Card answerWeapon = weaponCards.get(answerWeaponNum);
		Card answerPerson = personCards.get(answerPersonNum);
		
		// Then takes those 3 cards and stores it the answer as a Solution
		Solution answer = new Solution(answerRoom, answerWeapon, answerPerson);
		
		// Gets a callable answer so we can compare it to an accusation
		theAnswer = answer.theAnswer();
		
		// Removes the cards that were picked from the dealable cards, leaving the total amount of cards intact
		allDealableCards = new ArrayList<>(allCards);
		
		allDealableCards.remove(answerRoom);
		allDealableCards.remove(answerPerson);
		allDealableCards.remove(answerWeapon);
		
		int playerNum = 0;
		
		// Cycle through the cards, dealing them out to each player, and then removing it from the cards that can be dealable
		while (!allDealableCards.isEmpty()) {
			int thisCardNum = rand.nextInt(allDealableCards.size());			
			Card givingCard = allDealableCards.get(thisCardNum);
			Player recievingPlayer = players.get(playerNum);
			recievingPlayer.addCard(givingCard);
			playerNum++;
			if (playerNum >= players.size()) { playerNum = 0; }
			allDealableCards.remove(thisCardNum);
		}
		
	}
	
	
	public boolean testAccusation(Card room, Card weapon, Card person) throws Exception {
		// Handle the cases where the user inputs the wrong type for the cards
		if (room.getCardType() != CardType.ROOM) {
			throw new Exception("Invalid Room Card");
		}
		if (weapon.getCardType() != CardType.WEAPON) {
			throw new Exception("Invalid Weapon Card");
		}
		if (person.getCardType() != CardType.PERSON) {
			throw new Exception("Invalid Person Card");
		}
		
		// Add the guess to an array list
		List<Card> guess = new ArrayList<Card>();
		
		guess.add(room);
		guess.add(weapon);
		guess.add(person);
		
		// If the guess is equal to the answer, then return true, otherwise, return false
		if (guess.equals(theAnswer)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public Card makeSuggestion(Card room, Card weapon, Card person) throws Exception {
		// Handle cases where the user inputs the wrong type for the cards
		if (room.getCardType() != CardType.ROOM) {
			throw new Exception("Invalid Room Card");
		}
		if (weapon.getCardType() != CardType.WEAPON) {
			throw new Exception("Invalid Weapon Card");
		}
		if (person.getCardType() != CardType.PERSON) {
			throw new Exception("Invalid Person Card");
		}
		
		// Add the suggestion to an array list
		List<Card> suggestion = new ArrayList<Card>();
		
		suggestion.add(room);
		suggestion.add(weapon);
		suggestion.add(person);
		
		int playerNum = 0;
		
		// Cycle through the players, on each player, try to disprove the suggestion, if the suggestion returns a card, return that card, otherwise, keep
		// looping through the players until all the players are looped through, and if no cards are returned, return null
		for (Player player: players) {
			Player currentPlayer = players.get(playerNum);
			if (currentPlayer.disproveSuggestion(suggestion) != null) {
				return currentPlayer.disproveSuggestion(suggestion);
			}
			playerNum++;
		}
		return null;
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
	//Getter for Players
	public List<Player> getPlayers() {
		return players;
	}
	//Getter for the player given a name
	public Player getThisPlayer(String name) {
		Player thisPlayer = new Player("Test", null, null, null, null);
		for (Player player: players) {
			if (player.getName().equals(name)) {
				thisPlayer = player;
			}
		}
		return thisPlayer;
	}
	//Getter for the player given a color
	public Player getThisPlayer(Color color) {
		Player thisPlayer = new Player("Test", null, null, null, null);
		for (Player player: players) {
			if (player.getColor().equals(color)) {
				thisPlayer = player;
			}
		}
		return thisPlayer;
	}
	//Getter for the weapons
	public List<String> getWeapons() {
		return weapons;
	}
	//Getter for the weapon given a string
	public String getWeapon(String inputWeapon) {
		String thisWeapon = new String();
		for (String weapon: weapons) {
			if (weapon.equals(inputWeapon)) {
				thisWeapon = weapon;
			}
		}
		return thisWeapon;
	}
	//Getter for all of the cards
	public List<Card> getAllCards() {
		return allCards;
	}
	//Getter for all of the weapon cards
	public List<Card> getWeaponCards() {
		return weaponCards;
	}
	//Getter for all of the room cards
	public List<Card> getRoomCards() {
		return roomCards;
	}
	//Getter for all of the person cards
	public List<Card> getPersonCards() {
		return personCards;
	}
	//Getter for the answer
	public List<Card> getTheAnswer() {
		return theAnswer;
	}
	//Getter for all of the dealable cards
	public List<Card> getAllDealableCards() {
		return allDealableCards;
	}
	//Setter for theAnswer (used for tests)
	public void setTheAnswer(List<Card> theAnswer) {
	    this.theAnswer.clear();
	    this.theAnswer.addAll(theAnswer);
	}
	//Setter for the Players List (used for tests)
	public void setPlayers(List<Player> players) {
	    this.players.clear();
	    this.players.addAll(players);
	}

	
	


	
	
}