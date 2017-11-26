/**
 * @author 	Tahmid Khan
 * @PID		A12650032
 * This class is a logical representation of a single
 * basic TTT board
 */

package ultimate_tic_tac_toe.main;

public class Board extends GenericBoard
{	
	// An 2D matrix representing the status of the
	// individual TTT board
	private int[][] board;
	
	public Board()
	{
		board = new int[BOARD_SIZE][BOARD_SIZE];
		// Initialize board to all 0s (unclaimed)
		for(int i = 0; i < BOARD_SIZE; i++)
			for(int j = 0; j < BOARD_SIZE; j++)
				board[i][j] = NEUTRAL_MARKER;
	}
	
	// [SET METHODS]
	/**
	 * Sets a marker of the current player on the board
	 * Precondition: the position has been checked to be neutral
	 * before calling this method 
	 * @param row the row of the marker
	 * @param col the column of the marker
	 */
	public void makeMove(int row, int col)
	{
		board[row][col] = playerTurn;
		setVictory(checkBoardStatus());
	}
	
	// [CHECK METHODS]
	/**
	 * Checks if a position is claimed or unclaimed by either
	 * player
	 * @param row row of the marker
	 * @param col column of the marker	
	 * @return true if claimed, false if still a neutral position
	 */
	public boolean positionClaimed(int row, int col)
	{
		return board[row][col] != NEUTRAL_MARKER;
	}
	
	/**
	 * Checks if the given row is claimed by a player
	 * @param row the row to check
	 * @return true if claimed by a player (all markers in the
	 * 	row belong to a single player), false otherwise
	 */
	public boolean rowClaimed(int row)
	{
		int firstMarker = board[row][0];
		if(firstMarker == 0)
			return false;
		
		for(int i = 1; i < BOARD_SIZE; i++)
			if(board[row][i] != firstMarker)
				return false;
		
		return true;
	}
	
	/**
	 * Checks if the given column is claimed by a player
	 * @param col the column to check
	 * @return true if claimed by a player (i.e all markers in the
	 * 	row belong to a single player), false otherwise
	 */
	public boolean colClaimed(int col)
	{
		int firstMarker = board[0][col];
		if(firstMarker == 0)
			return false;
		
		for(int i = 1; i < BOARD_SIZE; i++)
			if(board[i][col] != firstMarker)
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
		int firstMarker = board[0][0];
		if(firstMarker == 0)
			return false;
		
		for(int i = 1; i < BOARD_SIZE; i++)
			if(board[i][i] != firstMarker)
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
		int firstMarker = board[BOARD_SIZE - 1][0];
		if(firstMarker == 0)
			return false;
		
		for(int i = 1, j = 1; i < BOARD_SIZE && j < BOARD_SIZE; i--, j++)
			if(board[i][j] != firstMarker)
				return false;
		
		return true;
	}
	
	/**
	 * Checks if one of the players have won possession of the 
	 * working board
	 * @return 	0 if unclaimed, 1 if claimed by p1, 2 by p2
	 *			and -1 if tie board 
	 */
	public int checkBoardStatus()
	{
		// Check rows and columns first
		for(int i = 0; i < BOARD_SIZE; i++)
		{
			if(rowClaimed(i))
				return board[i][0];
			if(colClaimed(i))
				return board[0][i];
		}
		
		// Check diagonals
		if(majorClaimed() || minorClaimed())
			return board[1][1];
		
		// Check if the board can still be claimed (there's atleast 1 
		// open cell)
		for(int row = 0; row < BOARD_SIZE; row++)
			for(int col = 0; col < BOARD_SIZE; col++)
				if(board[row][col] == 0)
					return NEUTRAL_MARKER;
		
		// If all else fails, this board is a tie board
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
	 * Sets a marker in the given position (row, col)
	 * @param marker the marker of the player to place
	 * @param row the row of the marker
	 * @param col the column of the marker
	 */
	public void setMarker(int marker, int row, int col)
	{
		board[row][col] = marker;
		return;
	}
	
	/**
	 * Sets a given 2D array of size 3 to be the board's representation
	 * Used purely for the purposes of the respective JUnit tester class
	 * @param 	board a board representation consisting of 0s, 1s, and 2s
	 * 			only
	 */
	public void setBoard(int[][] board)
	{
		if(board.length == BOARD_SIZE && board[0].length == BOARD_SIZE)
			this.board = board;
	}
	
	// [ACCESSOR METHODS]
	public int[][] getBoard()	{return board;}
	
	/**
	 * A method purely for testing purposes
	 */
	public void printBoard(int boardNum)
	{
		System.out.println("Board #" + boardNum);
		for(int r = 0; r < 3; r++)
		{
			for(int c = 0; c < 3; c++)
				System.out.print(board[r][c]);
			System.out.println();
		}	
	}
}
