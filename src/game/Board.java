package game;

import java.util.ArrayList;
import utils.Constants;
import utils.Heuristics;

/**
 * Represents the game board and manages board-related operations.
 */
public class Board {
	// class member
	private char[][] board; // 5x6 grid

	/**
	 * Constructor: Initializes the board.
	 */
	public Board() {
		board = new char[Constants.ROWS][Constants.COLS];
		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLS; c++) {
				board[r][c] = '\0';
			}
		}
	}

	/*
	 * Constructor: Creates a deep copy of the board.
	 */
	public Board(Board bd) {
		this.board = bd.getBoardState();
	}

	/**
	 * Places a piece if the move is valid.
	 * 
	 * @param row    - gridspace row
	 * @param col    - gridspace column
	 * @param player - the player making the move
	 * @return true if valid, false otherwise
	 */
	public boolean placePiece(int row, int col, char player) {
		// check if valid move
		if (!isValidMove(row, col, player))
			return false;

		// place piece
		board[row][col] = player;
		return true;
	}

	/**
	 * Checks if the move is valid.
	 * 
	 * @param row    - gridspace row
	 * @param col    - gridspace column
	 * @param player - the player making the move
	 * @return true if valid, false otherwise
	 */
	public boolean isValidMove(int row, int col, char player) {
		// check for valid position
		if (!Board.isInBounds(row, col))
			return false;

		// check if board space empty
		if (board[row][col] != '\0')
			return false;

		// check for valid player char
		if (player != 'X' && player != 'O')
			return false;

		// check if whole board is empty of player chars (players first move)
		boolean isEmpty = true;
		for (int r = 0; r < Constants.ROWS; r++) {
			for (int c = 0; c < Constants.COLS; c++) {
				if (board[r][c] == player)
					isEmpty = false;
			}
		}
		if (isEmpty)
			return true;

		/*** check all 8 neighbors ***/
		for (int r = -1; r <= 1; r++) {
			for (int c = -1; c <= 1; c++) {
				// skip self
				if (r == 0 && c == 0)
					continue;

				if (isInBounds(row + r, col + c) && board[row + r][col + c] == player)
					return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the game is over (win or draw).
	 * 
	 * @return true if terminal, false otherwise.
	 */
	public boolean isTerminal() {
		if (getWinner() == '\0')
			return false;
		return true;
	}

	/**
	 * Returns the winner
	 * 
	 * @return 'X', 'O', 'd' if tie, '\0' if no winner (but game still active)
	 */
	public char getWinner() {
		int XWins[] = { 0, 0, 0 };
		int OWins[] = { 0, 0, 0 };

		// check for 4-in-a-rows with 0, 1, and 2 sides open
		for (int i = 0; i < 3; i++) {
			XWins[i] = Heuristics.countNSidesOpen_MInARow(this, 'X', i, Constants.WIN_CONDITION);
			OWins[i] = Heuristics.countNSidesOpen_MInARow(this, 'O', i, Constants.WIN_CONDITION);
		}

		// check if there are any win conditions for X or O
		int XWinCount = XWins[0] + XWins[1] + XWins[2];
		int OWinCount = OWins[0] + OWins[1] + OWins[2];

		// this case should never arise, but we will handle just in case
		if (XWinCount > 0 && OWinCount > 0) {
			return XWinCount > OWinCount ? 'X' : OWinCount > XWinCount ? 'O' : 'd';
		}

		if (XWinCount > 0)
			return 'X';
		if (OWinCount > 0)
			return 'O';

		// check if game is terminal
		int countPossibleXMoves = getSuccessorMoves('X').size();
		int countPossibleOMoves = getSuccessorMoves('O').size();
		if (countPossibleXMoves + countPossibleOMoves == 0)
			return 'd';

		// game is still in progress
		return '\0';
	}

	/**
	 * Returns all possible valid moves.
	 * 
	 * @param player - this player's turn
	 * @return ArrayList of possible moves
	 */
	public ArrayList<Move> getSuccessorMoves(char player) {
		
		ArrayList<Move> moves = new ArrayList<Move>();
		
		// check all spaces available for player
		for (int row = 0; row < Constants.ROWS; row++) {
			for (int col = 0; col < Constants.COLS; col++) {
				
				// add this move to successors, if it is valid
				if (isValidMove(row, col, player)) {
					moves.add(new Move(row, col, player));
				}
			}
		}
		return moves;
	}

	/**
	 * Returns the current board state (deep copy)
	 * 
	 * @return
	 */
	public char[][] getBoardState() {
		char[][] boardCopy = new char[Constants.ROWS][Constants.COLS];
		
		// clone each row
		for (int i = 0; i < Constants.ROWS; i++) {
			boardCopy[i] = this.board[i].clone(); // deep copy
		}
		return boardCopy;
	}

	/**
	 * Print the current board state.
	 */
	public void printBoard() {
		final String rowSeperator;
		rowSeperator = "+---+---+---+---+---+---+";

		// print each row
		System.out.println();
		for (int row = 0; row < Constants.ROWS; row++) {
			System.out.println(rowSeperator);

			// print each grid space
			for (int col = 0; col < Constants.COLS; col++) {
				// temp char for blank spaces
				char temp = board[row][col];
				if (temp == '\0')
					temp = ' ';

				System.out.print("| " + temp + " ");
			}
			System.out.print("|\n");
		}
		System.out.println(rowSeperator);
	}

	/**
	 * Check if (row, col) is within bounds of the board
	 * 
	 * @param row - row number
	 * @param col - column number
	 * @return true if in bounds, false if not
	 */
	public static Boolean isInBounds(int row, int col) {
		if (row >= Constants.ROWS || row < 0)
			return false;
		if (col >= Constants.COLS || col < 0)
			return false;
		return true;
	}
}
