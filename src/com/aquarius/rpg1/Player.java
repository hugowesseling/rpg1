package com.aquarius.rpg1;

import java.util.Vector;

import com.aquarius.rpg1.behavior.hateno.HenryCharacter;

public class Player extends GameCharacter {
	Vector<CarryableItem> itemsCarried;
	private GameCharacter talkActionCharacter = null;
	public Player(CharacterPosition position, CharacterTileSet characterTileSet, Direction direction) {
		super(position, characterTileSet, direction);
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
	
}
