package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.util.Vector;

import com.aquarius.rpg1.AudioSystemPlayer.RandomSound;

public class Player extends GameObject {
	private static final long serialVersionUID = 1752542798493227606L;
	private static final int PLAYER_BEGIN_HEALTH = 12;
	Vector<CarryableItem> itemsCarried;
	private GameObject interactionGameObject = null;
	public Bag<String> inventory;
	private InteractionPossibility interactionPossibilty;
	private StorableObjectType itemAbovePlayerStorableObjectType = null;
	private int itemAbovePlayerTimer = 0;
	private int cooldown = 0;
	public Player(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("player", objectDrawer, position, direction);
		health = PLAYER_BEGIN_HEALTH;
		weapon = new BeamWeapon(this);
		inventory = new Bag<>();
	}
	public GameObject getInteractionGameObject() {
		return interactionGameObject;
	}
	public void setInteractionGameObject(GameObject interactionGameObject) {
		this.interactionGameObject = interactionGameObject;
	}
	
	public void determineInteractionGameObject(Vector<GameObject> allGameObjects) {
		setInteractionGameObject(null);
		for(GameObject gameObject: allGameObjects)
		{
			if(hasInSight(gameObject.getPosition(), 64))
			{
				//System.out.println("hasInSight: " + gameObject.name);
				if(gameObject.getInteractionPossibilities().contains(InteractionPossibility.TALK)) {
					setInteractionGameObject(gameObject);
					setInteractionPossibility(InteractionPossibility.TALK);
					//System.out.println("Player has in sight!");
				}else
				if(gameObject.getInteractionPossibilities().contains(InteractionPossibility.OPEN)) {
					setInteractionGameObject(gameObject);
					setInteractionPossibility(InteractionPossibility.OPEN);
					//System.out.println("Player has in sight!");
				}else					
				if(gameObject.getInteractionPossibilities().contains(InteractionPossibility.UNLOCK)) {
					setInteractionGameObject(gameObject);
					setInteractionPossibility(InteractionPossibility.UNLOCK);
					//System.out.println("Player has in sight!");
				}					
				
			}
		}
	}
	
	@Override
	public void think(Player player, WorldState worldState, LevelState levelState) {
		if(cooldown>0)
			cooldown --;
		
	}
	private void setInteractionPossibility(InteractionPossibility interactionPossibilty) {
		this.interactionPossibilty = interactionPossibilty;
		
	}
	public void checkIfTouching(LevelState levelState) {
		for(GameObject gameObject: levelState.allGameObjects) {
			if(colliding(gameObject)) {
				System.out.println(gameObject.name + " is colliding");
				if(gameObject.getInteractionPossibilities().contains(InteractionPossibility.TOUCH)) {
					System.out.println("Touching " + gameObject.name);
					gameObject.doTouchAction(levelState, this);
				}
			}
		}
	}
	@Override
	public void doTouchAction(LevelState levelState, Player player) {
		System.out.println("Don't touch yourself!");
	}
	
	public InteractionPossibility getInteractionPossiblity() {
		return interactionPossibilty;
	}
	public void startItemAbovePlayerAnimation(StorableObjectType storableObjectType) {
		System.out.println("Starting item above player animation");
		itemAbovePlayerStorableObjectType = storableObjectType;
		itemAbovePlayerTimer = 0;
	}
	@Override
	public void draw(Graphics2D graphics, int frameCounter, int screenx, int screeny, boolean simulating) {
		if(cooldown == 0 || (frameCounter % 2 == 0)) {
			super.draw(graphics, frameCounter, screenx, screeny, simulating);
		}
		if(itemAbovePlayerStorableObjectType != null) {
			System.out.println("Showing item above: " + itemAbovePlayerStorableObjectType.name + ":" + itemAbovePlayerTimer);
			graphics.drawImage(Resources.itemTileSet.getTileImageFromIndexSafe(itemAbovePlayerStorableObjectType.itemTileIndex), position.x - screenx - 8, position.y - screeny - itemAbovePlayerTimer - 30, null);
			itemAbovePlayerTimer ++;
			if(itemAbovePlayerTimer > 40) {
				itemAbovePlayerStorableObjectType = null;
			}
		}
	}
	
	@Override
	public void getDamage(LevelState levelState, int damage) {
		if(cooldown == 0 && damage>0) {
			health -= damage;
			if(health <= 0) {
				setBeginPosition(levelState);
				health = PLAYER_BEGIN_HEALTH; 
				AudioSystemPlayer.playRandom(RandomSound.MALE_DEATH);
			}else
				AudioSystemPlayer.playRandom(RandomSound.MALE_GRUNT_PAIN);
			cooldown  = 5;
		}
	}
	
	public void setBeginPosition(LevelState levelState) {
		levelState.setLevelPos("level", new Int2d(500,500));
		position = ObjectPosition.createFromTilePosition(new Int2d(13, 30));
	}
	
}
