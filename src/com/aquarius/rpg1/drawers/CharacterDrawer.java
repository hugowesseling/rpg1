package com.aquarius.rpg1.drawers;

import java.awt.Graphics2D;

import com.aquarius.rpg1.CharacterTileSet;
import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.Resources;

public class CharacterDrawer extends ObjectDrawer {

	private static final long serialVersionUID = -8394093202517298150L;
	private int characterTileSetIndex;
	
	public CharacterDrawer(int characterTileSetIndex) {
		super();
		this.characterTileSetIndex = characterTileSetIndex;
	}

	@Override
	public void draw(Graphics2D graphics, int x, int y, Direction direction, int frame) {
		CharacterTileSet characterTileSet = Resources.characterTileSets.get(characterTileSetIndex);
		characterTileSet.draw(graphics, x, y, direction, frame);		
	}

}
