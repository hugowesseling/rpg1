package com.aquarius.rpg1;

public enum Direction {

    NORTH, SOUTH, EAST, WEST;

    public Int2d movement;
    public int degrees;
    public int tileOffset;

    static {
        NORTH.movement = new Int2d( 0, -1);
        SOUTH.movement = new Int2d( 0,  1);
        EAST.movement =  new Int2d( 1,  0);
        WEST.movement =  new Int2d(-1,  0);

        NORTH.degrees = 0;
        EAST.degrees = 90;
        SOUTH.degrees = 180;
        WEST.degrees = 270;
        
        SOUTH.tileOffset =  0;
        WEST.tileOffset =  1; 
        EAST.tileOffset =  2;
        NORTH.tileOffset = 3;
    }

	public static Direction getDirectionFromTo(Position from, Position to)
	{
		int dx = to.getX() - from.getX(), dy = to.getY() - from.getY();
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
	public static Direction random() {
		int random = (int )(Math.random() * Direction.values().length);
		return Direction.values()[random];
	}

	public static Int2d getMovementFromTo(Position from, Position to) {
		int distY = to.getY() - from.getY(); 
		int distX = to.getX() - from.getX();
		return new Int2d(Integer.signum(distX),Integer.signum(distY));
	}
}