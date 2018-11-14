package com.aquarius.rpg1;

public class FoodObjectType extends StorableObjectType {

	private int energy;

	public FoodObjectType(String name, int tileIndex, int energy) {
		super(name, tileIndex);
		this.energy = energy;
	}
}
