package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.CharacterPosition;
import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameCharacter;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.WorldState;

public class WalkToPositionAction implements CharacterAction, Serializable {

	private static final long serialVersionUID = 5956310423103168473L;
	private CharacterPosition toPosition;
	private GameCharacter character;

	public WalkToPositionAction(GameCharacter character, CharacterPosition position) {
		toPosition = position;
		this.character = character;
	}

	@Override
	public boolean doAction(WorldState worldState) {
		Direction direction = Direction.getDirectionFromTo(character.getPosition(), toPosition);
		character.setDirection(direction);
		character.setMovement(direction.movement);		
		boolean isDone = character.getPosition().subnearby(toPosition, 1);
		if(isDone) {
			character.setMovement(new Int2d(0,0));
		}
		return isDone;
	}

}
