/**
 * @author 	Tahmid Khan
 * @PID		A12650032
 * This class is a logical representation of the larger 
 * ultimate TTT board
 */

package ultimate_tic_tac_toe.main;

public class BigBoard extends GenericBoard 
{
	/* An array representing the 9 smaller TTT boards on the
	 * big ultimate TTT board
	 */
	private Board[] ultBoard;
	/* Holds the board number of the board that the player should
	 * be playing on currently
	 */
	private int workingBoard;
	
	public BigBoard()
	{
		/* Declare the ultimate TTT board and initialize each of
		 * the 9 children board 
		 */
		ultBoard = new Board[NUM_CELLS];
		for(int i = 0; i < NUM_CELLS; i++)
				ultBoard[i] = new Board();
			
		workingBoard = -1;
	}
	
	/**
	 * Makes a move in the specified board and cell, updates
	 * the working board to the given board and the big board's
	 * victory status, and lastly returns the victory state of 
	 * the board where the play was made
	 * @param boardNum the board's index
	 * @param cellNum the board's cell index
	 * @return the victory state of Board #boardNum
	 */
	public int makeMove(int boardNum, int cellNum)
	{
		int row = cellNum / 3;
		int col = cellNum % 3;
		
		// Make the move
		Board board = ultBoard[boardNum];
		board.makeMove(row, col);
		
		// Switch players
		switchPlayer();
		
		// Update working board to cellNum if the board is still
		// unclaimed... otherwise, there is no working board (-1)
		Board nextBoard = ultBoard[cellNum];
		if(nextBoard.getVictory() == 0)
			workingBoard = cellNum;
		else
			workingBoard = -1;
		
		// Update the big board's victory status
		setVictory(checkBigBoardStatus());
		return board.getVictory();
	}
	
	/**
	 * Checks if the given row is claimed by a player
	 * @param row the row (0 --> 2) to check
	 * @return true if claimed by a player (all markers in the
	 * 	row belong to a single player), false otherwise
	 */
	public boolean rowClaimed(int row)
	{
		int firstMarker = ultBoard[row * 3].getVictory();
		if(firstMarker == 0)
			return false;
		
		for(int i = 1; i < BOARD_SIZE; i++)
			if(ultBoard[row * 3 + i].getVictory() != firstMarker)
				return false;
		
		return true;
	}
	
	/**
	 * Checks if the given column is claimed by a player
	 * @param col the column (0 --> 1) to check
	 * @return true if claimed by a player (i.e all markers in the
	 * 	row belong to a single player), false otherwise
	 */
	public boolean colClaimed(int col)
	{
		int firstMarker = ultBoard[col].getVictory();
		if(firstMarker == 0)
			return false;
		
		for(int i = 1; i < BOARD_SIZE; i++)
			if(ultBoard[col + 3*i].getVictory() != firstMarker)
				return false;
		
		return true;
	}
	
	/**
	 * Checks if the major diagonal of the board has been 
	 * claimed. Namely:
	 * X | 0 | 0
	 * 0 | X | 0
	 * 0 | 0 | X
	 * @return true if claimed, false otherwise
	 */
	public boolean majorClaimed()
	{
		int firstMarker = ultBoard[0].getVictory();
		if(firstMarker == 0)
			return false;
		
		for(int i = 1; i < BOARD_SIZE; i++)
			if(ultBoard[4*i].getVictory() != firstMarker)
				return false;
		
		return true;
	}
	
	/**
	 * Checks if the minor diagonal of the board has been 
	 * claimed. Namely:
	 * 0 | 0 | X
	 * 0 | X | 0
	 * X | 0 | 0
	 * @return true if claimed, false otherwise
	 */
	public boolean minorClaimed()
	{
		int firstMarker = ultBoard[2].getVictory();
		if(firstMarker == 0)
			return false;
		
		for(int i = 2; i <= BOARD_SIZE; i++)
			if(ultBoard[2*i].getVictory() != firstMarker)
				return false;
		
		return true;
	}
	
	/**
	 * Checks if one of the players have won possession of the 
	 * entire big board
	 * @return 	0 if unclaimed, 1 if claimed by p1, 2 by p2
	 *			and -1 if tie board 
	 */
	public int checkBigBoardStatus()
	{
		// Check and columns first
		for(int i = 0; i < BOARD_SIZE; i++)
		{
			// Check rows and columns
			if(rowClaimed(i))
				return ultBoard[3*i].getVictory();
			if(colClaimed(i))
				return ultBoard[i].getVictory();
		}
		
		// Check diagonals
		if(majorClaimed() || minorClaimed())
			return ultBoard[4].getVictory();
		
		// Check if the board can still be claimed or if it's a
		// tie game
		for(Board board : ultBoard)
			if(board.getVictory() == 0)
				return NEUTRAL_MARKER;
		
		return TIE_MARKER;
	}
	
	// [MUTATOR METHODS]
	/**
	 * Sets the victory status of the working board
	 * 0 = neutral board
	 * 1 = p1's victory
	 * 2 = p2's victory
	 * -1 = tie board
	 */
	public void setVictory(int victory)
	{
		this.victory = victory;
	}
	
	/**
	 * Sets a given 2D array of size 3 to be the board's representation
	 * A method purely for the sake of testing the class in the
	 * appropriate JUnit tester class
	 * @param 	boardVictories an int representation of the big board's 
	 * 			sub-board victory states consisting of 0s, 1s, and 2s
	 */
	public void setBigBoard(int[][] boardVictories)
	{
		for(int i = 0; i < NUM_CELLS; i++)
		{
			Board b = ultBoard[i];
			int row = i / 3;
			int col = i % 3;
			b.setVictory(boardVictories[row][col]);
		}
	}
	
	// [ACCESSOR METHODS]
	/**
	 * @return 	returns the board object currently being played on
	 */
	public int getWorkingBoard()	{return workingBoard;}
	/**
	 * @return 	returns the ultimate TTT board as a whole
	 */
	public Board[] getGameBoard()	{return ultBoard;}

	public String toString()
	{
		String board = "";
		for(int i = 0; i < NUM_CELLS; i++)
			board += "Board #" + (i+1) + ": " + ultBoard[i].getVictory() + "\n";
		
		return board;
	}
}
