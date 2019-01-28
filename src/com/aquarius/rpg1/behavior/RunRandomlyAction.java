package com.aquarius.rpg1.behavior;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldTime;
import com.aquarius.rpg1.objects.GameObject;

public class RunRandomlyAction implements ObjectAction {
	private static final long serialVersionUID = -7105141081754497848L;
	private GameObject gameObject;
	private int runTimeMs;
	private long startTime;
	private int counter;
	private int speed;

	public RunRandomlyAction(GameObject gameObject, WorldTime worldState, int runTimeMs, int speed) {
		this.gameObject = gameObject;
		this.runTimeMs = runTimeMs;
		this.speed = speed;
		startTime = worldState.getTimeMs();
		counter = 0;
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldTime worldState, LevelState levelState) {
		counter--;
		if(counter < 0) {
			gameObject.setDirection(Direction.random());
			counter = (int)(Math.random() * 10) + 20;
		}
		if(!gameObject.moveAndLevelCollide(levelState, gameObject.getDirection().movement.x*speed, (int)(gameObject.getDirection().movement.y*speed)))
			return true;
		if(runTimeMs > 0)
			return worldState.getTimeMs() > startTime + runTimeMs;
		return false;
	}

}
