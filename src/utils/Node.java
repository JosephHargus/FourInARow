package utils;

import java.util.ArrayList;

import game.Board;
import game.GameState;
import game.Move;

public class Node {
	
	private int depth;
	private ArrayList<Node> successors;
	private GameState state;
	
	/**
	 * Default constructor: initializes state and successors
	 * @param newState
	 */
	public Node(GameState newState)
	{
		this(newState, 0);
	}
	
	/**
	 * Constructor: create a new node with the given depth
	 * @param newState
	 * @param depth
	 */
	public Node(GameState newState, int depth)
	{
		this.depth = depth;
		state = newState;
		successors = new ArrayList<Node>();
	}
	
	/**
	 * Expand this Node, generate successors
	 * @return
	 */
	public ArrayList<Node> Expand(char playerSymbol)
	{
		Board board = state.getBoard();
		ArrayList<Move> successorMoves = board.getSuccessorMoves(playerSymbol);
		
		// for every move in s, create a corresponding node
		for(Move m : successorMoves)
		{
			// copy the board and place the piece
			Board boardCopy = new Board(board);
			boardCopy.placePiece(m.getRow(), m.getCol(), m.getPlayer());
			
			// determine the next player
			char nextPlayer = m.getPlayer() == 'X' ? 'O' : 'X';
			
			// add this node to successors
			successors.add(new Node(new GameState(boardCopy, nextPlayer), this.depth + 1));
		}
		return successors;
	}
	
	public int getDepth()
	{
		return depth;
	}
	
	public GameState getState()
	{
		return state;
	}
}
