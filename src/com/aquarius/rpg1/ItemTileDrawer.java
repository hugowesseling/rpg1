package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.awt.Image;

public class ItemTileDrawer extends ObjectDrawer {
	private static final long serialVersionUID = 7326576083880078418L;
	int itemTileIndex = 0;
	
	
	public ItemTileDrawer(int itemTileIndex) {
		super();
		this.itemTileIndex = itemTileIndex;
	}


	@Override
	public void draw(Graphics2D graphics, int x, int y, Direction direction, int frame) {
		Image image = Resources.itemTileSet.getTileImageFromIndexSafe(itemTileIndex);
		graphics.drawImage(image, x - image.getWidth(null)/2, y - image.getHeight(null)/2, null);
	}
}
