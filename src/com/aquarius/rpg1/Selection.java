package com.aquarius.rpg1;

import java.awt.Color;
import java.awt.Graphics;

public class Selection {
	Int2d topleft, bottomright;
	Selection(int tileX1, int tileY1, int tileX2, int tileY2)
	{
		//System.out.println("New selection: " + topLeftX + "," +  topLeftY + " - " + bottomRightX + "," + bottomRightY);
		topleft = new Int2d(Math.min(tileX1, tileX2), Math.min(tileY1, tileY2));
		bottomright = new Int2d(Math.max(tileX1, tileX2), Math.max(tileY1, tileY2));

		System.out.println("Selection: " + this);
	}
	
	public void draw(Graphics graphics, int screenx, int screeny)
	{
		graphics.setColor(Color.WHITE);
		graphics.drawRect(topleft.x * Constant.TILE_WIDTH - screenx,
				  topleft.y * Constant.TILE_HEIGHT - screeny, 
				  (bottomright.x+1 - topleft.x) * Constant.TILE_WIDTH,
				  (bottomright.y+1 - topleft.y) * Constant.TILE_HEIGHT);
	}
	
	public String toString()
	{
		return topleft + " - " + bottomright;
	}
	Layer copyFromLayer(Layer layer) {
		int [][] theClone = new int[bottomright.x - topleft.x + 1][bottomright.y - topleft.y +1];
		for(int x = topleft.x; x <= bottomright.x; x++){
			for(int y=topleft.y; y <= bottomright.y; y++){
				theClone[x - topleft.x][y - topleft.y] = layer.getTile(x,y); 	
			}
		}
		return new Layer(theClone);
	}
}
