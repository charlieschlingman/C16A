package clueGame;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {

    // Singleton
    private static BoardPanel instance = new BoardPanel();
    public static BoardPanel getInstance() {
    	return instance;
    }

    // Layout fields
    private BoardCell[][] grid;
    private int numRows;
    private int numCols;
    private static final Map<String, Color> colorMap = new HashMap<>();

    // Room mapping "M → Master Bedroom"
    private Map<Character, String> rooms = new HashMap<>();

    // Players
    private ArrayList<Player> players = new ArrayList<>();

    private static final String SETUP_FILE = "data/ClueSetup.txt";
    private static final String LAYOUT_FILE = "data/ClueLayout.csv";

    private BoardPanel() {
        setBackground(Color.GRAY);
    }

    static {
        colorMap.put("red", Color.RED);
        colorMap.put("blue", Color.BLUE);
        colorMap.put("orange", Color.ORANGE);
        colorMap.put("green", Color.GREEN);
        colorMap.put("green", Color.YELLOW);
        colorMap.put("green", Color.WHITE);
    }

    public static Color getColorFromName(String colorName) {
        return colorMap.getOrDefault(colorName.toLowerCase(), Color.BLACK); // Default to black if not found
    }

    // Public initializer
    public void initialize() {
        try {
            loadSetupConfig();
            loadLayoutConfig();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Board initialization failed.");
        }
    }

    // --------------------------
    // LOAD SETUP FILE
    // --------------------------
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
                    players.add(new Player(name, color, "Computer", startRow, startCol));
                    break;
            }
        }

        in.close();
    }

    // --------------------------
    // LOAD LAYOUT FILE
    // --------------------------
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
                grid[r][c] = new BoardCell(r, c, code);
            }
        }
    }

    // --------------------------
    // DRAWING
    // --------------------------
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Determine cell size based on panel size
        int cellWidth = getWidth() / numCols;
        int cellHeight = getHeight() / numRows;
        int cellSize = Math.min(cellWidth, cellHeight);

        // Draw board cells
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                grid[r][c].draw(g, cellSize);
            }
        }

        // Draw room names AFTER cells
        drawRoomLabels(g, cellSize);

        // Draw players
        for (Player p : players) {
            p.draw(g, cellSize);
        }
    }

    private void drawRoomLabels(Graphics g, int cellSize) {
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.BOLD, cellSize));

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                BoardCell cell = grid[r][c];

                if (cell.isLabel()) {
                    String label = rooms.get(cell.getInitial());
                    int x = c * cellSize;
                    int y = r * cellSize;

                    g.drawString(label, x, y);
                }
            }
        }
    }

    // --------------------------
    // PUBLIC ACCESSORS
    // --------------------------
    public BoardCell getCell(int r, int c) {
        return grid[r][c];
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
