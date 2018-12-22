package com.aquarius.rpg1.drawers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.Resources;

public class RotatingDrawer extends ObjectDrawer {
	private static final long serialVersionUID = -1142086611280754815L;
	private double radians;
	private int itemTileIndex;
	private transient Image image = null;
	
	
	public RotatingDrawer(int itemTileIndex) {
		super();
		radians = 0;
		this.itemTileIndex = itemTileIndex;
	}


	@Override
	public void draw(Graphics2D graphics, int x, int y, Direction direction, int frame) {
		if(image == null)
			image = Resources.itemTileSet.getTileImageFromIndexSafe(itemTileIndex);
		radians += 0.2;
		AffineTransform oldTransform = graphics.getTransform();
		graphics.translate(x, y);
		graphics.rotate(radians);
		graphics.drawImage(image, - image.getWidth(null)/2, -image.getHeight(null)/2, null);
		graphics.setTransform(oldTransform);
	}
}
