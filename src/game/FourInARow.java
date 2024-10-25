package game;

import players.Player;

/**
 * Manages the game loop and gameplay.
 */
public class FourInARow {
	// class members
	private Board board;
	private Player player1;
	private Player player2;
	private Player currentPlayer;
	
	/**
	 * Constructor: Initializes the players.
	 * Also places the start 2 piece as described in homework handout.
	 * @param player1
	 * @param player2
	 */
	public FourInARow(Player player1, Player player2)
	{
		board = new Board();
		
		// place starting pieces (as indicated by homework)
		board.placePiece(2, 3, 'X');
		board.placePiece(2, 2, 'O');
		
		this.player1 = player1;
		this.player2 = player2;
		
		currentPlayer = player1;
	}
	
	/**
	 * Starts the game loop and handles player turns.
	 */
	public void startGame()
	{	
		char res;
		
		while(true)
		{
			makeMove(currentPlayer);
						
			// if game no longer in progress, break game loop
			res = board.getWinner();
			if(res != '\0') break;
			
			switchPlayer();
		}
		
		// check for tie
		if(res == 'd') {
			System.out.println("It was a tie!");
		}
		else {
			System.out.println("Player " + res + " wins!");
		}
		board.printBoard();
	}
	
	/**
	 * Switches between player 1 and player 2.
	 */
	private void switchPlayer()
	{
		if(currentPlayer == player1)
		{
			currentPlayer = player2;
			return;
		}
		
		currentPlayer = player1;
	}
	
	/**
	 * Makes a move for the current player.
	 * @param player
	 * @return
	 */
	private void makeMove(Player player)
	{
		Move move = player.makeMove(board);
		board.placePiece(move.getRow(), move.getCol(), move.getPlayer());
	}
}
