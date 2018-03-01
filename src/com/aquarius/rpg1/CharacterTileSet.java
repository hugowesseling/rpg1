package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class CharacterTileSet {
	private Int2d tilesetPosition;
	private TileSet tileSet;
	private final static int[] FRAME_ORDER = {0,1,2,1};
	
	public CharacterTileSet(Int2d tilesetPosition, TileSet tileSet) 
	{
		this.tilesetPosition = tilesetPosition;
		this.tileSet = tileSet;
	}

	public void draw(Graphics2D graphics, int x, int y, Direction direction, int frame)
	{
		int drawFrame = FRAME_ORDER[frame%4];
		graphics.drawImage(tileSet.getTileImageFromXY(drawFrame + tilesetPosition.x, direction.tileOffset + tilesetPosition.y), x, y, null);		
	}
}
