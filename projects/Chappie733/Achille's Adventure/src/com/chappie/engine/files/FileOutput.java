package com.chappie.engine.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Handles all the file Output required from the game
 * 
 * @author Chappie
 * @since 3/5/2020
 */
public class FileOutput {
	
	public static boolean createFile(String path) {
		File file = new File(path);
		file.mkdirs();
		if (!file.exists()) {
			try {
				file.createNewFile(); 
				return true;
			} 
			catch (IOException e) { 
				e.printStackTrace(); 
				System.err.println("The file at path " + path + " already existed");
			}
		}
		return false;
	}
	
	public static boolean writeOnFile(String file, String content) {
		BufferedWriter f_stream;
		try {
			f_stream = new BufferedWriter(new FileWriter(new File(file)));
			f_stream.write(content);
			f_stream.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
