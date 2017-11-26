/**
 * @author 	Tahmid Khan
 * @PID		A12650032
 * This class performs unit tests on the Big BigBoard class functions
 */

package ultimate_tic_tac_toe.tester;

import static org.junit.Assert.*;

import org.junit.Test;

import ultimate_tic_tac_toe.main.BigBoard;

public class BigBoardTester 
{
	@Test
	public void testCheckBigBoardStatus()
	{
		BigBoard game = new BigBoard();
		int expected = 0;
		assertEquals(game.checkBigBoardStatus(), expected);
		
		int[][] test = {{0, 0, 1},
						{0, 0, 2},
						{1, 2, 0}};
		game.setBigBoard(test);
		expected = 0;
		assertEquals(game.checkBigBoardStatus(), expected);

		int[][] test2 = {{0, 0, 1},
						 {0, 1, 2},
						 {1, 2, 0}};
		game.setBigBoard(test2);
		expected = 1;
		assertEquals(game.checkBigBoardStatus(), expected);
		
		int[][] test3 = {{0, 2, 1},
				 		 {1, 2, 2},
				 		 {1, 2, 1}};
		game.setBigBoard(test3);
		expected = 2;
		assertEquals(game.checkBigBoardStatus(), expected);
		
		int[][] test4 = {{2, 1, 2},
				 		 {2, 2, 1},
				 		 {1, 2, 1}};
		game.setBigBoard(test4);
		expected = -1;
		assertEquals(game.checkBigBoardStatus(), expected);
	}
	
	@Test
	public void testMajorClaimed()
	{
		BigBoard game = new BigBoard();
		assertFalse(game.majorClaimed());
		
		int[][] test1 = {{0, 0, 1},
						 {0, 0, 2},
						 {2, 0, 1}};
		game.setBigBoard(test1);
		assertFalse(game.majorClaimed());
		
		int[][] test2 = {{0, 0, 1},
						 {0, 1, 2},
						 {1, 0, 2}};
		game.setBigBoard(test2);
		assertFalse(game.majorClaimed());
		
		int[][] test3 = {{2, 0, 1},
						 {1, 2, 0},
						 {1, 0, 2}};
		game.setBigBoard(test3);
		assertTrue(game.majorClaimed());
		
		int[][] test4 = {{1, 2, 0},
						 {0, 1, 0},
						 {2, 0, 1}};
		game.setBigBoard(test4);
		assertTrue(game.majorClaimed());
	}
	
	@Test
	public void testMinorClaimed()
	{
		BigBoard game = new BigBoard();
		assertFalse(game.minorClaimed());
		
		int[][] test1 = {{0, 0, 1},
						 {0, 0, 2},
						 {2, 0, 1}};
		game.setBigBoard(test1);
		assertFalse(game.minorClaimed());
		
		int[][] test2 = {{2, 0, 1},
						 {1, 2, 0},
						 {1, 0, 2}};
		game.setBigBoard(test2);
		assertFalse(game.minorClaimed());
		
		int[][] test3 = {{0, 0, 1},
						 {0, 1, 2},
						 {1, 0, 2}};
		game.setBigBoard(test3);
		assertTrue(game.minorClaimed());
		
		int[][] test4 = {{1, 1, 2},
						 {1, 2, 0},
						 {2, 0, 0}};
		game.setBigBoard(test4);
		assertTrue(game.minorClaimed());
	}
	
	@Test
	public void testRowClaimed()
	{
		BigBoard game = new BigBoard();
		assertFalse(game.rowClaimed(0));
		assertFalse(game.rowClaimed(1));
		assertFalse(game.rowClaimed(2));
		
		int[][] test1 = {{2, 2, 1},
						 {1, 1, 0},
						 {2, 2, 1}};
		game.setBigBoard(test1);
		assertFalse(game.rowClaimed(0));
		assertFalse(game.rowClaimed(1));
		assertFalse(game.rowClaimed(2));
		
		int[][] test2 = {{2, 2, 2},
						 {0, 1, 1},
						 {0, 0, 1}};
		game.setBigBoard(test2);
		assertTrue(game.rowClaimed(0));
		assertFalse(game.rowClaimed(1));
		assertFalse(game.rowClaimed(2));
		
		int[][] test3 = {{2, 2, 1},
						 {1, 1, 1},
						 {1, 2, 2}};
		game.setBigBoard(test3);
		assertFalse(game.rowClaimed(0));
		assertTrue(game.rowClaimed(1));
		assertFalse(game.rowClaimed(2));
	}
	
	@Test
	public void testColClaimed()
	{
		BigBoard game = new BigBoard();
		assertFalse(game.colClaimed(0));
		assertFalse(game.colClaimed(1));
		assertFalse(game.colClaimed(2));
		
		int[][] test1 = {{0, 0, 1},
						 {2, 2, 1},
						 {0, 2, 1}};
		game.setBigBoard(test1);
		assertFalse(game.colClaimed(0));
		assertFalse(game.colClaimed(1));
		assertTrue(game.colClaimed(2));
		
		int[][] test2 = {{2, 0, 0},
						 {2, 1, 0},
						 {2, 1, 0}};
		game.setBigBoard(test2);
		assertTrue(game.colClaimed(0));
		assertFalse(game.colClaimed(1));
		assertFalse(game.colClaimed(2));
		
		int[][] test3 = {{0, 0, 1},
						 {0, 1, 2},
						 {1, 0, 2}};
		game.setBigBoard(test3);
		assertFalse(game.colClaimed(0));
		assertFalse(game.colClaimed(1));
		assertFalse(game.colClaimed(2));
	}

}
