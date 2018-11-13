package com.aquarius.rpg1;

import java.util.Vector;

public class Player extends GameObject {
	private static final long serialVersionUID = 1752542798493227606L;
	Vector<CarryableItem> itemsCarried;
	private GameObject talkActionGameObject = null;
	public Player(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("player", objectDrawer, position, direction);
		weapon = new Sword(this);
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
				if(gameObject.getInteractionPossibilities().contains(InteractionPossibility.TALK))
				{
					setTalkActionGameObject(gameObject);
					//System.out.println("Player has in sight!");
				}
			}
		}
	}
	public boolean collided(LevelState levelState) {
		return levelState.collides(this.position, 16);
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
}
