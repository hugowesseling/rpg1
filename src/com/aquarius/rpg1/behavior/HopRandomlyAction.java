package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.WorldTime;
import com.aquarius.rpg1.objects.GameObject;

public class HopRandomlyAction implements ObjectAction {

	private static final long serialVersionUID = 6863101672701913439L;
	private GameObject gameObject;
	private boolean doNewHop;
	private float dz, z;
	private int hopTimeMs;
	private long startTime;

	public HopRandomlyAction(GameObject gameObject, WorldTime worldState, int hopTimeMs) {
		this.gameObject = gameObject;
		this.hopTimeMs = hopTimeMs;
		startTime = worldState.getTimeMs();
		doNewHop = true;
		
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldTime worldState, LevelState levelState) {
		if(doNewHop) {
			doNewHop = false;
			gameObject.setDirection(Direction.random());
			dz = 2;
			z =0;
		}
		gameObject.moveAndLevelCollide(levelState, gameObject.getDirection().movement.x, (int)(gameObject.getDirection().movement.y - dz));  
		z += dz;
		dz -= 0.3;
		if(z < 0) {
			doNewHop = true;
		}
		if(hopTimeMs > 0)
			return worldState.getTimeMs() > startTime + hopTimeMs;
		return false;
	}

}
