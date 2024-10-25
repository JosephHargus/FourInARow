package players;

import java.util.Scanner;

import game.Board;
import game.Move;

/**
 * Allows a human player to input moves.
 * Primarily for testing.
 */
public class HumanPlayer extends Player {
	
	Scanner scanner;
	
	/**
	 * Constructor: Initializes player symbol.
	 * @param symbol
	 */
	public HumanPlayer(char symbol, Scanner scanner)
	{
		super(symbol);
		this.scanner = scanner;
	}
	
	/**
	 * Implements Player's makeMove() using user input.
	 */
	public Move makeMove(Board board)
	{
		while(true)
		{
			// ask player to make move
			System.out.println("Player " + playerSymbol + " make a move: ");
			String input = scanner.nextLine();
			
			// parse input
			String[] inputToks = input.split(",");
			int row = Integer.parseInt(inputToks[0]);
			int col = Integer.parseInt(inputToks[1]);
		
			// check if valid
			if(board.isValidMove(row, col, playerSymbol))
			{
				return new Move(row, col, playerSymbol);
			}
			
			// input was invalid, try again
			System.out.println("Invalid move.");
		}
		
	}
}
