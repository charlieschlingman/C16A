package tests;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class FileInitTests {
	// Constants to test for
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 25;
	public static final int NUM_COLUMNS = 24;
	
	// Create one instance of board 
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Set Up the board to make sure that all cases are ready to test
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}
	
	// Test a majority of the rooms and their respective character representations, as well as only 9 rooms being generated
	@Test
	public void testRoomLabels() {
		assertEquals("Master Bedroom", board.getRoom('M').getName());
		assertEquals("Guest Bedroom", board.getRoom('G').getName());
		assertEquals("Restroom", board.getRoom('R').getName());
		assertEquals("Den", board.getRoom('D').getName());
		assertEquals("Patio", board.getRoom('P').getName());
		assertEquals("Closet", board.getRoom('C').getName());
		assertEquals("Barroom", board.getRoom('B').getName());
		assertEquals("Arcade", board.getRoom('A').getName());
		assertEquals("Office", board.getRoom('O').getName());
		assertEquals(board.getRoomCount(), 9);
	}
	
	// Make sure the amount of rows and columns are correct
	@Test 
	public void testBoardSize() {
		assertEquals(board.getNumRows(), 26);
		assertEquals(board.getNumColumns(), 24);
	}
	
	// Test doorways to make sure they are returning the right values
	@Test
	public void testDoorways() {
		BoardCell cell = board.getCell(6, 6);
		assertTrue(cell.isDoorway());
		assertEquals(cell.getDoorDirection(), DoorDirection.LEFT);
		cell = board.getCell(18, 11);
		assertTrue(cell.isDoorway());
		assertEquals(cell.getDoorDirection(), DoorDirection.DOWN);
		cell = board.getCell(8, 4);
		assertTrue(cell.isDoorway());
		assertEquals(cell.getDoorDirection(), DoorDirection.UP);
		cell = board.getCell(12, 9);
		assertTrue(cell.isDoorway());
		assertEquals(cell.getDoorDirection(), DoorDirection.RIGHT);
		cell = board.getCell(12, 13);
		assertTrue(cell.isDoorway());
		assertEquals(cell.getDoorDirection(), DoorDirection.LEFT);
		cell = board.getCell(18, 19);
		assertTrue(cell.isDoorway());
		assertEquals(cell.getDoorDirection(), DoorDirection.DOWN);
		cell = board.getCell(18, 8);
		assertFalse(cell.isDoorway());
		assertEquals(board.getDoors().size(), 14);
	}
	
	// Tests to make sure all rooms have the right center
	@Test
	public void testCenters() {
		BoardCell cell = board.getCell(3, 2);
		Room room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Den");
		assertFalse(cell.isLabel());
		assertTrue(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		
		cell = board.getCell(22, 2);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Office");
		assertFalse(cell.isLabel());
		assertTrue(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		
		cell = board.getCell(13, 1);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Restroom");
		assertFalse(cell.isLabel());
		assertTrue(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		
		cell = board.getCell(2, 11);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Master Bedroom");
		assertFalse(cell.isLabel());
		assertTrue(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		
		cell = board.getCell(22,11);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Guest Bedroom");
		assertFalse(cell.isLabel());
		assertTrue(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		
		cell = board.getCell(1,11);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Master Bedroom");
		assertTrue(cell.isLabel());
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		
		cell = board.getCell(21, 20);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Arcade");
		assertTrue(cell.isLabel());
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isDoorway());
		assertEquals(room.getSecretPassage(), 'D');
	}
	
	

}
