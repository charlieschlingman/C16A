package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;


public class BoardAdjTargetTest {
	
	private static Board board;
	
	
	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
	}
	
	
	@Test
	public void testAdjacenciesRooms()	{
		// Test the adjacency of a room with a secret passage
		Set<BoardCell> testList = board.getAdjList(3, 2);
		assertEquals(testList.size(), 3);
		assertTrue(testList.contains(board.getCell(22, 20)));
		assertTrue(testList.contains(board.getCell(8, 4)));
		assertTrue(testList.contains(board.getCell(6, 6)));
		
		// Test the adjacency of a room with no passage and one entrance
		testList = board.getAdjList(2, 11);
		assertEquals(testList.size(), 1);
		assertTrue(testList.contains(board.getCell(7, 11)));
		
		// Test the adjacency of a room with 2 doors
		testList = board.getAdjList(12, 11);
		assertEquals(testList.size(), 2);
		assertTrue(testList.contains(board.getCell(12, 9)));
		assertTrue(testList.contains(board.getCell(12, 13)));
		
		// Tests the adjacency of a room that is not the center (should be empty)
		testList = board.getAdjList(6, 15);
		assertEquals(testList.size(), 0);
	}
	
	
	// Test the adjacency of different doorways
	@Test
	public void testAdjacenciesDoor() {
		Set<BoardCell> testList = board.getAdjList(5, 17);
		assertEquals(testList.size(), 4);
		assertTrue(testList.contains(board.getCell(4, 17)));
		assertTrue(testList.contains(board.getCell(6, 17)));
		assertTrue(testList.contains(board.getCell(4, 16)));
		assertTrue(testList.contains(board.getCell(3, 20)));
		
		testList = board.getAdjList(20, 6);
		assertEquals(testList.size(), 4);
		assertTrue(testList.contains(board.getCell(19, 6)));
		assertTrue(testList.contains(board.getCell(21, 6)));
		assertTrue(testList.contains(board.getCell(20, 7)));
		assertTrue(testList.contains(board.getCell(22, 2)));
		
		testList =  board.getAdjList(13, 4);
		assertEquals(testList.size(), 4);
		assertTrue(testList.contains(board.getCell(12, 4)));
		assertTrue(testList.contains(board.getCell(14, 4)));
		assertTrue(testList.contains(board.getCell(13, 5)));
		assertTrue(testList.contains(board.getCell(13, 1)));
		
	}
	
	@Test
	public void testAdjacenciesWalkways() {
		Set<BoardCell> testList = board.getAdjList(18, 17);
		assertEquals(testList.size(), 4);
		assertTrue(testList.contains(board.getCell(17, 17)));
		assertTrue(testList.contains(board.getCell(19, 17)));
		assertTrue(testList.contains(board.getCell(18, 16)));
		assertTrue(testList.contains(board.getCell(18, 18)));
		
		testList = board.getAdjList(14, 9);
		assertEquals(testList.size(), 3);
		assertTrue(testList.contains(board.getCell(13, 9)));
		assertTrue(testList.contains(board.getCell(15, 9)));
		assertTrue(testList.contains(board.getCell(14, 8)));
		
		testList = board.getAdjList(9, 0);
		assertEquals(testList.size(), 1);
		assertTrue(testList.contains(board.getCell(9, 1)));

		testList = board.getAdjList(0, 6);
		assertEquals(testList.size(), 1);
		assertTrue(testList.contains(board.getCell(1, 6)));
		
		testList = board.getAdjList(18, 23);
		assertEquals(testList.size(), 1);
		assertTrue(testList.contains(board.getCell(18, 22)));
		
		testList = board.getAdjList(25, 7);
		assertEquals(testList.size(), 1);
		assertTrue(testList.contains(board.getCell(24, 7)));
		
		testList = board.getAdjList(8, 15);
		assertEquals(testList.size(), 3);
		assertTrue(testList.contains(board.getCell(7, 15)));
		assertTrue(testList.contains(board.getCell(8, 14)));
		assertTrue(testList.contains(board.getCell(8, 16)));
		
		testList = board.getAdjList(7, 8);
		assertEquals(testList.size(), 3);
		assertTrue(testList.contains(board.getCell(7, 7)));
		assertTrue(testList.contains(board.getCell(8, 8)));
		assertTrue(testList.contains(board.getCell(7, 9)));
		
	}
	

}
