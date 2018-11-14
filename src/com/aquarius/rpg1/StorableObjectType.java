package com.aquarius.rpg1;
import java.io.Serializable;

public abstract class StorableObjectType implements Serializable{

	private static final long serialVersionUID = -697804944188310096L;

	static transient StorableObjectType all[] = {
			new FoodObjectType("strawberries", 131076, 100),
			new FoodObjectType("soup", 133385, 50)
			};

	public String name;
	public int tileIndex;

	public StorableObjectType(String name, int tileIndex) {
		this.name = name;
		this.tileIndex = tileIndex;
	}
}
