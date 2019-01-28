package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldTime;
import com.aquarius.rpg1.objects.GameObject;

public class FlyAction implements ObjectAction {

	private static final long serialVersionUID = 6802074239685613595L;
	private GameObject gameObject;
	private Int2d movement;

	public FlyAction(GameObject gameObject, Int2d movement) {
		this.gameObject = gameObject;
		this.movement = movement;
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldTime worldState, LevelState levelState) {
		// TODO Auto-generated method stub
		return !gameObject.moveAndLevelCollide(levelState, movement.x, movement.y);
	}

}
