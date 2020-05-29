package com.chappie.engine.files;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.chappie.engine.gfx.animations.Animation;
import com.chappie.engine.math.Vector2f;

/**
 * Handles all the Input required from the game
 * 
 * @author Chappie
 * @since 2/5/2020
 */
public class Loader {
	
	public static BufferedImage LoadImage(String name) {
		File image = new File("res/textures/" + name);
		try {
			return ImageIO.read(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage LoadImageWithAbsolutePath(String name) {
		File image = new File(name);
		try {
			return ImageIO.read(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String LoadFile(String path) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(new File(path)));
		StringBuilder content = new StringBuilder();
		String curr_line = "";
		while ((curr_line = r.readLine()) != null)
			content.append(curr_line + "\n");
		r.close();
		return content.toString();
	}
	
	public static Animation LoadAnimation(String folder, long delay, boolean looping) {
		File f = new File(folder);
		String[] files = f.list();
		BufferedImage[] frames = new BufferedImage[files.length];
		for (int i = 0; i < files.length; i++)
			frames[i] = LoadImageWithAbsolutePath(folder + "/" + files[i]);
		return new Animation(frames, delay, looping);
	}
	
	public static Animation LoadAnimationWithScaling(String folder, long delay, boolean looping) {
		File f = new File(folder);
		String[] files = f.list();
		BufferedImage[] frames = new BufferedImage[files.length];
		Vector2f[] scalings = new Vector2f[frames.length];
		for (int i = 0; i < files.length; i++) {
			frames[i] = LoadImageWithAbsolutePath(folder + "/" + files[i]);
			scalings[i] = new Vector2f((float)frames[0].getWidth()/frames[i].getWidth(), 
									   (float)frames[0].getHeight()/frames[i].getHeight());
		}
		return new Animation(frames, delay, looping, scalings);
	}
	
	public static Animation LoadAnimationWithScaling(String folder, String scalingData, long delay, boolean looping) {
		File f = new File(folder);
		String[] files = f.list();
		BufferedImage[] frames = new BufferedImage[files.length-1];
		String scalingDataT = null;
		try {
			scalingDataT = LoadFile(scalingData);
		} catch (IOException e) { e.printStackTrace(); }
		String lines[] = scalingDataT.split("\n");
		Vector2f[] scalings = new Vector2f[frames.length];
		for (int i = 0; i < lines.length; i++) {
			String data[] = lines[i].split("\\s+");
			scalings[i] = new Vector2f(Float.parseFloat(data[0]), Float.parseFloat(data[1]));
		}
		
		boolean passedScaling = false;
		for (int i = 0; i < files.length; i++) {
			if (!scalingData.equals(folder + "/" + files[i]))
				frames[i-((passedScaling)?1:0)] = LoadImageWithAbsolutePath(folder + "/" + files[i]);
			else passedScaling = true;
		}
		return new Animation(frames, delay, looping, scalings);
	}
	
	public static Clip LoadSound(String path) {
		Clip audio = null;
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(path));
			audio = AudioSystem.getClip();
			audio.open(stream);
		} catch (LineUnavailableException e) { e.printStackTrace(); } catch (UnsupportedAudioFileException e) {
			e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
		return audio;
	}
	
}
