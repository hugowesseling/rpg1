package com.aquarius.rpg1;

import java.awt.Color;
import java.awt.Graphics;

public class EditorState
{
	class Selection
	{
		Int2d topleft, bottomright;
		Selection(int topLeftX, int topLeftY, int bottomRightX, int bottomRightY)
		{
			//System.out.println("New selection: " + topLeftX + "," +  topLeftY + " - " + bottomRightX + "," + bottomRightY);
			topleft = new Int2d(topLeftX, topLeftY);
			bottomright = new Int2d(bottomRightX, bottomRightY);
		}
		public String toString()
		{
			return topleft + " - " + bottomright;
		}
	}
	Selection mapSelection = null, tileSelection = null;

	public EditorState()
	{
	}

	public void setTileSelection(int tileX1, int tileY1, int tileX2, int tileY2)
	{
		tileSelection = new Selection(
				Math.min(tileX1, tileX2), Math.min(tileY1, tileY2),
				Math.max(tileX1, tileX2), Math.max(tileY1, tileY2));
		System.out.println("Selection: " + tileSelection);
	}


	public void setMapSelection(int tileX1, int tileY1, int tileX2, int tileY2)
	{
		mapSelection = new Selection(
				Math.min(tileX1, tileX2), Math.min(tileY1, tileY2),
				Math.max(tileX1, tileX2), Math.max(tileY1, tileY2));
	}

	public void drawMapSelection(Graphics graphics, int screenx, int screeny) 
	{
		if(mapSelection != null)
		{
			drawSelection(mapSelection, graphics, screenx, screeny);
		}
	}
	public void drawTileSelection(Graphics graphics, int screenx, int screeny) 
	{
		if(tileSelection != null)
		{
			drawSelection(tileSelection, graphics, screenx, screeny);
		}
	}

	private void drawSelection(Selection selection, Graphics graphics, int screenx, int screeny)
	{
		graphics.setColor(Color.WHITE);
		graphics.drawRect(selection.topleft.x * Constant.TILE_WIDTH - screenx,
				  selection.topleft.y * Constant.TILE_HEIGHT - screeny, 
				  (selection.bottomright.x+1-selection.topleft.x) * Constant.TILE_WIDTH,
				  (selection.bottomright.y+1-selection.topleft.y) * Constant.TILE_HEIGHT);
	}
}
