package com.aquarius.rpg1;
import java.io.Serializable;
import java.util.HashMap;

public class StorableObjectType implements Serializable{

	private static final long serialVersionUID = -697804944188310096L;

	public String name;
	public int itemTileIndex;
	public int amount;

	public StorableObjectType(String name, int itemTileIndex) {
		this(name, itemTileIndex, 1);
	}
	public StorableObjectType(String name, int itemTileIndex, int amount) {
		this.name = name;
		this.itemTileIndex = itemTileIndex;
		this.amount = amount;
	}

	public String toString(){
		return name;
	}
}
