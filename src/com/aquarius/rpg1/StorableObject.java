package com.aquarius.rpg1;

import java.util.Arrays;
import java.util.HashSet;

public class StorableObject extends GameObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -103755530791188265L;

	public StorableObject(StorableObjectType storableObjectType, TileDrawer tileDrawer, ObjectPosition position){
		super("StorableObject", tileDrawer, position, Direction.NORTH);
		init();
	}

	@Override
	protected void init() {
		interactionPossibilities = new HashSet<>(Arrays.asList(InteractionPossibility.PICKUP));
	}

}
