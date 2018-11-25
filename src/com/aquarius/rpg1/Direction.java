package com.aquarius.rpg1;

public enum Direction {

    NORTH, SOUTH, EAST, WEST;

    public Int2d movement;
    public int tileOffset;

    static {
        NORTH.movement = new Int2d( 0, -1);
        SOUTH.movement = new Int2d( 0,  1);
        EAST.movement =  new Int2d( 1,  0);
        WEST.movement =  new Int2d(-1,  0);
        
        SOUTH.tileOffset =  0;
        WEST.tileOffset =  1; 
        EAST.tileOffset =  2;
        NORTH.tileOffset = 3;
    }

	public static Direction getDirectionFromTo(Position from, Position to)
	{
		int distY = to.getY() - from.getY(); 
		int distX = to.getX() - from.getX();
		if(Math.abs(distX) > Math.abs(distY))
		{
			if(distX>0){
				return Direction.EAST;
			}else{
				return Direction.WEST;
			}
		}else
		{
			if(distY>0) {
				return Direction.SOUTH;
			}else{
				return Direction.NORTH;
			}			
		}
	}
	public static Direction random() {
		int random = (int )(Math.random() * Direction.values().length);
		return Direction.values()[random];
	}
}