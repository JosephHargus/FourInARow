package players;

import game.Board;
import game.Move;

/**
 * Abstract base class for players (AI or human).
 */
public abstract class Player {
	
	protected char playerSymbol; // 'X' or 'O'
	
	/**
	 * Constructor: Initializes player symbol.
	 * @param symbol
	 */
	public Player(char symbol)
	{
		playerSymbol = symbol;
	}
	
	/**
	 * Makes a move for the player.
	 * @param board
	 * @return
	 */
	public abstract Move makeMove(Board board);
	
	/**
	 * Returns the player's symbol.
	 * @return
	 */
	public char getSymbol()
	{
		return playerSymbol;
	}
}
