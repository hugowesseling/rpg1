package com.aquarius.rpg1.objects;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;

import com.aquarius.rpg1.behavior.ObjectAction;
import com.aquarius.rpg1.weapons.Weapon;
import com.aquarius.rpg1.AudioSystemPlayer;
import com.aquarius.rpg1.Dialogue;
import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.InteractionPossibility;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.PositionLabel;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.AudioSystemPlayer.RandomSound;
import com.aquarius.rpg1.behavior.CharacterBehavior;

public class GameObject implements CharacterBehavior, Serializable
{
	private static final long serialVersionUID = 4512007766793306312L;
	protected static final int DEFAULT_RADIUS = 20; //18;
	protected ObjectPosition position;
	private ObjectAction action;
	protected Direction direction;
	protected int health;
	protected transient HashSet<InteractionPossibility> interactionPossibilities;
	protected String name;
	protected Weapon weapon = null;
	protected ObjectDrawer objectDrawer;
	protected int damage;
	protected transient int frameDivider;
	public final static int FRAME_DIVIDER_DEFAULT = 10;

	public GameObject(String name, ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super();
		this.name = name;
		this.objectDrawer = objectDrawer;
		this.position = position;
		this.direction = direction;
		damage = 0;
		action = null;
		health = 20;
		frameDivider = FRAME_DIVIDER_DEFAULT;
		System.out.println("new GameObject: position: "  +position + ", name " + name);
		init();
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		System.out.println("GameObject.readObject: position: "  +position + ", name " + name);
		if(this instanceof PositionLabel) {
			System.out.println("Is positionLabel");
		}else {
			System.out.println("Is NOT positionLabel");
		}
		frameDivider = FRAME_DIVIDER_DEFAULT;
		init();
	}

	public HashSet<InteractionPossibility> getInteractionPossibilities() {
		return interactionPossibilities;
	}

	public void draw(Graphics2D graphics, int frameCounter, int screenx, int screeny, boolean simulating)
	{
		if(weapon != null && (direction == Direction.NORTH || direction == Direction.WEST)) {
			weapon.draw(graphics,frameCounter, screenx, screeny);
		}
		if(objectDrawer!=null)
			objectDrawer.draw(graphics, position.x - screenx, position.y - screeny, direction, frameCounter / frameDivider );
		if(!simulating) {
			graphics.drawRect(position.x - screenx, position.y - screeny, 0, 0);
			graphics.drawRect(position.x - screenx - 8, position.y - screeny - 8, 16,16);
		}
		if(weapon != null && !(direction == Direction.NORTH || direction == Direction.WEST)) {
			weapon.draw(graphics,frameCounter, screenx, screeny);
		}
	}

	public ObjectPosition getPosition() {
		return position;
	}

	protected void setAction(ObjectAction action){
		this.action = action;
	}
	
	protected ObjectAction getAction() {
		return action;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void doActionAndWeapon(WorldState worldState, LevelState levelState) {
		if(action != null) {
			if(action.doActionAndCheckIfDone(worldState, levelState)) {
				action = null;
			}
		}
		if(weapon != null) {
			weapon.think(worldState, levelState);
		}
	}

	public boolean hasInSight(ObjectPosition pos2, int maxdistance) {
		// returns true if this character can see something at position position at maximum distance maxdistance
		int maxdistance2 = maxdistance/2;
		Int2d view = getDirection().movement;
		Int2d viewright = new Int2d(view.y, view.x);
		// topleft and bottomright names are correct when the character looks up
		Int2d topleft = new Int2d(position.x - viewright.x*maxdistance2 + view.x*maxdistance, position.y - viewright.y*maxdistance2 + view.y*maxdistance);
		Int2d bottomright = new Int2d(position.x + viewright.x*maxdistance2, position.y + viewright.y*maxdistance2);
		return pos2.inRect(topleft, bottomright);
	}

	public Dialogue startDialog(Player player, WorldState worldState, LevelState levelState) {
		return null;
	}
	public StorableObjectType open() {
		return null;
	}
	public boolean unlock(Player player, LevelState levelState) {
		return false;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		System.out.println("Writing position: "  + position + ", for name " + name);
		oos.defaultWriteObject();
	}
	protected void init() {
		interactionPossibilities = new HashSet<>();
	}


	public void useWeapon() {
		if(weapon != null) {
			weapon.startUse();
		}
	}

	@Override
	public void think(Player player, WorldState worldState, LevelState levelState) {
		
	}
	
	public void doDamage(LevelState levelState, int damage) {
		AudioSystemPlayer.playRandom(RandomSound.FLESH_IMPACT);
		health -= damage;
		if(health<=0) {
			AudioSystemPlayer.playRandom(RandomSound.CREATURE_EMOTE);
		}
	}

	
	public Int2d getCollisionBounce(GameObject gameObject) {
		// Never collide with yourself
		float multiplier = 2;
		if(gameObject != this) {
			int rx = gameObject.position.x - position.x, ry = gameObject.position.y - position.y;
			int radius = DEFAULT_RADIUS;
			if(rx *rx + ry *ry < radius*radius) {
				System.out.println("Character " + gameObject.name + " in range");
				double dist = Math.hypot(rx, ry); // dist < radius
				return new Int2d((int)(-rx * (radius-dist) / dist * multiplier) , (int)(-ry * (radius-dist) / dist * multiplier));
			}
		}
		return null;
	}
	public boolean colliding(GameObject gameObject) {
		if(gameObject != this) {
			int rx = gameObject.position.x - position.x, ry = gameObject.position.y - position.y;
			int radius = DEFAULT_RADIUS;
			if(rx *rx + ry *ry < radius*radius)
				return true;
		}
		return false;
	}

	public void doTouchAction(LevelState levelState, Player player) {
		System.out.println("No touch action defined for: "+name);
	}

	public static boolean collided(int x, int y, LevelState levelState) {
		return levelState.collidesObjectPositionHitbox(x, y, -8, -5, 6, 8);
	}

	public void move(int x, int y) {
		position.x+=x;
		position.y+=y;
	}	
	public boolean moveAndLevelCollide(LevelState levelState, int dx, int dy) {
		// Returns true if movement could be done
		if(dx == 0 && dy == 0)
			return true;
		if(dx != 0 && dy != 0 && !collided(position.x + dx, position.y + dy, levelState)){
			position.x+=dx;
			position.y+=dy;
			return true;
		}
		//try only x movement
		if(dx != 0 && !collided(position.x + dx, position.y, levelState))
		{
			position.x+=dx;
			return true;
		}
		//try only y movement
		if(dy != 0 && !collided(position.x, position.y + dy, levelState))
		{
			position.y+=dy;
			return true;
		}
		//check player's current location
		if(collided(position.x, position.y, levelState))
		{
			//player is collided on the place where he's standing, allow all movements.
			position.x+=dx;
			position.y+=dy;
			return true;
		}
		return false;
	}

	public void setFrameDivider(int frameDivider) {
		this.frameDivider = frameDivider; 
		
	}

	public String getName() {
		return name;
	}

	public int getHealth() {
		return health;
	}

	public void setPosition(ObjectPosition newPosition) {
		position = newPosition;
	}

	public int getDamage() {
		return damage;
	}


	
}

