package com.aquarius.rpg1;

import java.util.Vector;

import com.aquarius.rpg1.behavior.hateno.HenryCharacter;

public class Player extends GameCharacter {
	Vector<CarryableItem> itemsCarried;
	private GameCharacter talkActionCharacter = null;
	public Player(CharacterPosition position, CharacterTileSet characterTileSet, Direction direction) {
		super(position, characterTileSet, direction, null);
	}
	public GameCharacter getTalkActionCharacter() {
		return talkActionCharacter;
	}
	public void setTalkActionCharacter(GameCharacter talkActionCharacter) {
		this.talkActionCharacter = talkActionCharacter;
	}
	
}
