package com.aquarius.rpg1.behavior;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.objects.GameObject;

public class ThrowRocksRandomlyAction implements ObjectAction {
	private static final long serialVersionUID = -6274782855599805370L;
	private int timer;
	private GameObject gameObject;
	public ThrowRocksRandomlyAction(GameObject gameObject) {
		this.gameObject = gameObject;
		timer = 50;
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldState worldState, LevelState levelState) {
		timer--;
		if(timer<0){
			timer = 50;
			gameObject.setDirection(Direction.random());
			levelState.gameObjectsToAdd.add(new BouncingRock(gameObject.getPosition().clone().addToThis(gameObject.getDirection().movement.multiply(10)), gameObject.getDirection().movement.multiply(2)));
		}
		return false;
	}

}
