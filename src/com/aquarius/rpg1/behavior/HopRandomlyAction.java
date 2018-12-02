package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.WorldState;

public class HopRandomlyAction implements ObjectAction, Serializable {

	private static final long serialVersionUID = 6863101672701913439L;
	private GameObject gameObject;
	private boolean doNewHop;
	private float dz, z;

	public HopRandomlyAction(GameObject gameObject) {
		this.gameObject = gameObject;
		doNewHop = true;
	}

	@Override
	public boolean doAction(WorldState worldState, LevelState levelState) {
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
		return false;
	}

}
