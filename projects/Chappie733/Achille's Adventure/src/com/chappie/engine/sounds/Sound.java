package com.chappie.engine.sounds;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import com.chappie.engine.files.Loader;

public class Sound {
	
	private Clip clip;
	private FloatControl volume;
	private float last_volume; // volume before pause
	private int last_frame; // frame at which the sound was paused
	
	public Sound(String path) {
		clip = Loader.LoadSound(path);
		volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	}
	
	public void restart() {
		clip.setFramePosition(0);
		play();
	}
	
	public float getLengthAsSeconds() {
		return (float)clip.getMicrosecondLength()/1000000f;
	}
	
	public void play() {
		clip.start();
	}
	
	public void stop() {
		clip.stop();
	}
	
	public boolean isOver() {
		return !clip.isRunning();
	}
	
	public void setVolume(int percentage) {
		double max = volume.getMaximum();
		double min = volume.getMinimum();
		volume.setValue((float) (min+((max-min)/100*percentage)));
	}
	
	public int getVolume() {
		// volume percentage = (100*({clip volume}-min))/(max-min)
		double max = volume.getMaximum();
		double min = volume.getMinimum();
		return (int) ((100*(volume.getValue()-min))/(max-min));
	}
	
	public void dispose() {
		clip.close();
	}
	
	public void pause() {
		last_frame = clip.getFramePosition();
		last_volume = volume.getValue();
		volume.setValue(volume.getMinimum());
	}
	
	public void unpause() {
		clip.setFramePosition(last_frame);
		volume.setValue(last_volume);
	}
	
}
