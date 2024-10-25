package utils;

import game.Board;

// data class to assist in calculating heuristic
public class PieceSequence {
	// coordinates of one end point
	public int start_row;
	public int start_col;
	
	// coordinates of another end point
	public int end_row;
	public int end_col;
	
	// the directions of the sequence
	public int rowDirection;
	public int colDirection;
	
	// the length of the sequence
	public int length;
	
	
	
	/**
	 * The start point will be the point with:
	 *   - Lowest Row Number, then (if equal)
	 *   - Lowest Col Number
	 * @param p1_row
	 * @param p1_col
	 * @param p2_row
	 * @param p2_col
	 */
	public PieceSequence(int p1_row, int p1_col, int p2_row, int p2_col)
	{
		if(!Board.isInBounds(p1_row, p1_col) || !Board.isInBounds(p2_row, p2_col)) {
			throw new IllegalArgumentException("PieceSequence constructor was passed invalid arguments.");
		}
		
		// sort the 2 points, based on the function description
		if(p1_row < p2_row || (p1_row == p2_row && p1_col < p2_col))
		{
			start_row = p1_row;
			start_col = p1_col;
			end_row = p2_row;
			end_col = p2_col;
		}
		else
		{
			start_row = p2_row;
			start_col = p2_col;
			end_row = p1_row;
			end_col = p1_col;
		}
		
		// calculate sequence direction
		rowDirection = start_row < end_row ? 1 : 0;
		colDirection = start_col < end_col ? 1 : start_col > end_col ? -1 : 0;
		
		// calculate length
		length = Math.max(Math.abs(end_row - start_row), Math.abs(end_col - start_col)) + 1;
	}
	
	/**
	 * Check if this sequence contains a smaller subsequence seq
	 * @param seq - the PieceSequence to check
	 * @return true if seq is smaller subsequence of this, false otherwise
	 */
	public boolean contains(PieceSequence seq)
	{
		if(seq == null) return false;
		
		// check if both sequences move in the same direction
		if(this.rowDirection != seq.rowDirection || this.colDirection != seq.colDirection)
			return false;
		
		// check if this sequence is shorter than seq
		if(this.length < seq.length)
			return false;
		
		int row = start_row;
		int col = start_col;
		
		// move down the sequence, until an overlap is encountered (or lack thereof)
		while(row != end_row || col != end_col)
		{
			// if points are equal, they overlap
			if((seq.start_row == row && seq.start_col == col) || (seq.end_row == row && seq.end_col == col)) {
				return true;
			}
			
			// move to next point
			row += rowDirection;
			col += colDirection;
		}
		
		// check end point
		if((seq.start_row == end_row && seq.start_col == end_col) && (seq.end_row == end_row && seq.end_col == end_col))
			return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		// treat sequences with the same 2 points as the same, regardless of their ordering
	    int point1Hash = start_row * 31 + start_col;
	    int point2Hash = end_row * 31 + end_col;

	    // Combine both hash values
	    return Math.min(point1Hash, point2Hash) * 31 + Math.max(point1Hash, point2Hash);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || obj.getClass() != this.getClass()) return false;
		
		PieceSequence seq = (PieceSequence)obj;
		return seq.hashCode() == this.hashCode();
	}
	
	@Override
	public String toString() {
		return "(" + start_row + ", " + start_col + ") -> (" + end_row + ", " + end_col + ")";
	}
}