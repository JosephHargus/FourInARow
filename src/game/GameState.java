package game;

/**
 * Encapsulates the state of the game at any given point.
 */
public class GameState {
	// class members
	private Board board; // the current board state
	private char currentPlayer; // 'X' or 'O'
	
	/**
	 * Constructor: initializes the state.
	 * @param board - if null, a new board is created
	 * @param currentPlayer - the player whose turn it is
	 */
	public GameState(Board board, char currentPlayer)
	{
		if(board == null) board = new Board();
		this.board = board;
		this.currentPlayer = currentPlayer;
	}
	
	/**
	 * Returns the current player.
	 * @return
	 */
	public char getCurrentPlayer()
	{
		return currentPlayer;
	}
	
	/**
	 * Returns the board in this state.
	 * @return
	 */
	public Board getBoard()
	{
		return board;
	}
}
