package com.chappie.game.states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import com.chappie.engine.files.Loader;
import com.chappie.engine.math.Vector2f;
import com.chappie.engine.sounds.SoundManager;
import com.chappie.game.entities.Player;
import com.chappie.game.entities.enemies.ESoldier;
import com.chappie.game.entities.npcs.NPC;
import com.chappie.game.items.Item;
import com.chappie.game.map.TileMap;
import com.chappie.game.map.World;
import com.chappie.main.Handler;

public class PlayingState extends GameState {
	
	private World world;
	private NPC npc_test;
	
	public PlayingState(GameStateManager gsm, Handler handler) {
		super(gsm, handler);
	}

	@Override
	public void init() {
		TileMap map = new TileMap(64, handler);
		map.LoadMap(Loader.LoadImage("map.png"), "res/files/tile_data_test.td",
				    Loader.LoadImage("structures_map.png"), "res/files/struct_data_test.td");
		Player player = new Player(new Vector2f(64*443, 64*285), 42, 60, handler);
		handler.setMap(map);
		handler.setPlayer(player);
		player.init();
		ESoldier enemy = new ESoldier(new Vector2f(64*442, 64*276), handler);
		enemy.init();
		world = new World(map, player);
		world.init();
		world.logEnemy(enemy);
		handler.setWorld(world);
		Item.LoadIcons();
		npc_test = new NPC(new Vector2f(64*442, 64*275), 42, 60, handler);
		npc_test.init();
		world.logEntity(npc_test);
		SoundManager.addtoSchedule(Arrays.asList(SoundManager.LoadSound("music/music_1.wav"),
												 SoundManager.LoadSound("music/music_2.wav"),
												 SoundManager.LoadSound("music/music_3.wav")));
		SoundManager.setScheduleSoundDelay(25000);
		SoundManager.setScheduledVolume(75);
		SoundManager.startSchedule();
		SoundManager.setVolumeMultiplier(1);
	}
	
	public void onStateEnter() {
		//SoundManager.unpauseSchedule();
	}

	@Override
	public void update() {
		world.update();
		SoundManager.updateSchedule();
	}

	@Override
	public void render(Graphics2D g) {
		world.render(g);
	}

	@Override
	public void onKeyPress(int keyPressed) {}

	@Override
	public void onKeyRelease(int keyReleased) {
		if (keyReleased == KeyEvent.VK_ESCAPE) {
			gsm.setState(State.PAUSED_STATE, false);
		//	SoundManager.pauseSchedule();
		}
	}

	@Override
	public void onMouseClick(int mouseButton) {}

	@Override
	public void onMouseRelease(int mouseButton) {}
	
}
