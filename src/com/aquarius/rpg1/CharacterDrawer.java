package com.aquarius.rpg1;

import java.awt.Graphics2D;

public class CharacterDrawer extends ObjectDrawer {

	private static final long serialVersionUID = -8394093202517298150L;
	private CharacterTileSet characterTileSet;
	
	public CharacterDrawer(CharacterTileSet characterTileSet) {
		super();
		this.characterTileSet = characterTileSet;
	}

	@Override
	public void draw(Graphics2D graphics, int x, int y, Direction direction, int frame) {
		characterTileSet.draw(graphics, x, y, direction, frame);		
	}

}
