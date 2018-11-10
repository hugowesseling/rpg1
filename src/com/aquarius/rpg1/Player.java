package com.aquarius.rpg1;

import java.util.Vector;

public class Player extends GameObject {
	private static final long serialVersionUID = 1752542798493227606L;
	Vector<CarryableItem> itemsCarried;
	private GameObject talkActionCharacter = null;
	public Player(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("player", objectDrawer, position, direction);
		weapon = new Sword(this);
	}
	public GameObject getTalkActionCharacter() {
		return talkActionCharacter;
	}
	public void setTalkActionCharacter(GameObject talkActionCharacter) {
		this.talkActionCharacter = talkActionCharacter;
	}
	
	public void determineTalkActionCharacter(Vector<GameObject> allCharacters) {
		setTalkActionCharacter(null);
		for(GameObject character: allCharacters)
		{
			if(hasInSight(character.getPosition(), 64))
			{
				if(character.getInteractionPossibilities().contains(InteractionPossibility.TALK))
				{
					setTalkActionCharacter(character);
					//System.out.println("Player has in sight!");
				}
			}
		}
	}
	public boolean collided(LevelState levelState) {
		return levelState.collides(this.position, 16);
	}
}
