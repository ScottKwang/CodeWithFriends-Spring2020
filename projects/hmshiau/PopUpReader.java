// Hubert Shiau
// Pop Up Reader Class

import javax.swing.*;

public class PopUpReader {

	public static int readInt(String message){
	// returns string as int	
		
		return Integer.parseInt(readString(message));
		
	} // end readInt
	
	public static double readDouble(String message){
	// returns string as double
		
		return Double.parseDouble(readString(message));
		
	} // end readDouble
	
	public static char readChar(String message){
	// returns string as char
		
		return readString(message).charAt(0);
		
	} // end readChar
	
	public static String readString(String message){
	// prompts user for int, double, or char	
		
		return JOptionPane.showInputDialog(null, message);
		
	} // end readString
	
}