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

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsInPatio() {
		// test a roll of 1
		board.calcTargets(board.getCell(13, 21), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(12, 18)));

		// test a roll of 3
		board.calcTargets(board.getCell(12, 20), 3);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(10, 18)));
		assertTrue(targets.contains(board.getCell(14, 18)));	
		assertTrue(targets.contains(board.getCell(11, 17)));
		assertTrue(targets.contains(board.getCell(13, 17)));	

		// test a roll of 4
		board.calcTargets(board.getCell(12, 20), 4);
		targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(9, 18)));
		assertTrue(targets.contains(board.getCell(15, 18)));	
		assertTrue(targets.contains(board.getCell(10, 18)));
		assertTrue(targets.contains(board.getCell(14, 18)));	
	}

	@Test
	public void testTargetsInOffice() {
		// test a roll of 1
		board.calcTargets(board.getCell(22, 2), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(18, 4)));
		assertTrue(targets.contains(board.getCell(20, 6)));	

		// test a roll of 3
		board.calcTargets(board.getCell(22, 2), 3);
		targets= board.getTargets();
		assertEquals(18, targets.size());
		assertTrue(targets.contains(board.getCell(18, 1)));
		assertTrue(targets.contains(board.getCell(15, 4)));	
		assertTrue(targets.contains(board.getCell(23, 6)));
		assertTrue(targets.contains(board.getCell(18, 7)));	
	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(7, 19), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 20)));
		assertTrue(targets.contains(board.getCell(7, 18)));	
		assertTrue(targets.contains(board.getCell(8, 19)));	

		// test a roll of 3
		board.calcTargets(board.getCell(7, 19), 3);
		targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(3, 20)));
		assertTrue(targets.contains(board.getCell(8, 21)));
		assertTrue(targets.contains(board.getCell(9, 18)));	
		assertTrue(targets.contains(board.getCell(7, 18)));
		assertTrue(targets.contains(board.getCell(6, 17)));	

		// test a roll of 4
		board.calcTargets(board.getCell(7, 19), 4);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(3, 20)));
		assertTrue(targets.contains(board.getCell(7, 21)));
		assertTrue(targets.contains(board.getCell(10, 18)));	
		assertTrue(targets.contains(board.getCell(8, 16)));
		assertTrue(targets.contains(board.getCell(9, 17)));	
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(8, 2), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(9, 2)));
		assertTrue(targets.contains(board.getCell(8, 3)));	

		// test a roll of 3
		board.calcTargets(board.getCell(8, 2), 3);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(8, 1)));
		assertTrue(targets.contains(board.getCell(9, 2)));
		assertTrue(targets.contains(board.getCell(11, 5)));	

		// test a roll of 4
		board.calcTargets(board.getCell(8, 2), 4);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(3, 2)));
		assertTrue(targets.contains(board.getCell(9, 1)));
		assertTrue(targets.contains(board.getCell(9, 5)));	
	}

	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(25, 16), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(24, 16)));

		// test a roll of 3
		board.calcTargets(board.getCell(25, 16), 3);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(22, 16)));
		assertTrue(targets.contains(board.getCell(23, 17)));

		// test a roll of 4
		board.calcTargets(board.getCell(25, 16), 4);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(21, 16)));
		assertTrue(targets.contains(board.getCell(23, 16)));
		assertTrue(targets.contains(board.getCell(24, 17)));	
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 down
		board.getCell(23, 16).setOccupied(true);
		board.calcTargets(board.getCell(25, 16), 4);
		board.getCell(23, 7).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(22, 17)));
		assertFalse( targets.contains( board.getCell(23, 16))) ;
		assertFalse( targets.contains( board.getCell(21, 16))) ;

		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(3, 20).setOccupied(true);
		board.getCell(7, 20).setOccupied(true);
		board.calcTargets(board.getCell(7, 19), 1);
		board.getCell(12, 20).setOccupied(false);
		board.getCell(8, 18).setOccupied(false);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(7, 18)));	
		assertTrue(targets.contains(board.getCell(8, 19)));	
		assertTrue(targets.contains(board.getCell(3, 20)));	

		// check leaving a room with a blocked doorway
		board.getCell(12, 13).setOccupied(true);
		board.calcTargets(board.getCell(12, 11), 3);
		board.getCell(12, 13).setOccupied(false);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(9, 9)));
		assertTrue(targets.contains(board.getCell(13, 9)));	
		assertTrue(targets.contains(board.getCell(14, 8)));

	}
}
