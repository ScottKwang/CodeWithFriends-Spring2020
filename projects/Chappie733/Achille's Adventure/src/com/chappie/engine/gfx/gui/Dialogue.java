package com.chappie.engine.gfx.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.chappie.engine.math.Vector2i;
import com.chappie.engine.time.Timer;
import com.chappie.main.Game;

public class Dialogue {
	
	private List<DialogueBox> dialogues;
	private List<Timer> delays;
	private int curr_dialogue;
	
	public Dialogue(List<String> content, List<Long> delays) {
		dialogues = new LinkedList<DialogueBox>();
		for (String str : content) {
			DialogueBox box = new DialogueBox(new Vector2i(50, Game.HEIGHT*3/5), new Vector2i(700, Game.HEIGHT*2/5-50));
			box.setText(str);
			dialogues.add(box);
		}
		this.delays = new ArrayList<Timer>();
		for (long l : delays)
			this.delays.add(new Timer(l));
		curr_dialogue = 0;
	}
	
	public Dialogue(List<String> content, long delay) {
		dialogues = new LinkedList<DialogueBox>();
		for (String str : content) {
			DialogueBox box = new DialogueBox(new Vector2i(50, Game.HEIGHT*3/5), new Vector2i(700, Game.HEIGHT*2/5-50));
			box.setText(str);
			dialogues.add(box);
		}
		delays = new ArrayList<Timer>();
		delays.add(new Timer(delay));
		curr_dialogue = 0;
	}
	
	public void update() {
		if (isOver()) return;
		dialogues.get(curr_dialogue).update(); // update the current dialog
		delays.get((delays.size()>1)?curr_dialogue:0).update(); // update the timer
		
		if (delays.get((delays.size()>1)?curr_dialogue:0).isOver()) {// if it's over
			++curr_dialogue; // pass to the next dialog box
			if (delays.size()==1) 
				delays.get(0).restart();
		}
	}
	
	public void render(Graphics2D g) {
		if (!isOver())
			dialogues.get(curr_dialogue).render(g);
	}
	
	public boolean isOver() {
		return curr_dialogue == dialogues.size();
	}
	
}
