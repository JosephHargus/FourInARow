package utils;

import java.util.HashSet;
import game.Board;

/**
 * Calculates the heuristic score of the board at the cut-off nodes.
 */
public class Heuristics {
	
	/**
	 * Heuristic function to evaluate non-terminal states.
	 * @param board
	 * @param player
	 * @return
	 */
	public static int evaluate(Board board, char player)
	{	
		// calculate points for this player
		int thisPlayerPoints = 
				200 * countNSidesOpen_MInARow(board, player, 2, 3) +
				150 * countNSidesOpen_MInARow(board, player, 1, 3) +
				20 * countNSidesOpen_MInARow(board, player, 2, 2) +
				5 * countNSidesOpen_MInARow(board, player, 1, 2);
		
		// calculate points for other player
		char otherPlayer = player == 'X' ? 'O' : 'X';
		int otherPlayerPoints = 
				80 * countNSidesOpen_MInARow(board, otherPlayer, 2, 3) +
				40 * countNSidesOpen_MInARow(board, otherPlayer, 1, 3) +
				15 * countNSidesOpen_MInARow(board, otherPlayer, 2, 2) +
				2 * countNSidesOpen_MInARow(board, otherPlayer, 1, 2);
		
		return thisPlayerPoints - otherPlayerPoints;
	}
	
	/**
	 * Count the number of sequences with n sides open and of length m.
	 * @param board - the current game board
	 * @param player - the player who's pieces should be checked
	 * @param n - number of sides open
	 * @param m - length of sequence
	 * @return the number of sequences with n sides open, m in a row.
	 */
	public static int countNSidesOpen_MInARow(Board board, char player, int n, int m)
	{
		// we will keep track of all pieces in a row to prevent duplicates
		HashSet<PieceSequence> sequences = new HashSet<PieceSequence>();
		
		// add all sequences on the board for this player
		sequences.addAll(getAllSequences(board, player));
		
		// narrow down to n sides open
		sequences.removeIf(sequence -> countSidesOpen(board, sequence) != n);
		
		// narrow down to m pieces long
		sequences.removeIf(sequence -> sequence.length != m);
		
		return sequences.size();
	}
	
	/**
	 * Get every sequence of this player's pieces.
	 * @param boardObj - the current game board
	 * @param player - the player whose pieces to check
	 * @return HashSet of all PieceSequences
	 */
	public static HashSet<PieceSequence> getAllSequences(Board boardObj, char player)
	{
		char[][] board = boardObj.getBoardState();
		HashSet<PieceSequence> sequences = new HashSet<PieceSequence>();
		
		// loop through every position on the board
		for(int row = 0; row < Constants.ROWS; ++row) {
			for(int col = 0; col < Constants.COLS; ++col) {
				
				// check if this posn belongs to this player
				if(board[row][col] == player)
				{
					// look for the longest sequence in all four directions:
					// 1. Horizontal
					addOrReplaceSequence(sequences, getLongestSequence(boardObj, player, row, col, 0, 1));
					// 2. Vertical
					addOrReplaceSequence(sequences, getLongestSequence(boardObj, player, row, col, 1, 0));
					// 3. Diagonal (right-down)
					addOrReplaceSequence(sequences, getLongestSequence(boardObj, player, row, col, 1, 1));
					// 4. Diagonal (left-down)
					addOrReplaceSequence(sequences, getLongestSequence(boardObj, player, row, col, 1, -1));
				}
			}
		}
		return sequences;
	}
	
	/**
	 * Will add the sequence to the HashSet, if a larger overr-encompassing sequence is not already there. Otherwise, will
	 * check for the opposite, and replace and subsequences of its own.
	 * @param sequences - the HashSet of all sequences found so far
	 * @param newSeq - the new sequence to add
	 * @return true if added, false otherwise
	 */
	private static boolean addOrReplaceSequence(HashSet<PieceSequence> sequences, PieceSequence newSeq) {
	    if (newSeq == null) {
	        return false;
	    }
	    
	    // store sequences to be removed if they are sub-segments of the new sequence
	    HashSet<PieceSequence> toRemove = new HashSet<>();
	    boolean shouldAdd = true;

	    // check if any existing sequence allready contains this sequence
	    for (PieceSequence existingSeq : sequences) {
	    	
	        if (existingSeq.contains(newSeq)) {
	            // new sequence is a sub-segment of an existing one, do not add it
	            shouldAdd = false;
	        } 
	        
	        else if (newSeq.contains(existingSeq)) {
	            // existing sequence is a sub-segment of the new one, mark it for removal
	            toRemove.add(existingSeq);
	        }
	    }

	    // remove smaller sub-segments
	    sequences.removeAll(toRemove);

	    // add the new sequence if it should be added
	    if (shouldAdd) {
	        sequences.add(newSeq);
	        return true;
	    }
	    return false;
	}
	
	/**
	 * Finds the longest sequence in the direction (rowDir, colDir) 
	 * with the start point (startRow, startCol).
	 * @param boardObj - the current game board
	 * @param player - the current player
	 * @param startRow - row coordinate of start point
	 * @param startCol - column coordinate of end point
	 * @param rowDir - direction row moves
	 * @param colDir - direction col moves
	 * @return the longest sequence from the given start point in the given direction
	 */
	public static PieceSequence getLongestSequence(Board boardObj, char player, 
										   int startRow, int startCol, int rowDir, int colDir)
	{
		// check for valid input
		if(boardObj == null) return null;
		char[][] board = boardObj.getBoardState();
		if(board[startRow][startCol] != player) return null;
		
		// starting values
		int row = startRow;
		int col = startCol;
		int length = 1;
		
		// find the longest sequence in the given direction
		while(Board.isInBounds(row + rowDir, col + colDir) && board[row + rowDir][col + colDir] == player)
		{
			row += rowDir;
			col += colDir;
			length++;
		}
		
		// only add sequences of length > 1
		if (length > 1) return new PieceSequence(startRow, startCol, row, col);
		else return null;
	}
	
	/**
	 * Count the number of sides open on this sequence.
	 * @param boardObj - the current game board
	 * @param seq - the sequence to check
	 * @return the number of open sides
	 */
	public static int countSidesOpen(Board boardObj, PieceSequence seq) 
	{
		int numSidesOpen = 0;
		char[][] board = boardObj.getBoardState();
		
		// (loosely) check for invalid sequence
		// previous functions should already ensure sequence is valid
		if(board[seq.start_row][seq.start_col] == '\0' || board[seq.end_row][seq.end_col] == '\0' ||
		   board[seq.start_row][seq.start_col] != board[seq.end_row][seq.end_col]) {
			return -1;
		}
		
		// check at start point
		if(Board.isInBounds(seq.start_row - seq.rowDirection, seq.start_col - seq.colDirection) &&
				board[seq.start_row - seq.rowDirection][seq.start_col - seq.colDirection] == '\0')
			numSidesOpen++;
		
		// check at end point
		if(Board.isInBounds(seq.end_row + seq.rowDirection, seq.end_col + seq.colDirection) &&
				board[seq.end_row + seq.rowDirection][seq.end_col + seq.colDirection] == '\0')
			numSidesOpen++;
		
		return numSidesOpen;
	}
}




















