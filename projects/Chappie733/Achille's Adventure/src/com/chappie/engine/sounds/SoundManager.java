package com.chappie.engine.sounds;

import java.util.ArrayList;
import java.util.List;

import com.chappie.engine.time.Timer;
import com.chappie.game.settings.Settings;

public class SoundManager {
	
	private static List<Sound> sounds;
	private static List<Integer> scheduled;
	private static int curr_scheduled_track;
	private static Timer schedule_wait_time;
	private static float sound_multiplier;
	
	public static void init() {
		sounds = new ArrayList<Sound>();
		scheduled = new ArrayList<Integer>();
		curr_scheduled_track = 0;
		sound_multiplier = 1;
	}
	
	public static void PlaySound(int index) {
		sounds.get(index).restart();
	}
	
	public static void StopSound(int index) {
		sounds.get(index).stop();
	}
	
	public static void updateSchedule() {
		if (scheduled.isEmpty()) return;
		if (sounds.get(scheduled.get(curr_scheduled_track)).isOver()) {
			if (schedule_wait_time.hasStarted()) schedule_wait_time.update();
			else schedule_wait_time.start();
			if (curr_scheduled_track != scheduled.size()-1 && schedule_wait_time.isOver()) {
				++curr_scheduled_track;
				PlaySound(scheduled.get(curr_scheduled_track));
				schedule_wait_time.restart();
			}
		}
	}
	
	/**
	 * @param path the path of the file to be loaded
	 * @return an integer representing the sound, used to access the sound in the sound manager
	 */
	public static int LoadSound(String name) {
		sounds.add(new Sound("res/sounds/" + name));
		return sounds.size()-1;
	}
	
	public static void setVolume(int volume) {
		for (Sound s : sounds)
			s.setVolume((int) (volume * sound_multiplier));
	}
	
	public static void setVolumeMultiplier(float multiplier) {
		for (Sound s : sounds)
			s.setVolume((int) (s.getVolume()/sound_multiplier * multiplier));
		SoundManager.sound_multiplier = multiplier;
	}
	
	public static void setVolume(int volume, List<Integer> tracks) {
		for (int i : tracks)
			sounds.get(i).setVolume((int) (volume * sound_multiplier));
	}
	
	public static void dispose() {
		for (Sound s : sounds)
			s.dispose();
	}
	
	public static void addtoSchedule(int index) {
		scheduled.add(index);
	}
	
	public static void addtoSchedule(List<Integer> tracks) {
		scheduled.addAll(tracks);
	}
	
	public static void setScheduleSoundDelay(long millis) {
		schedule_wait_time = new Timer(millis, false);
	}
	
	public static void startSchedule() {
		PlaySound(scheduled.get(curr_scheduled_track));
	}
	
	public static void setScheduledVolume(int volume) {
		for (int i : scheduled)
			sounds.get(i).setVolume((int) (volume * sound_multiplier));
		Settings.setMusicVolume(volume);
	}
	
	public static void pause(int sound) {
		sounds.get(sound).pause();
	}	
	
	public static void unpause(int sound) {
		sounds.get(sound).unpause();
	}
	
	public static void pauseSchedule() {
		sounds.get(scheduled.get(curr_scheduled_track)).pause();
	}
	
	public static void unpauseSchedule() {
		sounds.get(scheduled.get(curr_scheduled_track)).unpause();
	}
	
	
}
