package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldState;

public class WalkToTilePositionAction implements ObjectAction, Serializable{

	private static final long serialVersionUID = -2497869400457681585L;
	private Int2d toPosition;
	GameObject gameObject;
	private int runTimeMs;
	private long startTime;

	public WalkToTilePositionAction(GameObject gameObject, WorldState worldState, Int2d position, int runTimeMs) {
		toPosition = position;
		this.gameObject = gameObject;
		this.runTimeMs = runTimeMs;
		startTime = worldState.getTimeMs();
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldState worldState, LevelState levelState) {
		Int2d characterTilePosition = gameObject.getPosition().tileAsInt2d();
		Direction direction = Direction.getDirectionFromTo(characterTilePosition, toPosition);
		//System.out.println(gameObject.getName() + " direction to position " + toPosition + "=" + direction);
		gameObject.setDirection(direction);
		gameObject.moveAndLevelCollide(levelState, direction.movement.x, direction.movement.y);
		
		boolean isDone = characterTilePosition.is(toPosition);
		if(runTimeMs > 0)
			return isDone || worldState.getTimeMs() > startTime + runTimeMs;
		return isDone;
	}

}
