package com.aquarius.rpg1.behavior;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameCharacter;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.WorldState;

public class WalkToCharacterAction implements CharacterAction{

	private GameCharacter character;
	private GameCharacter toCharacter;
	private int mindistance;
	private int maxdistance;

	public WalkToCharacterAction(GameCharacter character, GameCharacter toCharacter, int mindistance, int maxdistance) {
		this.character = character;
		this.toCharacter = toCharacter;
		this.mindistance = mindistance;
		this.maxdistance = maxdistance;
	}

	@Override
	public boolean doAction(WorldState worldState) {
		Direction direction = Direction.getDirectionFromTo(character.getPosition(), toCharacter.getPosition());
		character.setDirection(direction);
		character.setMovement(direction.movement);
		
		int distance = toCharacter.getPosition().distanceTo(character.getPosition());
		boolean isDone = (distance < mindistance) || (distance > maxdistance);
		if(isDone) {
			character.setMovement(new Int2d(0,0));
		}
		return isDone;
	}


}
