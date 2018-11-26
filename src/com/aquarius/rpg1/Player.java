package com.aquarius.rpg1;

import java.util.Vector;

public class Player extends GameObject {
	private static final long serialVersionUID = 1752542798493227606L;
	Vector<CarryableItem> itemsCarried;
	private GameObject talkActionGameObject = null;
	public Bag<String> inventory;
	public Player(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("player", objectDrawer, position, direction);
		weapon = new Sword(this);
		inventory = new Bag<>();
	}
	public GameObject getTalkActionGameObject() {
		return talkActionGameObject;
	}
	public void setTalkActionGameObject(GameObject talkActionGameObject) {
		this.talkActionGameObject = talkActionGameObject;
	}
	
	public void determineTalkActionCharacter(Vector<GameObject> allGameObjects) {
		setTalkActionGameObject(null);
		for(GameObject gameObject: allGameObjects)
		{
			if(hasInSight(gameObject.getPosition(), 64))
			{
				//System.out.println("hasInSight: " + gameObject.name);
				if(gameObject.getInteractionPossibilities().contains(InteractionPossibility.TALK))
				{
					setTalkActionGameObject(gameObject);
					//System.out.println("Player has in sight!");
				}
			}
		}
	}
	public boolean collided(LevelState levelState) {
		if(levelState.collides(new ObjectPosition(position.x+6, position.y+8), 16))return true;
		if(levelState.collides(new ObjectPosition(position.x-8, position.y+8), 16))return true;
		if(levelState.collides(new ObjectPosition(position.x-8, position.y-5), 16))return true;
		if(levelState.collides(new ObjectPosition(position.x+6, position.y-5), 16))return true;
		return false;
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
	@Override
	protected void init() {
	}
}
