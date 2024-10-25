package game;

/**
 * Represents a move made on the board.
 * Consists of indices and the player who makes the move.
 */
public class Move {
	// class members
	private int row;
	private int col;
	private char player;
	
	/**
	 * Constructor to initialize a move.
	 * @param row
	 * @param col
	 * @param player
	 */
	public Move(int row, int col, char player)
	{
		this.row = row;
		this.col = col;
		this.player = player;
	}
	
	/**
	 * Returns the row of the move.
	 * @return
	 */
	public int getRow()
	{
		return row;
	}
	
	/**
	 * Returns the column of the move.
	 * @return
	 */
	public int getCol()
	{
		return col;
	}
	
	/**
	 * Return the player who makes the move.
	 * @return
	 */
	public char getPlayer()
	{
		return player;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || obj.getClass() != this.getClass()) return false;
		Move move = (Move)obj;
		return move.row == row && move.col == col && move.player == player;
	}
	
	@Override
	public String toString() {
		// accounts for the index discrepancy between my implementation and the homework handout
		return String.format("[%d,%d]", row + 1, col + 1);
	}
}
