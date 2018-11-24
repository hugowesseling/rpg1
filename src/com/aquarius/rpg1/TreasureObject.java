package com.aquarius.rpg1;

import java.util.Arrays;
import java.util.HashSet;

public class TreasureObject extends GameObject{
	private static final long serialVersionUID = 4226718004114866622L;

	public TreasureObject(TileDrawer tileDrawer, ObjectPosition position){
		super("treasureObject", tileDrawer, position, Direction.NORTH);
		init();
	}
	
	protected void init() {
		interactionPossibilities = new HashSet<>(Arrays.asList(InteractionPossibility.OPEN));
	}

}
