package com.aquarius.rpg1.drawers;

import java.awt.Graphics2D;
import java.awt.Image;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.Resources;

public class TileDrawer extends ObjectDrawer {
	private static final long serialVersionUID = 5856422740357583069L;
	public int tileIndex = 0;
	
	
	public TileDrawer(int tileIndex) {
		super();
		this.tileIndex = tileIndex;
	}


	@Override
	public void draw(Graphics2D graphics, int x, int y, Direction direction, int frame) {
		Image image = Resources.getTileImageFromIndex(tileIndex);
		graphics.drawImage(image, x - image.getWidth(null)/2, y - image.getHeight(null)/2, null);
	}
}
