package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.awt.Image;

public class TileObjectDrawer extends ObjectDrawer {
	private static final long serialVersionUID = -1806149907921854224L;
	int tileIndex = 0;
	private int itemTileIndex = 0;
	
	
	public TileObjectDrawer(int tileIndex, int itemTileIndex) {
		super();
		this.tileIndex = tileIndex;
		this.itemTileIndex = itemTileIndex;
	}


	@Override
	public void draw(Graphics2D graphics, int x, int y, Direction direction, int frame) {
		Image image = Resources.getTileImageFromIndex(tileIndex);
		graphics.drawImage(image, x - image.getWidth(null)/2, y - image.getHeight(null)/2, null);
		image = Resources.itemTileSet.getTileImageFromIndexSafe(itemTileIndex);
		graphics.drawImage(image, x - image.getWidth(null)/2, y - image.getHeight(null)/2, null);
	}
}
