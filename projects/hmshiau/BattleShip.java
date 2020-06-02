/**
 * Class BattleShip - manages game of BattleShip between two players
 * @author Hubert Shiau
 */

import java.util.*;

public class BattleShip {

	private BattlePlayer player1; // represents one player
	private BattlePlayer player2; // represents another player
	private int first; // stores the player that goes first
	
	/**
	 * Constructor BattleShip - sets up battle players
	 */
	public BattleShip(){
		
		Random r = new Random();
		
		this.player1 = new BattlePlayer(PopUpReader.readString("Enter Name of Player 1: "));
		this.player2 = new BattlePlayer(PopUpReader.readString("Enter Name of Player 2: "));
		this.first = r.nextInt(2) + 1;
		
	} // end constructor
	
	/**
	 * <b> Summary </b> - manages game between two players
	 */
	public void playGame(){
		
		// loops until game is over
		do{
			
			if(first == 1){
				turn(this.player1, this.player2);
				turn(this.player2, this.player1);
			} // end if
			else{
				turn(this.player2, this.player1);
				turn(this.player1, this.player2);
			} // end else
			
		} while((this.player1.shipsLeft() > 0) && (this.player2.shipsLeft() > 0)); // end do
		
		if(this.player1.shipsLeft() > this.player2.shipsLeft()){
			System.out.println(player1 + " is the winner!");
		} // end if
		else if(this.player1.shipsLeft() < this.player2.shipsLeft()){
			System.out.println(player2 + " is the winner!");
		} // end else if
		else{
			System.out.println("Draw Game!");
		} // end else
		
	} // end playGame
	
	// pauses until a key is pressed
	private void pause(){
		
		System.out.println("\nPress Any Key to Continue\n");
		Scanner in = new Scanner(System.in);
		in.nextLine();
		
	} // end pause
	
	// operates one turn of player	
	private void turn(BattlePlayer player, BattlePlayer aimAt){
		
		System.out.println("---------------------------------------------------");
		System.out.println(player + "'s Attack Board:\n");
		player.displayAttackBoard();
		
		// prompts for coordinate
		String coordinate = null; // stores coordinate to aim at
		char aimedRow; // stores row to aim at
		int aimedColumn; // stores column to aim at
		
		// loops until valid coordinates given
		boolean valid = true; // stores whether entered input is valid
		String message; // stores popup message
		do{
			
			message = player + ", Enter a Coordinate to Aim At:";
			
			if(!valid){
				message = "Invalid Coordinate Entered (No Repeats), Please Try Again: " + message;
			} // end else if
		
			valid = true;
			coordinate = PopUpReader.readString(message);
			
			System.out.print(coordinate + " - ");
			if(!player.isValid(coordinate)){
				valid = false;
				System.out.println(" Invalid");
			} // end if
			
		} while(!valid); // end do
		
		aimedRow = coordinate.charAt(0);
		aimedColumn = Integer.parseInt(coordinate.substring(1));
		
		player.aim(aimAt, aimedRow, aimedColumn);
		
		pause();
		
	} // end turn
	
}