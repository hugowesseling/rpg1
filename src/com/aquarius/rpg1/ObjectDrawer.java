package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.io.Serializable;

public abstract class ObjectDrawer implements Serializable {
	private static final long serialVersionUID = -401936347740434607L;

	public abstract void draw(Graphics2D graphics, int x, int y, Direction direction, int frame);

}
