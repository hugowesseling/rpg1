package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashSet;

import com.aquarius.rpg1.behavior.ObjectAction;
import com.aquarius.rpg1.behavior.CharacterBehavior;

public class GameObject implements CharacterBehavior, Serializable
{
	private static final long serialVersionUID = 4512007766793306312L;
	protected ObjectPosition position;
	private ObjectAction action;
	private Direction direction;
	private Int2d movement;
	float health;
	protected transient HashSet<InteractionPossibility> interactionPossibilities;
	protected String name;
	protected Weapon weapon = null;
	protected ObjectDrawer objectDrawer;

	public GameObject(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		this("noname", objectDrawer, position, direction);
	}
	
	public GameObject(String name, ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super();
		this.name = name;
		this.objectDrawer = objectDrawer;
		this.position = position;
		this.direction = direction;
		this.action = null;
		this.movement = new Int2d(0,0);
		this.health = 0;
		this.interactionPossibilities = new HashSet<>();
		System.out.println("GameCharacter: Constructor position: "  +position + ", for name " + name);
	}

	public GameObject() {
		this("noname2", new CharacterDrawer(new CharacterTileSet(new Int2d(3,1))), new ObjectPosition(100,100), Direction.EAST);
	}
	public HashSet<InteractionPossibility> getInteractionPossibilities() {
		return interactionPossibilities;
	}

	public void draw(Graphics2D graphics, int frameCounter, int screenx, int screeny)
	{
		objectDrawer.draw(graphics, position.x - screenx, position.y - screeny, direction, frameCounter / 10);
		if(weapon != null) {
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

	/*
	@Override
	public void writeSaveState(FileOutputStream fileOutputStream) {
		// TODO Auto-generated method stub
	}

	@Override
	public void readSaveState(FileInputStream fileInputStream) {
		// TODO Auto-generated method stub
	}*/

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setMovement(Int2d movement) {
		this.movement = movement;
	}

	public void doMovement() {
		position.add(movement);
	}
	
	public void doActionAndWeapon(WorldState worldState, LevelState levelState) {
		if(action != null) {
			if(action.doAction(worldState)) {
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
	private void writeObject(java.io.ObjectOutputStream oos) throws IOException {
		System.out.println("Writing position: "  + position + ", for name " + name);
		oos.defaultWriteObject();
		//stream.writeObject(name);
		//stream.writeObject(position);
		//stream.writeObject(direction);
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		//name = (String)ois.readObject();
		//position = (CharacterPosition) ois.readObject();
		//direction = (Direction) ois.readObject();
		System.out.println("Read position: "  +position + ", for name " + name);
	}

	public void useWeapon() {
		if(weapon != null) {
			weapon.startUse();
		}
	}

	@Override
	public void think(Player player, WorldState worldState, LevelState levelState) {
		
	}	
	
	public Int2d getCollisionBounce(GameObject other_char) {
		// Never collide with yourself
		if(other_char != this) {
			int rx = other_char.position.x - position.x, ry = other_char.position.y - position.y;
			int radius = 10;
			if(rx *rx + ry *ry < radius*radius) {
				System.out.println("Character " + other_char.name + " in range");
				double dist = Math.hypot(rx, ry); // dist < radius
				return new Int2d((int)(-rx * (radius-dist) / dist), (int)(-ry * (radius-dist) / dist));
			}
		}
		return null;
	}
}
