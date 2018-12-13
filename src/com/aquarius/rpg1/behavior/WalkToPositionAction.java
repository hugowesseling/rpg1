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
	private int runTimeMs;
	private long startTime;

	public WalkToPositionAction(GameObject character, WorldState worldState, ObjectPosition position, int runTimeMs) {
		toPosition = position;
		this.character = character;
		this.runTimeMs = runTimeMs;
		startTime = worldState.getTimeMs();
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldState worldState, LevelState levelState) {
		Direction direction = Direction.getDirectionFromTo(character.getPosition(), toPosition);
		character.setDirection(direction);
		character.moveAndLevelCollide(levelState, direction.movement.x, direction.movement.y);
		boolean isDone = character.getPosition().isNearby(toPosition, 1);
		if(runTimeMs > 0)
			return isDone || worldState.getTimeMs() > startTime + runTimeMs;
		return isDone;
	}

}
