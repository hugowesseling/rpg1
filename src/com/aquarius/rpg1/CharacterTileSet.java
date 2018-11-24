package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.Serializable;

public class CharacterTileSet  {
//	private static final long serialVersionUID = -8543957292787335445L;
	private Int2d tilesetPosition;
	private TileSet tileSet;
	private final static int[] FRAME_ORDER = {0,1,2,1};
	
	public CharacterTileSet(TileSet tileSet, Int2d tilesetPosition) 
	{
		this.tilesetPosition = tilesetPosition;
		this.tileSet = tileSet;
	}

	public void draw(Graphics2D graphics, int x, int y, Direction direction, int frame)
	{
		int drawFrame = FRAME_ORDER[frame%4];
		Image image = tileSet.getTileImageFromXY(drawFrame + tilesetPosition.x, direction.tileOffset + tilesetPosition.y);
		graphics.drawImage(image, x - image.getWidth(null)/2, y - image.getHeight(null)*3/4, null);		
	}

	public void drawTopLeft(Graphics2D graphics, int x, int y, Direction direction, int frame)
	{
		int drawFrame = FRAME_ORDER[frame%4];
		Image image = tileSet.getTileImageFromXY(drawFrame + tilesetPosition.x, direction.tileOffset + tilesetPosition.y);
		graphics.drawImage(image, x , y , null);		
	}

	public Image getImage() {
		int frame = 0;
		int drawFrame = FRAME_ORDER[frame%4];
		Direction direction = Direction.SOUTH;
		return tileSet.getTileImageFromXY(drawFrame + tilesetPosition.x, direction.tileOffset + tilesetPosition.y);
	}
	
}
