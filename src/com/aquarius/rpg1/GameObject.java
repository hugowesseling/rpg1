package com.aquarius.rpg1;

import com.aquarius.rpg1.behavior.GameObjectType;

public class GameObject{
	Int2d position;
	GameObjectType objectType;
	public double distanceTo(Int2d position2)
	{
		return Math.hypot(position.x - position2.x, position.y - position2.y);
	}
	public Int2d getPosition() {
		return position;
	}

}
