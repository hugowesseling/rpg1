package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashSet;

import com.aquarius.rpg1.behavior.CharacterAction;
import com.aquarius.rpg1.behavior.CharacterBehavior;

public class GameCharacter implements CharacterBehavior, Serializable
{
	private static final long serialVersionUID = 4512007766793306312L;
	protected CharacterPosition position;
	private CharacterTileSet characterTileSet;
	private CharacterBehavior behavior;
	private CharacterAction action;
	private Direction direction;
	private Int2d movement;
	private HashSet<InteractionPossibility> interactionPossibilities;
	float health;
	protected Dialogue dialogue = null;
	private String name;

	public GameCharacter(CharacterPosition position, CharacterTileSet characterTileSet, Direction direction) {
		this("noname", position, characterTileSet, direction);
	}
	
	public GameCharacter(String name, CharacterPosition position, CharacterTileSet characterTileSet, Direction direction) {
		super();
		this.name = name;
		this.position = position;
		this.characterTileSet = characterTileSet;
		this.direction = direction;
		this.action = null;
		this.behavior = new NothingBehavior();
		this.movement = new Int2d(0,0);
		this.health = 0;
		this.interactionPossibilities = new HashSet<>();
	}
	
	public HashSet<InteractionPossibility> getInteractionPossibilities() {
		return interactionPossibilities;
	}

	public void draw(Graphics2D graphics, int frameCounter, int screenx, int screeny)
	{
		characterTileSet.draw(graphics, position.x - screenx, position.y - screeny, direction, frameCounter / 10);
	}

	public CharacterPosition getPosition() {
		return position;
	}

	public CharacterBehavior getBehavior() {
		return behavior;
	}

	protected void setAction(CharacterAction action){
		this.action = action;
	}
	
	protected CharacterAction getAction() {
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

	@Override
	public void think(Player player, WorldState worldState, LevelState levelState) {
	}

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
	
	public void doAction(WorldState worldState) {
		if(action != null) {
			if(action.doAction(worldState)) {
				action = null;
			}
		}
	}

	public boolean hasInSight(CharacterPosition pos2, int maxdistance) {
		// returns true if this character can see something at position position at maximum distance maxdistance
		int maxdistance2 = maxdistance/2;
		Int2d view = getDirection().movement;
		Int2d viewright = new Int2d(view.y, view.x);
		// topleft and bottomright names are correct when the character looks up
		Int2d topleft = new Int2d(position.x - viewright.x*maxdistance2 + view.x*maxdistance, position.y - viewright.y*maxdistance2 + view.y*maxdistance);
		Int2d bottomright = new Int2d(position.x + viewright.x*maxdistance2, position.y + viewright.y*maxdistance2);
		return pos2.inRect(topleft, bottomright);
	}

	public Dialogue startDialog() {

		return dialogue ;
	}
	private void writeObject(java.io.ObjectOutputStream stream)	throws IOException {
		stream.writeObject(name);
		stream.writeObject(position);
		stream.writeObject(direction);
	}

	private void readObject(ObjectInputStream ois)
		    throws ClassNotFoundException, IOException {
		name = (String)ois.readObject();
		position = (CharacterPosition) ois.readObject();
		direction = (Direction) ois.readObject();
	}
	
}
