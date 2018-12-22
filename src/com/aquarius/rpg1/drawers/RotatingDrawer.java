package com.aquarius.rpg1.drawers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.ObjectDrawer;

public class RotatingDrawer extends ObjectDrawer {
	private static final long serialVersionUID = -1142086611280754815L;
	private double radians;
	private Image image;
	
	
	public RotatingDrawer(Image image) {
		super();
		radians = 0;
		this.image = image;
	}


	@Override
	public void draw(Graphics2D graphics, int x, int y, Direction direction, int frame) {
		radians += 0.2;
		AffineTransform oldTransform = graphics.getTransform();
		graphics.translate(x, y);
		graphics.rotate(radians);
		graphics.drawImage(image, - image.getWidth(null)/2, -image.getHeight(null)/2, null);
		graphics.setTransform(oldTransform);
	}
}
