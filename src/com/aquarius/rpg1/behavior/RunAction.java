package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.WorldState;

public class RunAction implements ObjectAction, Serializable {

	private static final long serialVersionUID = 6863101672701913439L;
	private GameObject gameObject;
	private float dz, z;

	public RunAction(GameObject gameObject) {
		this.gameObject = gameObject;
		gameObject.setDirection(Direction.random());
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldState worldState, LevelState levelState) {
		boolean moved = gameObject.moveAndLevelCollide(levelState, gameObject.getDirection().movement.x * 4, gameObject.getDirection().movement.y *4);
		gameObject.setFrameDivider(2);
		if(!moved) {
			System.out.println("RunAction: " + gameObject + " collided with something");
		}
		return !moved;
	}

}
