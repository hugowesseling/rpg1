package com.aquarius.rpg1;
import java.io.Serializable;
import java.util.HashMap;

public class StorableObjectType implements Serializable{

	private static final long serialVersionUID = -697804944188310096L;

	static transient StorableObjectType all[] = {
			new FoodObjectType("strawberries", 131076, 100),
			new FoodObjectType("soup", 133385, 60),
			new ValueObjectType("crystal", 5930, 100),
			new RingObjectType("broccoli ring", 17678, ObjectColor.GREEN),
			new UsableObjectType("Hammer", 2+101*256)
			};
	public static transient HashMap<String, StorableObjectType> allHashMap;
	
	static
	{
		allHashMap = new HashMap<>();
		for(StorableObjectType sot: all){
			allHashMap.put(sot.name, sot);
		}
	}
	
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
