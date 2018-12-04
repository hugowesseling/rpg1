package com.aquarius.rpg1.behavior;

import java.io.Serializable;
import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldState;

public class RunRandomlyAction implements ObjectAction, Serializable {
	private static final long serialVersionUID = -7105141081754497848L;
	private GameObject gameObject;
	private int runTimeMs;
	private long startTime;
	private int counter;

	public RunRandomlyAction(GameObject gameObject, WorldState worldState, int runTimeMs) {
		this.gameObject = gameObject;
		this.runTimeMs = runTimeMs;
		startTime = worldState.getTimeMs();
		counter = 0;
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldState worldState, LevelState levelState) {
		counter--;
		if(counter < 0) {
			gameObject.setDirection(Direction.random());
			counter = (int)(Math.random() * 10) + 20;
		}
		if(!gameObject.moveAndLevelCollide(levelState, gameObject.getDirection().movement.x*3, (int)(gameObject.getDirection().movement.y*3)))
			counter = 0;
		if(runTimeMs > 0)
			return worldState.getTimeMs() > startTime + runTimeMs;
		return false;
	}

}
