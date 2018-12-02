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

	public WalkToTilePositionAction(GameObject gameObject, Int2d position) {
		toPosition = position;
		this.gameObject = gameObject;
	}

	@Override
	public boolean doAction(WorldState worldState, LevelState levelState) {
		Int2d characterTilePosition = gameObject.getPosition().tileAsInt2d();
		Direction direction = Direction.getDirectionFromTo(characterTilePosition, toPosition);
		gameObject.setDirection(direction);
		gameObject.setMovement(direction.movement);		
		boolean isDone = characterTilePosition.nearby(toPosition, 1);
		if(isDone) {
			gameObject.setMovement(new Int2d(0,0));
		}
		return isDone;
	}

}
