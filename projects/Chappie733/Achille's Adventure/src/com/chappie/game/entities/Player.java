package com.chappie.game.entities;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import com.chappie.engine.files.Loader;
import com.chappie.engine.gfx.animations.AnimationManager;
import com.chappie.engine.gfx.gui.GUI;
import com.chappie.engine.gfx.gui.widgets.ProgressBar;
import com.chappie.engine.gfx.particles.ParticleSystem;
import com.chappie.engine.gfx.particles.ParticleType;
import com.chappie.engine.gfx.particles.Particles;
import com.chappie.engine.input.Input;
import com.chappie.engine.math.Vector2f;
import com.chappie.engine.math.Vector2i;
import com.chappie.engine.sounds.SoundManager;
import com.chappie.engine.time.Timer;
import com.chappie.game.entities.components.CCollider;
import com.chappie.game.items.Inventory;
import com.chappie.game.items.Sword;
import com.chappie.game.items.potions.HealingPotion;
import com.chappie.main.Game;
import com.chappie.main.Handler;

public class Player extends Entity {
	
	private static final float DEFAULT_SPEED = 5f;
	// pos -> position on the screen
	private Vector2f gamePos; // gamePos -> actual position in the game
	private Vector2i attack_reach;
	private AnimationManager AM;
	private HashMap<String, Integer> sounds;
	private int base_damage;
	private boolean facingRight;
	private Timer attack_timer;
	
	private ParticleSystem particleSystem;
	private Inventory inventory;
	private GUI inGame;
	
	public Player(Vector2f pos, int width, int height, Handler handler) {
		super(new Vector2f(Game.WIDTH/2-width/2, Game.HEIGHT/2-height/2), width, height, null, handler, DEFAULT_SPEED);
		this.handler = handler;
		this.gamePos = pos;
	}
	
	@Override
	public void init() {
		sounds = new HashMap<String, Integer>();
		addComponent(new CCollider(this, handler));
		handler.getMap().getPosOffSet().add(Vector2f.getDifference(gamePos, pos));
		AM = new AnimationManager();
		AM.setDefaultState((byte) 0);
		LoadAnimations();
		LoadSounds();
		health = 25;
		facingRight = true;
		attack_timer = new Timer(500);
		attack_reach = new Vector2i(handler.getMap().getTileSize(), handler.getMap().getTileSize());
		base_damage = 10;
		InitGUIs();
		particleSystem = new ParticleSystem();
		particleSystem.addParticles(new Particles(new Vector2f(pos.getX(), pos.getY()+height/4).asVector2i(),
												  new Vector2i(width, height/2),
												  400, Loader.LoadImage("particles/health.png"),
												  3, 3, 0.05f));
	}
	
	@Override
	public void update() {
		super.update();
		manageInput();
		AM.update();
		attack_timer.update();
		inventory.update();
		inGame.update();
		particleSystem.update();
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		g.drawImage(AM.getTexture(), (int) pos.getX()+(facingRight?0:width), (int) pos.getY(), 
				(int) ((facingRight?width:-width)*AM.getScaling().getX()), (int) 
				(height*AM.getScaling().getY()), null);
		inventory.render(g);
		inGame.render(g);
		particleSystem.render(g);
	}
	
	private void manageInput() {
		// INVENTORY STUFF
		if (Input.isKeyPressed(KeyEvent.VK_E))
			inventory.setActive(!inventory.isActive());
		if (inventory.isActive()) {
			if (Input.isKeyPressed(KeyEvent.VK_ENTER))
				inventory.setActive(false);
			if (Input.isKeyPressed(KeyEvent.VK_UP))
				inventory.selectPrevious();
			if (Input.isKeyPressed(KeyEvent.VK_DOWN))
				inventory.selectNext();
		}
		if (Input.isKeyPressed(KeyEvent.VK_SPACE)) {
			if (inventory.getSelected() instanceof HealingPotion && health >= 100) {
				SoundManager.PlaySound(sounds.get("error"));
			} else {
				ParticleType particles = inventory.onItemIteraction(this);
				if (particles != ParticleType.EMPTY)
					particleSystem.play(particles.getId());
			}
		}
		
		// ATTACK STUFF
		if (Input.isKeyPressed(KeyEvent.VK_C)) {
			if (attack_timer.isOver()) {
				AM.setState((byte) 2); 
				AM.getAnimation().restart();
				attack_timer.restart();
				speed = new Vector2f(0,0);
				attack_reach.setX(handler.getMap().getTileSize()*((facingRight)?1:-1));
				for (Entity e : handler.getWorld().getEntitiesInArea(gamePos, attack_reach))
					e.damage(base_damage);
			}
		}
		
		// MOVEMENT STUFF
		if (AM.getState() != 2) { // if not attacking
			if (Input.isKeyPressed(KeyEvent.VK_D)) {
				speed.setX(DEFAULT_SPEED);
				AM.setState((byte) 1); 
				facingRight = true;
			}
			else if (Input.isKeyPressed(KeyEvent.VK_A)){
				speed.setX(-DEFAULT_SPEED);
				AM.setState((byte) 1);
				facingRight = false;
			}
			else if (speed.getX() != 0) {
				speed.setX((byte) 0);
				AM.setState((byte) 0); 
			}
			
			if (Input.isKeyPressed(KeyEvent.VK_W)) {
				speed.setY(-DEFAULT_SPEED);
				AM.setState((byte) 1);
			}
			else if (Input.isKeyPressed(KeyEvent.VK_S)) {
				speed.setY(DEFAULT_SPEED);
				AM.setState((byte) 1);
			}
			else if (speed.getY() != 0) {
				speed.setY(0);
				AM.setState((byte) 0);
			}
		}
	}
	
	private void LoadAnimations() {
		AM.addAnimation(Loader.LoadAnimation("res/textures/entities/player/idle", 500, true));
		AM.addAnimation(Loader.LoadAnimation("res/textures/entities/player/walk", 100, true));
		AM.addAnimation(Loader.LoadAnimationWithScaling("res/textures/entities/player/attack", 
														"res/textures/entities/player/attack/scalings.anim", 75, false));
	}
	
	private void LoadSounds() {
		sounds.put("error", SoundManager.LoadSound("feedback/error.wav"));
	}
	
	private void InitGUIs() {
		inventory = new Inventory((byte) 10);
		inventory.init();
		inventory.addItem(new Sword());
		inventory.addItem(new Sword());
		inventory.addItem(new Sword());
		inventory.addItem(new HealingPotion(3));
		inGame = new GUI();
		ProgressBar healthbar = new ProgressBar(new Vector2i(50, 50), new Vector2i(128, 32), 100, 
												health, Loader.LoadImage("gui/ingame/healthbar.png"));
		inGame.addElement(healthbar);
	}
	
	public boolean isDead() { return health <= 0; }
	
	public Vector2i getTilePosition() { 
		return new Vector2i((int) gamePos.getX()/handler.getMap().getTileSize(),
							(int) gamePos.getY()/handler.getMap().getTileSize()); 
	}
	
	public Vector2f getGamePos() {
		return gamePos;
	}
	
	public Vector2f getScreenPos() {
		return pos;
	}
	
	@Override
	public Vector2f getPos() {
		return gamePos;
	}
	
	@Override
	public void damage(int amount) {
		health -= amount;
		ProgressBar b = (ProgressBar) inGame.getElement(0);
		b.setProgress(health);
	}
	
	@Override
	public void addHealth(int amount) {
		health += amount;
		ProgressBar healthBar = (ProgressBar) inGame.getElement(0);
		healthBar.setProgress(health);
	}
	
}