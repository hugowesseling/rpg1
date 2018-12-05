package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldState;

public class WalkToPositionAction implements ObjectAction, Serializable {

	private static final long serialVersionUID = 5956310423103168473L;
	private ObjectPosition toPosition;
	private GameObject character;

	public WalkToPositionAction(GameObject character, ObjectPosition position) {
		toPosition = position;
		this.character = character;
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldState worldState, LevelState levelState) {
		Direction direction = Direction.getDirectionFromTo(character.getPosition(), toPosition);
		character.setDirection(direction);
		character.setMovement(direction.movement);		
		boolean isDone = character.getPosition().isNearby(toPosition, 1);
		if(isDone) {
			character.setMovement(new Int2d(0,0));
		}
		return isDone;
	}

}
