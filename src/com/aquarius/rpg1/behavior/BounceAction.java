package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldTime;
import com.aquarius.rpg1.objects.GameObject;

public class BounceAction implements ObjectAction {
	private static final long serialVersionUID = 8012860963215535300L;
	private GameObject gameObject;
	private Int2d movement;
	private float z,dz;
	private boolean firstBounce;

	public BounceAction(GameObject gameObject, Int2d movement) {
		this.gameObject = gameObject;
		this.movement = movement;
		z =0;
		dz = 4;
		firstBounce = true;
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldTime worldState, LevelState levelState) {
		if(firstBounce) {
			gameObject.move(movement.x, (int)(movement.y - dz));
		}else {
			if(!gameObject.moveAndLevelCollide(levelState, movement.x, (int)(movement.y - dz)))
				return true;
		}  
		z += dz;
		dz -= 0.6f;
		if(z < 0) {
			firstBounce = false;
			dz = -dz * 0.65f;
			if(dz < 0.10f)
				return true;
		}
		return false;
	}

}