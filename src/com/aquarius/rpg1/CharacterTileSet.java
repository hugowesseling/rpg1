package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;

public class CharacterTileSet implements Serializable {
	private static final long serialVersionUID = -8543957292787335445L;
	private Int2d tilesetPosition;
	private final static int[] FRAME_ORDER = {0,1,2,1};
	
	public CharacterTileSet(Int2d tilesetPosition) 
	{
		this.tilesetPosition = tilesetPosition;
	}

	public void draw(Graphics2D graphics, int x, int y, Direction direction, int frame)
	{
		int drawFrame = FRAME_ORDER[frame%4];
		Image image = Resources.characterTileSets.getTileImageFromXY(drawFrame + tilesetPosition.x, direction.tileOffset + tilesetPosition.y);
		graphics.drawImage(image, x - image.getWidth(null)/2, y - image.getHeight(null)*3/4, null);		
	}
	
	public void changeTileSetPosition(boolean nextprevious)
	{
		if(nextprevious) {
			tilesetPosition.x += 3;
		}else {
			tilesetPosition.x -= 3;
		}
		if(tilesetPosition.x > 9)
		{
			tilesetPosition.x = 0;
			tilesetPosition.y = (tilesetPosition.y + 4) % 8;
		}
		if(tilesetPosition.x < 0)
		{
			tilesetPosition.x = 9;
			tilesetPosition.y = (tilesetPosition.y - 4 + 8) % 8;
		}
	}
}
