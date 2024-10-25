package main;

import game.FourInARow;
import players.MinimaxPlayer;
import players.Player;

public class Main {
	
	/* note to self: grid implementation in this project does not match that of the project handout
	 * However, the program accounts for this when printing moves to console.
	 * +---+---+---+---+---+---+
	 * |0,0|0,1|0,2|0,3|0,4|0,5|
	 * +---+---+---+---+---+---+
	 * |1,0|1,1|1,2|           |
	 * +---+---+---+           +
	 * |2,0|2,1|2,2|           |
	 * +---+---+---+           +
	 * |3,0|                   |
	 * +---+     	       +---+
	 * |4,0|               |4,5|
	 * +---+---+---+---+---+---+ 
	 */

	public static void main(String[] args) {
	
		Player player1 = new MinimaxPlayer('X', 2);	// X uses a 2-ply look-ahead.
		Player player2 = new MinimaxPlayer('O', 4); // O uses a 4-ply look-ahead.
		
		FourInARow game = new FourInARow(player1, player2);
		game.startGame();  // starts the game loop	
	}

}
