/**
 * @author 	Tahmid Khan
 * @PID		A12650032
 * This class performs unit tests on the Board class functions
 */

package ultimate_tic_tac_toe.tester;

import static org.junit.Assert.*;
import org.junit.Test;
import ultimate_tic_tac_toe.main.Board;

public class BoardTester 
{
	@Test
	public void testPositionClaimed()
	{
		Board game = new Board();
		int[][] test = {{0, 0, 1},
						{0, 0, 2},
						{1, 2, 0}};
		game.setBoard(test);
		
		assertFalse(game.positionClaimed(0, 0));
		assertFalse(game.positionClaimed(0, 1));
		assertTrue(game.positionClaimed(0, 2));
		
		assertFalse(game.positionClaimed(1, 0));
		assertFalse(game.positionClaimed(1, 1));
		assertTrue(game.positionClaimed(1, 2));
		
		assertTrue(game.positionClaimed(2, 0));
		assertTrue(game.positionClaimed(2, 1));
		assertFalse(game.positionClaimed(2, 2));
	}
	
	@Test
	public void testSetMarker()
	{
		Board game = new Board();
		game.setMarker(Board.P1_MARKER, 0, 0);
		game.setMarker(Board.P2_MARKER, 2, 2);
		game.setMarker(Board.P1_MARKER, 1, 0);
		game.setMarker(Board.P2_MARKER, 0, 0);
		
		int[][] expected = {{2, 0, 0},
							{1, 0, 0},
							{0, 0, 2}};
		assertArrayEquals(game.getBoard(), expected);
	}

	@Test
	public void testCheckBoardStatus()
	{
		Board game = new Board();
		int expected = 0;
		assertEquals(game.checkBoardStatus(), expected);
		
		int[][] test = {{0, 0, 1},
						{0, 0, 2},
						{1, 2, 0}};
		game.setBoard(test);
		expected = 0;
		assertEquals(game.checkBoardStatus(), expected);

		int[][] test2 = {{0, 0, 1},
						 {0, 1, 2},
						 {1, 2, 0}};
		game.setBoard(test2);
		expected = 1;
		assertEquals(game.checkBoardStatus(), expected);
		
		int[][] test3 = {{0, 2, 1},
				 		 {1, 2, 2},
				 		 {1, 2, 1}};
		game.setBoard(test3);
		expected = 2;
		assertEquals(game.checkBoardStatus(), expected);
		
		int[][] test4 = {{2, 1, 2},
				 		 {2, 2, 1},
				 		 {1, 2, 1}};
		game.setBoard(test4);
		expected = -1;
		assertEquals(game.checkBoardStatus(), expected);
		
		int[][] test5 = {{2, 0, 2},
		 		 		 {2, 1, 1},
		 		 		 {1, 1, 2}};
		game.setBoard(test5);
		expected = 0;
		assertEquals(game.checkBoardStatus(), expected);
	}
	
	@Test
	public void testMajorClaimed()
	{
		Board game = new Board();
		assertFalse(game.majorClaimed());
		
		int[][] test1 = {{0, 0, 1},
						 {0, 0, 2},
						 {2, 0, 1}};
		game.setBoard(test1);
		assertFalse(game.majorClaimed());
		
		int[][] test2 = {{0, 0, 1},
						 {0, 1, 2},
						 {1, 0, 2}};
		game.setBoard(test2);
		assertFalse(game.majorClaimed());
		
		int[][] test3 = {{2, 0, 1},
						 {1, 2, 0},
						 {1, 0, 2}};
		game.setBoard(test3);
		assertTrue(game.majorClaimed());
		
		int[][] test4 = {{1, 2, 0},
						 {0, 1, 0},
						 {2, 0, 1}};
		game.setBoard(test4);
		assertTrue(game.majorClaimed());
	}
	
	@Test
	public void testMinorClaimed()
	{
		Board game = new Board();
		assertFalse(game.minorClaimed());
		
		int[][] test1 = {{0, 0, 1},
						 {0, 0, 2},
						 {2, 0, 1}};
		game.setBoard(test1);
		assertFalse(game.minorClaimed());
		
		int[][] test2 = {{2, 0, 1},
						 {1, 2, 0},
						 {1, 0, 2}};
		game.setBoard(test2);
		assertFalse(game.minorClaimed());
		
		int[][] test3 = {{0, 0, 1},
						 {0, 1, 2},
						 {1, 0, 2}};
		game.setBoard(test3);
		assertTrue(game.minorClaimed());
		
		int[][] test4 = {{1, 1, 2},
						 {1, 2, 0},
						 {2, 0, 0}};
		game.setBoard(test4);
		assertTrue(game.minorClaimed());
	}
	
	@Test
	public void testRowClaimed()
	{
		Board game = new Board();
		assertFalse(game.rowClaimed(0));
		assertFalse(game.rowClaimed(1));
		assertFalse(game.rowClaimed(2));
		
		int[][] test1 = {{2, 2, 1},
						 {1, 1, 0},
						 {2, 2, 1}};
		game.setBoard(test1);
		assertFalse(game.rowClaimed(0));
		assertFalse(game.rowClaimed(1));
		assertFalse(game.rowClaimed(2));
		
		int[][] test2 = {{2, 2, 2},
						 {0, 1, 1},
						 {0, 0, 1}};
		game.setBoard(test2);
		assertTrue(game.rowClaimed(0));
		assertFalse(game.rowClaimed(1));
		assertFalse(game.rowClaimed(2));
		
		int[][] test3 = {{2, 2, 1},
						 {1, 1, 1},
						 {1, 2, 2}};
		game.setBoard(test3);
		assertFalse(game.rowClaimed(0));
		assertTrue(game.rowClaimed(1));
		assertFalse(game.rowClaimed(2));
	}
	
	@Test
	public void testColClaimed()
	{
		Board game = new Board();
		assertFalse(game.colClaimed(0));
		assertFalse(game.colClaimed(1));
		assertFalse(game.colClaimed(2));
		
		int[][] test1 = {{0, 0, 1},
						 {2, 2, 1},
						 {0, 2, 1}};
		game.setBoard(test1);
		assertFalse(game.colClaimed(0));
		assertFalse(game.colClaimed(1));
		assertTrue(game.colClaimed(2));
		
		int[][] test2 = {{2, 0, 0},
						 {2, 1, 0},
						 {2, 1, 0}};
		game.setBoard(test2);
		assertTrue(game.colClaimed(0));
		assertFalse(game.colClaimed(1));
		assertFalse(game.colClaimed(2));
		
		int[][] test3 = {{0, 0, 1},
						 {0, 1, 2},
						 {1, 0, 2}};
		game.setBoard(test3);
		assertFalse(game.colClaimed(0));
		assertFalse(game.colClaimed(1));
		assertFalse(game.colClaimed(2));
	}
}
