package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.util.Vector;

import com.aquarius.common2dgraphics.util.Input;
import com.aquarius.rpg1.AudioSystemPlayer.RandomSound;
import com.aquarius.rpg1.objects.GameObject;
import com.aquarius.rpg1.objects.StorableObjectType;
import com.aquarius.rpg1.weapons.BeamWeapon;
import com.aquarius.rpg1.weapons.Weapon;

public class Player extends GameObject {
	private static final long serialVersionUID = 1752542798493227606L;
	private static final int PLAYER_BEGIN_HEALTH = 12;
	private GameObject interactionGameObject = null;
	public Bag<String> inventory;
	private InteractionPossibility interactionPossibilty;
	private StorableObjectType itemAbovePlayerStorableObjectType = null;
	private int itemAbovePlayerTimer = 0;
	private int cooldown = 0;
	private int walkSoundCounter = 0;
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
	public void think(Player player, WorldTime worldState, LevelState levelState) {
		if(cooldown>0)
			cooldown --;
		
	}
	private void setInteractionPossibility(InteractionPossibility interactionPossibilty) {
		this.interactionPossibilty = interactionPossibilty;
		
	}
	public void checkIfTouching(LevelState levelState) {
		for(GameObject gameObject: levelState.allGameObjects) {
			if(colliding(gameObject)) {
				System.out.println(gameObject.getName() + " is colliding");
				if(gameObject.getInteractionPossibilities().contains(InteractionPossibility.TOUCH)) {
					System.out.println("Touching " + gameObject.getName());
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
	public void doDamage(LevelState levelState, int damage) {
		if(cooldown == 0 && damage>0) {
			health -= damage;
			if(health <= 0) {
				levelState.resetToBeginOfGame();
				health = PLAYER_BEGIN_HEALTH; 
				AudioSystemPlayer.playRandom(RandomSound.MALE_DEATH);
			}else
				AudioSystemPlayer.playRandom(RandomSound.MALE_GRUNT_PAIN);
			cooldown  = 5;
		}
	}
	
	public void setBeginOfGamePosition() {
		position = ObjectPosition.createFromTilePosition(new Int2d(13, 30));
	}
	public void setWeapon(Weapon newWeapon) {
		weapon = newWeapon;
	}
	
	public void inputPlayerMovement(Input input, LevelState levelState) {
		int dx = 0, dy = 0;
		if(input.buttons[Input.LEFT])
		{
			dx = -2;
			setDirection(Direction.WEST);
		}
		if(input.buttons[Input.RIGHT])
		{
			dx = 2;
			setDirection(Direction.EAST);
		}
		if(input.buttons[Input.UP])
		{
			dy = -2;
			setDirection(Direction.NORTH);
		}
		if(input.buttons[Input.DOWN])
		{
			dy = 2;
			setDirection(Direction.SOUTH);
		}
		//Add dx,dy from colliding with other characters
		int bouncedx = 0, bouncedy = 0, bouncecount =0;
		for(GameObject gameObject: levelState.allGameObjects) {
			Int2d bounce = getCollisionBounce(gameObject);
			if(bounce != null)
			{
				bouncedx += bounce.x;
				bouncedy += bounce.y;
				bouncecount ++;
				doDamage(levelState, gameObject.getDamage());
			}
		}
		if(bouncecount != 0)
		{
			dx += bouncedx / bouncecount;
			dy += bouncedy / bouncecount;
		}

		
		boolean playerCouldMove = moveAndLevelCollide(levelState, dx, dy);
		
		if(playerCouldMove && (dx!=0 || dy!=0)){
			
			walkSoundCounter--;
			if(walkSoundCounter <= 0) {
				
				int walkSoundIndex = (int )(Math.random() * 12) + 1;
				String audioFileName = String.format(AudioSystemPlayer.AUDIO_FOLDER + "Footsteps\\footstep_dirt_walk_run_%02d.wav", 
						walkSoundIndex);
				AudioSystemPlayer.playSound(audioFileName, false);
				walkSoundCounter = 15;
			}
		}else {
			walkSoundCounter = 0;
		}
		levelState.loadLevelByPlayerPosition(this);
	}

}
