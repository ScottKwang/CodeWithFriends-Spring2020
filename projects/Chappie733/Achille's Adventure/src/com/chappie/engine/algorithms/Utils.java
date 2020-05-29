package com.chappie.engine.algorithms;

import com.chappie.engine.math.Vector2i;

public class Utils {
	
	/**
	 * Note: the x and y coordinates will be flipped in the subset, so if a point was (x,y) in the
	 * original set, in the sub set it's (y,x)
	 * 
	 * @param arr the original set
	 * @param subPos the starting position of the bidimensional sub set
	 * @param subSize the size of the subset
	 * @return the bidimensional set that is a subset of the original in the given position and size
	 */
	public static int[][] getSubArray(int arr[][], Vector2i subPos, Vector2i subSize) {
		int result[][] = new int[subSize.getX()][subSize.getY()];
		for (int y = subPos.getY(); y < subPos.getY()+subSize.getY(); y++) {
			for (int x = subPos.getX(); x < subPos.getX()+subSize.getX(); x++) {
				result[x-subPos.getX()][y-subPos.getX()] = arr[x][y];
			}
		}
		return result;
	}
	
	/**
	 * Note: the x and y coordinates will be flipped in the subset, so if a point was (x,y) in the
	 * original set, in the sub set it's (y,x)
	 * 
	 * @param arr the original set
	 * @param subPos the starting position of the bidimensional sub set
	 * @param subSize the size of the subset
	 * @return the bidimensional set that is a subset of the original in the given position and size
	 */
	public static boolean[][] getSubArray(boolean arr[][], Vector2i subPos, Vector2i subSize) {
		boolean result[][] = new boolean[subSize.getX()][subSize.getY()];
		for (int y = subPos.getY(); y < subPos.getY()+subSize.getY(); y++) {
			for (int x = subPos.getX(); x < subPos.getX()+subSize.getX(); x++) {
				result[x-subPos.getX()][y-subPos.getX()] = arr[x][y];
			}
		}
		return result;
	}
	
	
}
