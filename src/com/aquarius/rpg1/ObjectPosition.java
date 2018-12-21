package com.aquarius.rpg1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class ObjectPosition implements Position, Serializable {
	private static final long serialVersionUID = -2456233648907996230L;
	public int x, y;
	
	public ObjectPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public ObjectPosition clone(){
		return new ObjectPosition(x,y);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	public String toString() {
		return x + ", " + y;		
	}
	
	public int getXTile() {
		return x / Constant.TILE_WIDTH;
	}
	
	public int getYTile() {
		return y / Constant.TILE_HEIGHT;
	}
	
	public static int getXTileFromX(int x) {
		return x / Constant.TILE_WIDTH;
	}
	
	public static int getYTileFromY(int y) {
		return y / Constant.TILE_HEIGHT;
	}
	

	public Int2d tileAsInt2d() {
		return new Int2d(getXTile(), getYTile());
	}

	public int distanceTo(ObjectPosition pos) {
		return (int) Math.hypot(x - pos.x, y - pos.y);
	}
	
	public boolean isNearby(ObjectPosition pos, int distance) {
		return distanceTo(pos) <= distance;
	}

	public static ObjectPosition createFromTilePosition(Int2d tilePosition) {
		return new ObjectPosition(tilePosition.x * Constant.TILE_WIDTH + 8, tilePosition.y * Constant.TILE_HEIGHT + 6);
	}

	public void focusScreen(Int2d screen){
		
	}

	public ObjectPosition add(Int2d movement) {
		x += movement.x;
		y += movement.y;
		return this;
	}

	public boolean inRect(Int2d topleft, Int2d bottomright) {
		int minx = Math.min(topleft.x, bottomright.x), miny = Math.min(topleft.y, bottomright.y);
		int maxx = Math.max(topleft.x, bottomright.x), maxy = Math.max(topleft.y, bottomright.y);
		return (x >= minx) && (y >= miny) && (x <= maxx) && (y <= maxy);
	}
	private void writeObject(java.io.ObjectOutputStream stream)	throws IOException {
		stream.writeInt(x);
		stream.writeInt(y);
	}
	private void readObject(ObjectInputStream ois)
	    throws ClassNotFoundException, IOException {
	  x = (int) ois.readInt();
	  y = (int) ois.readInt();
	}

	public boolean isOnStraightLine(ObjectPosition position, int margin) {
		return (Math.abs(position.x - x) < margin) ||
			(Math.abs(position.y - y) < margin);
	}

	public Direction getDirectionTo(ObjectPosition position) {
		int dx = position.x - x, dy = position.y - y;
		if(dx > dy){ // NE
			if(dx > -dy)
				return Direction.EAST;
			else
				return Direction.NORTH;
		}else {//SW
			if(dx > -dy)
				return Direction.SOUTH;
			else
				return Direction.WEST;
		}
	}

	public ObjectPosition createForward(Direction direction, int dist) {
		return new ObjectPosition(x+direction.movement.x*dist, y+direction.movement.y*dist);
	}
	
}
