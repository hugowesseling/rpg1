package com.aquarius.rpg1.behavior.hateno;

import java.io.Serializable;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.behavior.ObjectAction;

public class HopRandomlyAction implements ObjectAction, Serializable {

	private static final long serialVersionUID = 6863101672701913439L;
	private GameObject gameObject;
	private ObjectPosition groundPosition;
	private Direction hopDirection;
	private Int2d dPos;
	private float dz, z;

	public HopRandomlyAction(GameObject gameObject) {
		this.gameObject = gameObject;
		this.groundPosition = gameObject.getPosition().clone();
		this.hopDirection = null;
		dPos = new Int2d(0,0);
	}

	@Override
	public boolean doAction(WorldState worldState) {
		if(hopDirection == null) {
			hopDirection = Direction.random();
			gameObject.setDirection(hopDirection);
			dPos = hopDirection.movement;
			dz = 2;
			z =0;
		}
		gameObject.getPosition().x = groundPosition.x; 
		gameObject.getPosition().y = (int)(groundPosition.y - z); 
		groundPosition.x += dPos.x;
		groundPosition.y += dPos.y;
		z += dz;
		dz -= 0.3;
		if(z < 0) {
			z = 0;
			hopDirection = null;
		}
		return false;
	}

}
