package com.aquarius.rpg1.drawers;

import java.awt.Graphics2D;
import java.awt.Image;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.ImageSet;
import com.aquarius.rpg1.ObjectDrawer;

public class AnimationDrawer extends ObjectDrawer {
	private static final long serialVersionUID = 6722376226466974917L;
	private int imageCount;
	private ImageSet imageSet;
	
	
	public AnimationDrawer(ImageSet imageSet) {
		super();
		this.imageCount = imageSet.images.length;
		this.imageSet = imageSet;
	}


	@Override
	public void draw(Graphics2D graphics, int x, int y, Direction direction, int frame) {
		Image image = imageSet.images[frame % imageCount];
		graphics.drawImage(image, x - image.getWidth(null)/2, y - image.getHeight(null)/2, null);
	}
}
