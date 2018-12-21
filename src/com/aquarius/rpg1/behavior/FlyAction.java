package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.behavior.hateno.FireBall;

public class FlyAction implements ObjectAction, Serializable {

	private static final long serialVersionUID = 6802074239685613595L;
	private GameObject gameObject;
	private Int2d movement;

	public FlyAction(GameObject gameObject, Int2d movement) {
		this.gameObject = gameObject;
		this.movement = movement;
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldState worldState, LevelState levelState) {
		// TODO Auto-generated method stub
		return !gameObject.moveAndLevelCollide(levelState, movement.x, movement.y);
	}

}
