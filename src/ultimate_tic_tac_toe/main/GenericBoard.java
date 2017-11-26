/**
 * @author 	Tahmid Khan
 * @PID		A12650032
 * This class is the parent class for the individual boards and
 * the larger ultimate TTT board and contains all common variables
 * and methods
 */

package ultimate_tic_tac_toe.main;

public class GenericBoard 
{
	// The size of any tic-tac-toe board is 3x3 
	public static final int BOARD_SIZE = 3;
	public static final int NUM_CELLS = BOARD_SIZE * BOARD_SIZE;
	
	/* Integer representations of the 3 possible states of any
	   position or the board itself
	*/
	public static final int NEUTRAL_MARKER = 0;
	public static final int P1_MARKER = 1;
	public static final int P2_MARKER = 2;
	public static final int TIE_MARKER = -1;
	
	/* Holds the victory state of the board:
	   default(0), P1's victory(1) P2's victory(2), and tie(-1)
	*/
	protected int victory;
	// Tracks which player's turn it currently is
	protected static int playerTurn;
	
	public GenericBoard(){
		victory = NEUTRAL_MARKER;
		playerTurn = P1_MARKER;
	}
	
	/**
	 * Changes playerTurn according to whose turn it is 
	 * currently
	 */
	public void switchPlayer()
	{
		if(playerTurn == P1_MARKER)
			playerTurn = P2_MARKER;
		else
			playerTurn = P1_MARKER;
	}

	// [ACCESSOR METHODS]
	/**
	 * @return the integer victory status of the game
	 */
	public int getVictory() 	{return victory;}
	/**
	 * @return the integer representation of the current player's turn
	 */
	public int getPlayerTurn() 	{return playerTurn;}
}
