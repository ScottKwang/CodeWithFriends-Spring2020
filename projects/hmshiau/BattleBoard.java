/**
 * Class BattleBoard - represents a board that contains game pieces
 * @author Hubert Shiau
 */

import java.util.*;

public class BattleBoard {
	
	final static public Random r = new Random();
	
	public static enum AIM_RESULT {SUNK, MISS, HIT}; // stores possible aim results
	
	int[][] board; // stores board, ships represented by size
	GamePiece[] gamePieces; // stores ships

	/**
	 * Constructor BattleBoard - sets game pieces on board
	 */
	public BattleBoard(){
		
		this.board = new int[10][10];
		this.gamePieces = new GamePiece[5];
		
		int rowTry; // stores row coordinate to try
		int columnTry; // stores column coordinate to try
		GamePiece.DIRECTION direction; // stores direction of current ship
		
		// loops until coordinates of five ships determined
		for(int i = 5; i > 0; i--){
		
			// loops until point and direction of one ship determined
			do{
				
				// loops until empty coordinate chosen
				do{
						
					rowTry = r.nextInt(10);
					columnTry = r.nextInt(10);
						
				} while(board[rowTry][columnTry] != 0); // end do
				
				direction = this.getDirection(i, rowTry, columnTry);
			
			} while (direction == GamePiece.DIRECTION.NONE);
			
			this.gamePieces[i - 1] = new GamePiece(i, direction, rowTry, columnTry);
			this.graphCoordinates(this.gamePieces[i - 1].getCoordinates(), i);
		
		} // end for		
		
	} // end constructor
	
	private GamePiece.DIRECTION getDirection(int size, int rowTry, int columnTry){
	// returns valid direction for certain ship based on starting coordinate
		
		boolean determined = false; // stores whether ship direction determined
		GamePiece.DIRECTION direction = null; // stores direction of ship
		boolean[] tried = new boolean[4]; // stores whether directions have been tried
		
		// loops until direction of ship determined
		do{
			
			switch(r.nextInt(4)){
				case 0:
					if(this.isValid(size, rowTry, columnTry, GamePiece.DIRECTION.UP)){
						direction = GamePiece.DIRECTION.UP;
						determined = true;
					} // end if
					else{
						tried[0] = true;
					} // end else
					break;
				case 1:
					if(this.isValid(size, rowTry, columnTry, GamePiece.DIRECTION.RIGHT)){
						direction = GamePiece.DIRECTION.RIGHT;
						determined = true;
					} // end if
					else{
						tried[1] = true;
					} // end else
					break;
				case 2:
					if(this.isValid(size, rowTry, columnTry, GamePiece.DIRECTION.DOWN)){
						direction = GamePiece.DIRECTION.DOWN;
						determined = true;
					} // end if
					else{
						tried[2] = true;
					} // end else
					break;
				case 3:
					if(this.isValid(size, rowTry, columnTry, GamePiece.DIRECTION.LEFT)){
						direction = GamePiece.DIRECTION.LEFT;
						determined = true;
					} // end if
					else{
						tried[3] = true;
					} // end else
					break;
			} // end switch
			
			if(allTried(tried)){
				return GamePiece.DIRECTION.NONE;
			} // end if
				
		} while(determined == false); // end do
		
		return direction;
		
	} // end getDirection
	
	private boolean allTried(boolean[] tried){
	// returns whether all directions have been tried	
			
		for(int i = 0; i < tried.length; i++){
			if(tried[i] == false){
				return false;
			} // end if
		} // end for
			
		return true;
			
	} // end allTried
	
	private boolean isValid(int size, int rowTry, int columnTry, GamePiece.DIRECTION direction){
	// determines if direction for certain ship is valid based on starting coordinate	
		
		for(int k = 1; k < size; k++){	
			switch(direction.ordinal()){
				case 0:
					if((rowTry - k < 0) || (this.board[rowTry - k][columnTry] != 0)){
						return false;
					} // end if
					break;
				case 1:
					if((columnTry + k > 9) || (this.board[rowTry][columnTry + k] != 0)){
						return false;
					} // end if
					break;
				case 2:
					if((rowTry + k > 9) || (this.board[rowTry + k][columnTry] != 0)){
						return false;
					} // end if
					break;
				case 3:
					if((columnTry - k < 0) || (this.board[rowTry][columnTry - k] != 0)){
						return false;
					} // end if
					break;
			} // end switch
		} // end for
		
		return true;
			
	} // end isValid
	
	private void graphCoordinates(int[][] coordinates, int shipSize){
	// graphs given coordinates onto board	
		
		for(int i = 0; i < coordinates[0].length; i++){
			this.board[coordinates[0][i]][coordinates[1][i]] = shipSize;
		} // end for
		
	} // end graphCoordiantes
	
	/**
	 * <b> Summary </b> - aims at given row and column
	 * @param aimedRow - row to aim at
	 * @param aimedColumn - column to aim at
	 * @return MISS if aim does not hit a ship
	 * @return SUNK if aim sinks a ship
	 * @return 'name of ship hit' if aim hits a ship but does not sink
	 */
	public AIM_RESULT aim(int aimedRow, int aimedColumn){
		
		if(this.board[aimedRow][aimedColumn] != 0){
			if(this.gamePieces[this.board[aimedRow][aimedColumn] - 1].hitsLeft() == 1){
				return AIM_RESULT.SUNK;
			} // end if
			else{
				return AIM_RESULT.HIT;
			} // end else
		} // end if
		
		return AIM_RESULT.MISS;
		
	} // end aim
	
	/**
	 * <b> Summary </b> - returns board
	 * @return - returns board
	 */
	public int[][] getBoard(){

		return this.board;

	} // end getBoard
	
	/**
	 * <b> Summary </b> - returns name of ship at given coordinate
	 * @param aimedRow - row to inquire
	 * @param aimedColumn - column to inquire
	 * @return - returns name of ship at given coordinate
	 */
	public String shipAt(int aimedRow, int aimedColumn){
		
		return this.gamePieces[this.board[aimedRow][aimedColumn] - 1].toString();
		
	} // end shipAt
	
	/**
	 * <b> Summary </b> - sinks ship, sets board value to 0
	 * @param aimedRow - ship row to sink
	 * @param aimedColumn - ship column to sink
	 */
	public void hit(int aimedRow, int aimedColumn){
		
		this.gamePieces[this.board[aimedRow][aimedColumn] - 1].hit(aimedRow, aimedColumn);
		this.board[aimedRow][aimedColumn] = 0;
		
	} // end sinkShip
	
	private void displayBoard(){
	// prints the location of each ship on the board
			
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				System.out.print(this.board[i][j] + "    ");
			} // end for
			System.out.println();
		} // end for
			
	} // end displayBoard
	
	public static void main(String[] args){
		
		BattleBoard test = new BattleBoard();
		test.displayBoard();
		
	} // end main
	
}