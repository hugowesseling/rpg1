package com.aquarius.rpg1;

import java.io.IOException;
import java.io.Serializable;

public class CharacterPosition implements Position, Serializable {
	private static final long serialVersionUID = -2456233648907996230L;
	public int x, y;
	
	public CharacterPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	
	public int getXTile() {
		return x / Constant.TILE_WIDTH;
	}
	
	public int getYTile() {
		return y / Constant.TILE_HEIGHT;
	}

	public Int2d tileAsInt2d() {
		return new Int2d(getXTile(), getYTile());
	}

	public int distanceTo(CharacterPosition pos) {
		return (int) Math.hypot(x - pos.x, y - pos.y);
	}
	
	public boolean subnearby(CharacterPosition pos, int distance) {
		return distanceTo(pos) <= distance;
	}

	public static CharacterPosition createFromTilePosition(Int2d tilePosition) {
		return new CharacterPosition(tilePosition.x * Constant.TILE_WIDTH, tilePosition.y * Constant.TILE_HEIGHT);
	}

	public void focusScreen(Int2d screen){
		
	}

	public void add(Int2d movement) {
		x += movement.x;
		y += movement.y;
	}

	public boolean inRect(Int2d topleft, Int2d bottomright) {
		int minx = Math.min(topleft.x, bottomright.x), miny = Math.min(topleft.y, bottomright.y);
		int maxx = Math.max(topleft.x, bottomright.x), maxy = Math.max(topleft.y, bottomright.y);
		return (x >= minx) && (y >= miny) && (x <= maxx) && (y <= maxy);
	}
	private void writeObject(java.io.ObjectOutputStream stream)	throws IOException {
		stream.writeObject(x);
		stream.writeObject(y);
	}
}
