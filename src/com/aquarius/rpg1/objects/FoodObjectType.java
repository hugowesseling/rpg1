package com.aquarius.rpg1.objects;

public class FoodObjectType extends StorableObjectType {
	private static final long serialVersionUID = 1192287535816503457L;
	private int energy;

	public FoodObjectType(String name, int tileIndex, int energy) {
		super(name, tileIndex);
		this.energy = energy;
	}
}
