package com.aquarius.rpg1;

import java.io.Serializable;

public class Int2d implements Position, Serializable
{
	private static final long serialVersionUID = -5947870930273839690L;
	public int x,y;

	public Int2d(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	public boolean nearby(Int2d pos, float distance) {
		return Math.hypot(x - pos.x, y - pos.y) <= distance;
	}
	
	public String toString() {
		return x + ", " + y;
	}
}
