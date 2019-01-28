package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldTime;
import com.aquarius.rpg1.behavior.ObjectAction;
import com.aquarius.rpg1.objects.GameObject;

public class RunUntilWallAction implements ObjectAction {
	private static final long serialVersionUID = -8814784237530471167L;
	private GameObject gameObject;
	private int speed;

	public RunUntilWallAction(GameObject gameObject, int speed) {
		this.gameObject = gameObject;
		this.speed = speed;
	}

	public boolean doActionAndCheckIfDone(WorldTime worldState, LevelState levelState) {
		return !gameObject.moveAndLevelCollide(levelState, gameObject.getDirection().movement.x*speed, (int)(gameObject.getDirection().movement.y*speed));
	}

}
