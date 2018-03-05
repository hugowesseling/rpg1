package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameCharacter;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.WorldState;

public class WalkToTilePositionAction implements CharacterAction, Serializable{

	private static final long serialVersionUID = -2497869400457681585L;
	private Int2d toPosition;
	GameCharacter character;

	public WalkToTilePositionAction(GameCharacter character, Int2d position) {
		toPosition = position;
		this.character = character;
	}

	@Override
	public boolean doAction(WorldState worldState) {
		Int2d characterTilePosition = character.getPosition().tileAsInt2d();
		Direction direction = Direction.getDirectionFromTo(characterTilePosition, toPosition);
		character.setDirection(direction);
		character.setMovement(direction.movement);		
		boolean isDone = characterTilePosition.nearby(toPosition, 1);
		if(isDone) {
			character.setMovement(new Int2d(0,0));
		}
		return isDone;
	}

}
