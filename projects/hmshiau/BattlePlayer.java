/**
 * Class BattlePlayer - represents a named Player who contains a BattleBoard
 * @author Hubert Shiau
 */

public class BattlePlayer {
	
	public static enum STATUS {NOT_TRIED, MISS, SUBMARINE, DESTROYER, CRUISER, BATTLESHIP, AIRCRAFT_CARRIER }; // stores coordinate status of player's attack board

	private String name; // stores name of player
	private BattleBoard board; // stores players battleship board
	private STATUS[][] attackBoard; // stores players attack board
	private int shipsLeft; // stores number of floating ships left for player
	
	/**
	 * Constructor BattlePlayer - sets name, initializes attack board, initializes number of floating ships player has
	 * @param name - stores name of player
	 */
	public BattlePlayer(String name){
		
		this.name = name;
		this.board = new BattleBoard();
		this.attackBoard = new STATUS[10][10];
		this.shipsLeft = 5;
		
		// initializes attack board
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				this.attackBoard[i][j] = STATUS.NOT_TRIED;
			} // end for
		} // end for
		
	} // end constructor
	
	/**
	 * <b> Summary </b> - allows player to aim at board of another player
	 * @param otherPlayer - represents player to aim at
	 * @param aimedRow - represents row to aim at
	 * @param aimedColumn - represents column to aim at
	 */
	public void aim(BattlePlayer otherPlayer, char aimedRowChar, int aimedColumn){
		
		aimedColumn--;
		int aimedRow = "ABCDEFGHIJ".indexOf(aimedRowChar); // stores integer version of aimed row
		
		if(otherPlayer.board.aim(aimedRow, aimedColumn) == BattleBoard.AIM_RESULT.SUNK){
			this.setAttackBoard(aimedRow, aimedColumn, STATUS.values()
					[otherPlayer.board.getBoard()[aimedRow][aimedColumn] + 1]);
			System.out.println("You sunk the " + otherPlayer.board.shipAt(aimedRow, aimedColumn) + ".");
			otherPlayer.sink();
			otherPlayer.board.hit(aimedRow, aimedColumn);
		} // end else if
		else if(otherPlayer.board.aim(aimedRow, aimedColumn) == BattleBoard.AIM_RESULT.MISS){
			this.setAttackBoard(aimedRow, aimedColumn, STATUS.MISS);
			System.out.println("Miss");
		} // end else
		else{
			this.setAttackBoard(aimedRow, aimedColumn, STATUS.values()[otherPlayer.board.getBoard()[aimedRow][aimedColumn] + 1]);
			System.out.println("You hit the " + otherPlayer.board.shipAt(aimedRow, aimedColumn) + ".");			
			otherPlayer.board.hit(aimedRow, aimedColumn);
		} // end else
	
	} // end aim
	
	private void setAttackBoard(int row, int column, STATUS status){
	// updates attack board based on outcome of aim
		
		this.attackBoard[row][column] = status;
		
	} // end setAttackBoard

	/**
	 * <b> Summary </b> - displays attack board, ships hits represented by size
	 */
	public void displayAttackBoard(){
		
		System.out.println("    1    2    3    4    5    6    7    8    9    10");
		
		for(int i = 0; i < 10; i++){
			System.out.print("ABCDEFGHIJ".charAt(i) + "   ");
			for(int j = 0; j < 10; j++){
				switch(this.attackBoard[i][j].ordinal()){
					case 0:
						System.out.print("-");
						break;
					case 1:
						System.out.print("0");
						break;
					default:
						System.out.print(this.attackBoard[i][j].ordinal() - 1);
						break;
				} // end switch
				System.out.print("    ");
			} // end for
			System.out.println();
		} // end for
		
		System.out.println();
		
	} // end displayAttackBoard
		
	/**
	 * <b> Summary </b> - returns number of ships floating for player
	 * @return - returns number of ships floating for player
	 */
	public int shipsLeft(){
		
		return this.shipsLeft;
		
	} // end getShipsleft
	
	/**
	 * <b> Summary </b> - sinks one ships, decreases number of ships
	 */
	public void sink(){
		
		this.shipsLeft--;
		
	} // end sink
	
	/**
	 * <b> Summary </b> - returns true if coordiante has already been fired at
	 * @param coordinate
	 * @return
	 */
	public boolean isValid(String coordinate){
		
		// checks that voordinate is valid
		if((coordinate.length() < 2) || (coordinate.length() > 3)){
			return false;
		} // end if
		if("ABCDEFGHIJ".indexOf(coordinate.charAt(0)) == -1){
			return false;
		} // end if
		int columnAim = Integer.parseInt(coordinate.substring(1)); // stores column aimed at
		if((columnAim < 1) || (columnAim > 10)){
			return false;
		} // end if
		
		// checks for repeats
		int row = "ABCDEFGHIJ".indexOf(coordinate.charAt(0));
		int column = Integer.parseInt(coordinate.substring(1)) - 1;
		
		if(this.attackBoard[row][column] != STATUS.NOT_TRIED){
			return false;
		} // end if
		else{
			return true;
		} // end else
		
	} // end repeat
	
	/**
	 * <b> Summary </b> - returns name of player
	 */
	public String toString(){
		
		return this.name;
		
	} // end toString();

}