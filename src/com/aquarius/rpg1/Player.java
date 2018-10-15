package com.aquarius.rpg1;

import java.util.Vector;

public class Player extends GameCharacter {
	private static final long serialVersionUID = 1752542798493227606L;
	Vector<CarryableItem> itemsCarried;
	private GameCharacter talkActionCharacter = null;
	public Player(CharacterPosition position, CharacterTileSet characterTileSet, Direction direction) {
		super("player", position, characterTileSet, direction);
		weapon = new Sword(this);
	}
	public GameCharacter getTalkActionCharacter() {
		return talkActionCharacter;
	}
	public void setTalkActionCharacter(GameCharacter talkActionCharacter) {
		this.talkActionCharacter = talkActionCharacter;
	}
	
	public void determineTalkActionCharacter(Vector<GameCharacter> allCharacters) {
		setTalkActionCharacter(null);
		for(GameCharacter character: allCharacters)
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
