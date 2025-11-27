package clueGame;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	private BoardCell[][] grid;
	private int numRows;
	private int numCols;
	private static final Map<String, Color> colorMap = new HashMap<>();
	private static Board theBoard;
	private Set<BoardCell> targets = new HashSet<>();

	public interface BoardClickListener {
		void onBoardClick(int row, int col);
	}

	private BoardClickListener clickListener;

	public void setBoardClickListener(BoardClickListener listener) {
		this.clickListener = listener;
	}

	// Singleton principle
	private static BoardPanel instance = new BoardPanel();
	public static BoardPanel getInstance() {
		return instance;
	}

	// Room mapping
	private Map<Character, String> rooms = new HashMap<>();

	private static final String SETUP_FILE = "data/ClueSetup.txt";
	private static final String LAYOUT_FILE = "data/ClueLayout.csv";

	private BoardPanel() {
		setBackground(Color.GRAY);
		addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				handleClick(e);
			}
		}
				);
	}

	// Color mapping from CSV
	static {
		colorMap.put("red", Color.RED);
		colorMap.put("blue", Color.BLUE);
		colorMap.put("orange", Color.ORANGE);
		colorMap.put("green", Color.GREEN);
		colorMap.put("yellow", Color.YELLOW);
		colorMap.put("white", Color.WHITE);
	}


	public static Color getColorFromName(String colorName) {
		return colorMap.getOrDefault(colorName.toLowerCase(), Color.BLACK); // Default to black if not found
	}

	// Public initializer
	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
			theBoard = Board.getInstance();
			theBoard.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
			theBoard.initialize();
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Board initialization failed.");
		}
	}

	// Load setup file
	private void loadSetupConfig() throws FileNotFoundException {
		File file = new File(SETUP_FILE);
		Scanner in = new Scanner(file);

		while (in.hasNextLine()) {
			String line = in.nextLine().trim();
			if (line.length() == 0 || line.startsWith("//")) continue;

			String[] parts = line.split(",");

			switch (parts[0].strip()) {

			case "Room":
				String roomName = parts[1].strip();
				char initial = parts[2].strip().charAt(0);
				rooms.put(initial, roomName);
				break;

			case "Player":
				String colorName = parts[1].strip();
				Color color = getColorFromName(colorName);
				String name = parts[3].strip();
				int startRow = Integer.parseInt(parts[4].strip());
				int startCol = Integer.parseInt(parts[5].strip());
				break;
			}
		}

		in.close();
	}

	// Load layout file
	private void loadLayoutConfig() throws FileNotFoundException {
		ArrayList<String[]> lines = new ArrayList<>();

		Scanner in = new Scanner(new File(LAYOUT_FILE));

		while (in.hasNextLine()) {
			lines.add(in.nextLine().split(","));
		}
		in.close();

		numRows = lines.size();
		numCols = lines.get(0).length;

		grid = new BoardCell[numRows][numCols];

		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				String code = lines.get(r)[c].trim();
				BoardCell cell = new BoardCell(r, c);
				cell.setInitial(code.charAt(0));

				if (code.length() > 1) {
					char flag = code.charAt(1);
					switch (flag) {
					case '*':
						cell.setLabel(true);
						break;
					case '#':
						cell.setRoomCenter(true);
						break;
					case '^':
						cell.setDoorDirection(DoorDirection.UP);
						break;
					case 'v':
						cell.setDoorDirection(DoorDirection.DOWN);
						break;
					case '<':
						cell.setDoorDirection(DoorDirection.LEFT);
						break;
					case '>':
						cell.setDoorDirection(DoorDirection.RIGHT);
						break;
					}
				}

				grid[r][c] = cell;
			}
		}

	}

	// Drawing
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Determine cell size based on panel size
		int cellWidth  = getWidth()  / numCols;
		int cellHeight = getHeight() / numRows;
		int cellSize   = Math.min(cellWidth, cellHeight);

		int boardPixelWidth  = cellSize * numCols;
		int boardPixelHeight = cellSize * numRows;

		int xOffset = (getWidth()  - boardPixelWidth)  / 2;
		int yOffset = (getHeight() - boardPixelHeight) / 2;


		// Draw board cells
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				BoardCell cell = grid[r][c];
				boolean isTarget = false;
				for (BoardCell t : targets) {
					if (t.getRow() == cell.getRow() && t.getColumn() == cell.getColumn()) {
						isTarget = true;
						break;
					}
				}
				cell.draw(g, cellSize, xOffset, yOffset, isTarget);
			}
		}

		// Draw room names
		drawRoomLabels(g, cellSize, xOffset, yOffset);

		// Draw players
		if (theBoard != null) {
		    for (Player p : theBoard.getPlayers()) {
		        p.draw(g, cellSize, xOffset, yOffset);
		    }
		}
	}

	private void drawRoomLabels(Graphics g, int cellSize, int xOffset, int yOffset) {
		g.setColor(Color.BLUE);
		g.setFont(new Font("Arial", Font.BOLD, cellSize - 4));

		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				BoardCell cell = grid[r][c];

				if (cell.isLabel()) {
					String label = rooms.get(cell.getInitial());
					int offset;
					int x = xOffset + c * cellSize;
					int y = yOffset + r * cellSize;
					switch (label) {
					case ("Master Bedroom"):
						offset = (int)(cellSize * 2.2);
					x -= offset;
					g.drawString(label, x, y);
					break;
					case ("Guest Bedroom"):
						offset = (int)(cellSize * 2.1);
					x -= offset;
					g.drawString(label, x, y);
					break;
					case ("Restroom"):
						offset = (int)(cellSize * 1);
					x -= offset;
					g.drawString(label, x, y);
					break;
					case ("Den"):
						g.drawString(label, x, y);
					break;
					case ("Patio"):
						g.drawString(label, x, y);
					break;
					case ("Closet"):
						g.drawString(label, x, y);
					break;
					case ("Barroom"):
						offset = (int)(cellSize * 1.2);
					x -= offset;
					g.drawString(label, x, y);
					break;
					case ("Arcade"):
						g.drawString(label, x, y);
					break;
					case ("Office"):
						g.drawString(label, x, y);
					break;
					}

				}
			}
		}
	}

	// Handle Clicking, return grid coordinate
	private void handleClick(java.awt.event.MouseEvent e) {
		int cellWidth = getWidth() / numCols;
		int cellHeight = getHeight() / numRows;
		int cellSize = Math.min(cellWidth, cellHeight);

		int boardPixelWidth = cellSize * numCols;
		int boardPixelHeight = cellSize * numRows;

		int xOffset = (getWidth() - boardPixelWidth) / 2;
		int yOffset = (getHeight() - boardPixelHeight) / 2;

		int mouseX = e.getX();
		int mouseY = e.getY();

		// Ignore outside clicks
		if (mouseX < xOffset || mouseY < yOffset) return;

		int col = (mouseX - xOffset) / cellSize;
		int row = (mouseY - yOffset) / cellSize;

		if (row < 0 || row >= numRows || col < 0 || col >= numCols) return;

		// Send click coordinates to listener
		if (clickListener != null) {
			clickListener.onBoardClick(row, col);
		}
	}

	// Getters
	public BoardCell getCell(int r, int c) {
		return grid[r][c];
	}

	public Board getBoard() {
		return theBoard;
	}

	public void setTargets(Set<BoardCell> targets) {
		this.targets = targets;
		repaint();
	}
	public Set<BoardCell> getTargets() {
		return targets;
	}

}
