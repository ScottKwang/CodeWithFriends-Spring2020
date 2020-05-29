package com.chappie.game.settings;

/**
 * Used to store all the settings for the initialization of the OptionsState
 * and for a quick usage from every component of the game
 * 
 * @author Chappie
 * @since 10/05/2020 (dd/mm/yyyy)
 */
public class Settings {
	
	private static int master_volume = 100;
	private static int music_volume = 100;
	
	public static int getMusicVolume() {
		return master_volume;
	}
	
	public static int getVolume() {
		return master_volume;
	}

	public static void setMusicVolume(int music_volume) {
		Settings.music_volume = music_volume;
	}
	
	public static void setMasterVolume(int master_volume) {
		Settings.master_volume = master_volume;
	}
	
}
