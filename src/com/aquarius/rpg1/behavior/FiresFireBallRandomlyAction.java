package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.objects.GameObject;

public class FiresFireBallRandomlyAction implements ObjectAction, Serializable {
	private static final long serialVersionUID = -628399314221672794L;
	private int timer;
	private GameObject gameObject;
	public FiresFireBallRandomlyAction(GameObject gameObject) {
		this.gameObject = gameObject;
		timer = 20;
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldState worldState, LevelState levelState) {
		timer--;
		if(timer<0){
			timer = 50;
			gameObject.setDirection(Direction.random());
			levelState.gameObjectsToAdd.add(new FireBall(gameObject.getPosition().clone().addToThis(gameObject.getDirection().movement.multiply(10)), gameObject.getDirection().movement.multiply(3)));
		}
		return false;
	}

}
