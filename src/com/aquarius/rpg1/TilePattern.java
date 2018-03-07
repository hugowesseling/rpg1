package com.aquarius.rpg1;

import java.awt.Graphics;
import java.io.Serializable;

/*
 * Create new tile pattern:
 * 1. Switch to edit tile pattern mode ('#'-like menubar icon)
 * 	See all existing tile patterns
 * 2. Click on existing tile pattern or click on un assigned tile to create new
 * 3. Add and remove tiles to tile pattern (normal left click)
 * 4. Switch to tile pattern editing mode (red-green-grey circle menubar icon)
 * 5. color tiles connections
 * 6. exit tile pattern mode ('#'-like menubar icon) 
 */


import java.util.Vector;

public class TilePattern implements Serializable
{
	private static final long serialVersionUID = -5801611831067687495L;
	
	private Vector<TilePatternTile> tilePatternTiles;

	public TilePattern() {
		tilePatternTiles = new Vector<TilePatternTile>();
	}
	

	public TilePattern(Vector<TilePatternTile> tilePatternTiles)
	{
		this.tilePatternTiles = tilePatternTiles;
	}

	public void addTile(TilePatternTile tilePatternTile) 
	{
		tilePatternTiles.add(tilePatternTile);
	}
	
	public void draw(Graphics imageG) {
		for(TilePatternTile tilePatternTile:tilePatternTiles)
		{
			tilePatternTile.draw(imageG);
		}
	}

	public void drawColorHash(Graphics imageG)
	{
		for(TilePatternTile tilePatternTile:tilePatternTiles)
		{
			tilePatternTile.drawColorHash(imageG);
		}
		
	}
	

	public TilePatternTile findTileInTilePattern(int tileX, int tileY)
	{
		for(TilePatternTile tilePatternTile:tilePatternTiles)
		{
			if(tilePatternTile.tileX == tileX && tilePatternTile.tileY == tileY)
			{
				return tilePatternTile;
			}
		}
		return null;
	}
	

	public boolean isTileInTilePattern(int tileX, int tileY)
	{
		for(TilePatternTile tilePatternTile:tilePatternTiles)
		{
			if(tilePatternTile.tileX == tileX && tilePatternTile.tileY == tileY)
			{
				return true;
			}
		}
		return false;
	}


	public void removeTileFromPattern(int tileX, int tileY)
	{
		Vector<TilePatternTile> newTilePatternTiles = new Vector<TilePatternTile>();
		for(TilePatternTile tilePatternTile:tilePatternTiles)
		{
			if(tilePatternTile.tileX != tileX || tilePatternTile.tileY != tileY)
			{
				newTilePatternTiles.add(tilePatternTile);
			}
		}
		tilePatternTiles = newTilePatternTiles;
	}


	public void changeColor(int tileX, int tileY, int tileThirdX, int tileThirdY)
	{
		TilePatternTile tilePatternTile = findTileInTilePattern(tileX,tileY);
		if(tilePatternTile == null)
		{
			return;
		}
		tilePatternTile.changeColor(tileThirdX, tileThirdY);
	}


	public void place(Layer layer, TileSet tileSet, int tileX, int tileY, boolean placeNeighborhood)
	{
		// Determine correct tile to place and set it on layer
		if(tilePatternTiles.size() == 0)
		{
			return;
		}
		
		boolean neighborhoodTilePattern[][] = getNeighborhoodTilePattern(layer, tileX,tileY);
		int matchRate, maxMatchRate = -9*4 - 1;
		TilePatternTile maxRateTilePatternTile = tilePatternTiles.get(0);
		for(TilePatternTile tilePatternTile:tilePatternTiles)
		{
			matchRate = tilePatternTile.getMatchRate(neighborhoodTilePattern);
			//System.out.println("tilePatternTile:" + tilePatternTile.tileX + "," + tilePatternTile.tileY + ", match: " + matchRate);
			if(matchRate > maxMatchRate)
			{
				maxRateTilePatternTile = tilePatternTile;
				maxMatchRate = matchRate;
			}
		}
		layer.setTile(tileX, tileY, tileSet.getTileIndexFromXY(maxRateTilePatternTile.tileX, maxRateTilePatternTile.tileY));
		if(placeNeighborhood)
		{
			// Check all neighborhood tiles that are also in this tilePattern and update them
			for(int y=-1;y<2;y++)
			{
				for(int x=-1;x<2;x++)
				{
					int nTileX = tileX + x;
					int nTileY = tileY + y;
					// not outside of layer
					if(nTileX >= 0 && nTileX < layer.getWidth() && nTileY >= 0 && nTileY < layer.getHeight())
					{
						// Check if in this tilePattern
						int tileIndex = layer.getTile(nTileX, nTileY);
						Int2d tileXY = tileSet.getTileXYFromIndex(tileIndex);
						if(isTileInTilePattern(tileXY.x,tileXY.y))
						{
							place(layer, tileSet, nTileX, nTileY, false);
						}
					}
				}
			}
		}
	}


	private boolean[][] getNeighborhoodTilePattern(Layer layer, int tileX, int tileY)
	{
		boolean result[][] = new boolean[3][3];
		for(int y=-1;y<2;y++)
		{
			for(int x=-1;x<2;x++)
			{
				int tileIndex = layer.getTile(tileX + x, tileY + y);
				if(tileIndex == -1)
				{
					result[x+1][y+1] = false;
				}else
				{
					Int2d tileXY = TileSet.getTileXYFromIndex(tileIndex);
					result[x+1][y+1] = isTileInTilePattern(tileXY.x,tileXY.y);
				}
			}
		}
		//print2darray(result);
		return result;
	}


	private void print2darray(boolean[][] a)
	{
		for(int yi=0; yi<a[0].length; yi++)
		{
			String out="";
			for(int xi=0; xi<a.length; xi++)
			{
				out+= "," + a[xi][yi];
			}
			System.out.println(out);
		}
	}


	public TilePattern cloneTranslated(int x, int y)
	{
		// Clone the current tile pattern so that the left-top most corner (could be a missing tile) is at x,y
		TilePattern result = new TilePattern();
		Int2d left_top = determineMinXMinY();
		int xtrans = x - left_top.x;
		int ytrans = y - left_top.y;
		System.out.println("Cloning from " + left_top.x + "," + left_top.y + " to " + x + "," + y);
		for(TilePatternTile tilePatternTile:tilePatternTiles)
		{
			result.tilePatternTiles.add(tilePatternTile.cloneTranslated(xtrans, ytrans));
		}
		return result;
	}


	private Int2d determineMinXMinY()
	{
		int minx = Integer.MAX_VALUE;
		int miny = Integer.MAX_VALUE;
		for(TilePatternTile tilePatternTile:tilePatternTiles)
		{
			if(tilePatternTile.tileX < minx)
			{
				minx = tilePatternTile.tileX;
			}
			if(tilePatternTile.tileY < miny)
			{
				miny = tilePatternTile.tileY;
			}
		}
		return new Int2d(minx, miny);
	}

}
