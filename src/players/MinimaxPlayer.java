package players;

import java.util.concurrent.atomic.AtomicInteger;

import game.Board;
import game.GameState;
import game.Move;
import utils.Heuristics;
import utils.Node;

/**
 * Implements a player that uses the minimax algorithm to choose moves.
 */
public class MinimaxPlayer extends Player {
	
	private int maxDepth;	// Depth to search in the minimax tree.
	
	/**
	 * Constructor to initialize the player and max depth.
	 * @param symbol
	 * @param maxDepth
	 */
	public MinimaxPlayer(char symbol, int maxDepth)
	{
		super(symbol);
		this.maxDepth = maxDepth;
	}
	
	/**
	 * overrides Player's makeMove() to use minimax.
	 */
	public Move makeMove(Board board)
	{
		// start clock
		long startTime = System.nanoTime();
		
		// use minimax to determine best move to take
		GameState state = new GameState(board, playerSymbol);
		Node node = new Node(state);
		Move move = minimaxDecision(node);
		
		// end and print clock
		double elapsedTime = (System.nanoTime() - startTime) / 1000000.0;
		System.out.print(String.format("%.2fms\n", elapsedTime));
		
		return move;
	}
	
	/**
	 * Runs the minimax algorithm to determine the best move for this player
	 * @param node - the current state of the game
	 * @return the best move for this player, according to minimax
	 */
	private Move minimaxDecision(Node node)
	{
		// count the number of nodes generated
		AtomicInteger numGenerated = new AtomicInteger(0);
		
		// determine the opposing player
		char minimizingPlayer = playerSymbol == 'X' ? 'O' : 'X';
		
		GameState state = node.getState();
		Board board = state.getBoard();
		
		// maximize the minValue
		Move maxMove = null;
		int maxVal = Integer.MIN_VALUE;
		
		// find the move that maximizes the minVal
		for(Move successorMove : board.getSuccessorMoves(playerSymbol))
		{
			// create a copy of this board, and make this move
			Board nextBoard = new Board(board);
			nextBoard.placePiece(successorMove.getRow(), successorMove.getCol(), playerSymbol);
			
			// create the node resulting from this move
			GameState nextState = new GameState(nextBoard, minimizingPlayer);
			Node successor = new Node(nextState, node.getDepth() + 1);
			
			numGenerated.incrementAndGet();
			
			// make the maximum minValue
			int res = minValue(successor, minimizingPlayer, numGenerated);
			if(maxVal < res) {
				maxVal = res;
				maxMove = successorMove;
			}
		}
		// print result
		System.out.print(playerSymbol + " made move " + maxMove.toString() + " and generated " + numGenerated + " nodes in ");
		return maxMove;
	}
	
	private int maxValue(Node node, char minPlayer, AtomicInteger numGenerated)
	{		
		GameState state = node.getState();
		Board board = state.getBoard();
		
		// check if at max depth
		if(node.getDepth() == this.maxDepth) return evaluate(board);
		
		// if terminal then return Utility(state)
		if(board.isTerminal()) {
			char res = board.getWinner();
			if(res == playerSymbol) return 1000;
			if(res == 'd') return 0;
			else return -1000;
		}
				
		// v <- -infinity
		int v = Integer.MIN_VALUE;
		
		// for a,s in Successors(state)
		for(Node successor : node.Expand(playerSymbol))
		{
			numGenerated.incrementAndGet();
			
			// do v <- Max(v, Min-Value(s))
			v = Math.max(v, minValue(successor, minPlayer, numGenerated));
		}
		return v;
	}
	
	private int minValue(Node node, char minPlayer, AtomicInteger numGenerated)
	{		
		GameState state = node.getState();
		Board board = state.getBoard();
		
		// check if at max depth
		if(node.getDepth() == this.maxDepth) return evaluate(board);
		
		// if terminal then return Utility(state)
		if(board.isTerminal()) {
			char res = board.getWinner();
			if(res == playerSymbol) return 1000;
			if(res == 'd') return 0;
			else return -1000;
		}
		
		// v <- infinity
		int v = Integer.MAX_VALUE;
		
		// for a,s in Successors(state)
		for(Node successor : node.Expand(minPlayer))
		{
			numGenerated.incrementAndGet();
			
			// do v <- Min(v, Max-Value(s))
			v = Math.min(v,  maxValue(successor, minPlayer, numGenerated));
		}
		return v;
	}
	
	/**
	 * Heuristic evaluation function (h(n) from the problem statement).
	 * @param board
	 * @return
	 */
	private int evaluate(Board board)
	{
		return Heuristics.evaluate(board,  playerSymbol);
	}
}
