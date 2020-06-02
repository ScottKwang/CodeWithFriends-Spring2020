/**
 * Class GamePiece - represents one ship in battle ship game 
 * @author Hubert Shiau
 * @version 1.0
 */

public class GamePiece {
	
	public static enum DIRECTION { UP, RIGHT, DOWN, LEFT, NONE }; // represents direction of ship
	
	private int size; // stores size of ship
	private int[][] coordinates; // stores original coordinates of ship
	private int hitsLeft; // stores number of hits left before ship sinks
	
	/**
	 * Constructor GamePiece - sets ship size, direction, and coordinates
	 * @param shipOrdinal - represents ships ordinal
	 * @param direction - represents ships direction
	 * @param startRow - represents ships starting row coordinate
	 * @param startColumn - represents ships starting column coordinate
	 */
	public GamePiece(int size, DIRECTION direction, int startRow, int startColumn){
		
		this.size = size;
		this.hitsLeft = this.size;
		this.coordinates = new int[2][this.size];
		
		// determine coordinates of ship
		for(int i = 0; i < this.size; i++){
			switch(direction.ordinal()){
				case 0:
					this.coordinates[0][i] = startRow - i;
					this.coordinates[1][i] = startColumn;
					break;
				case 1:
					this.coordinates[0][i] = startRow;
					this.coordinates[1][i] = startColumn + i;
					break;
				case 2:
					this.coordinates[0][i] = startRow + i;
					this.coordinates[1][i] = startColumn;
					break;
				case 3:
					this.coordinates[0][i] = startRow;
					this.coordinates[1][i] = startColumn - i;
					break;
			} // end switch
		} // end for

	} // end GamePiece Constructor
	
	/**
	 * <b> Summary </b> - returns coordinates of ship
	 * @return - coordinates of ship
	 */
	public int[][] getCoordinates(){
		
		return this.coordinates;
		
	} // end getCoordinates
	
	/**
	 * <b> Summary </b> - records a hit made to ship
	 * @param row - stores row of hit
	 * @param column - stores column of hit
	 */
	public void hit(int row, int column){
		
		this.hitsLeft--;
	
	} // end hit
	
	/**
	 * <b> Summary </b> - returns number of hits left for ship
	 * @return - number of hits left for ship
	 */
	public int hitsLeft(){
		
		return this.hitsLeft;
		
	} // end hitsLeft
	
	/**
	 * <b> Summary </b> - returns string format of ship name
	 */
	public String toString(){
		
		if(this.size == 1){
			return "Submarine";
		} // end if
		else if(this.size == 2){
			return "Destroyer";
		} // end else if
		else if(this.size == 3){
			return "Cruiser";
		} // end else if
		else if(this.size == 4){
			return "Battleship";
		} // end else if
		else{
			return "Aircraft Carrier";
		} // end else if
 		
	} // end toString
	 
}