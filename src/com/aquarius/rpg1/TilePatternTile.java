package com.aquarius.rpg1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.Serializable;

public class TilePatternTile implements Serializable
{
	private static final long serialVersionUID = 848967961623009901L;
	public final static int EMPTY = 0;
	public final static int OCCUPIED = 1;
	public final static int EITHER = 2;
	//int tileX, tileY;
	int tileIndex;
	int[][] tileConnections;	// [0-2][0-2] array of connections to other tiles
	
	public TilePatternTile(int tileIndex)
	{
		this.tileIndex = tileIndex;
		tileConnections = new int[3][3];
		for(int yi=0; yi<3; yi++)
		{
			for(int xi=0; xi<3; xi++)
			{
				tileConnections[xi][yi] = EITHER;
			}
		}
		tileConnections[1][1] = OCCUPIED;
	}

	public void draw(Graphics imageG) {
		Int2d xy = TileSet.getTileXYFromIndex(tileIndex);
		imageG.drawRect(xy.x * Constant.TILE_WIDTH, xy.y * Constant.TILE_HEIGHT, 
						Constant.TILE_WIDTH - 1, Constant.TILE_HEIGHT - 1);
	}

	public void drawColorHash(Graphics imageG)
	{
		Int2d xy = TileSet.getTileXYFromIndex(tileIndex);
		int x[] = new int[4];
		int y[] = new int[4];
		imageG.setColor(Color.GRAY);
		x[0] = xy.x * Constant.TILE_WIDTH;
		x[1] = x[0] + Constant.TILE_WIDTH / 3;
		x[2] = x[0] + Constant.TILE_WIDTH * 2 / 3;
		x[3] = x[0] + Constant.TILE_WIDTH - 1;
		y[0] = xy.y * Constant.TILE_HEIGHT;
		y[1] = y[0] + Constant.TILE_HEIGHT / 3;
		y[2] = y[0] + Constant.TILE_HEIGHT * 2 / 3;
		y[3] = y[0] + Constant.TILE_HEIGHT - 1;
		imageG.drawLine(x[1], y[0], x[1], y[3]);
		imageG.drawLine(x[2], y[0], x[2], y[3]);
		imageG.drawLine(x[0], y[1], x[3], y[1]);
		imageG.drawLine(x[0], y[2], x[3], y[2]);
		for(int yi=0; yi<3; yi++)
		{
			for(int xi=0; xi<3; xi++)
			{
				switch(tileConnections[xi][yi])
				{
					case EMPTY:
						imageG.setColor(Color.RED);
						break;
					case OCCUPIED:
						imageG.setColor(Color.GREEN);
						break;
					case EITHER:
						imageG.setColor(Color.YELLOW);
						break;
				}
				
				imageG.fillRect(x[xi] + 1, y[yi] + 1, x[xi+1] - x[xi] - 1, y[yi+1] - y[yi] - 1);
			}
		}
	}

	public void changeColor(int tileThirdX, int tileThirdY, int newColor)
	{
		if(tileThirdX >= 0 && tileThirdX <= 2 && tileThirdY >= 0 && tileThirdY <= 2) {
			tileConnections[tileThirdX][tileThirdY] = newColor;
		}
	}

	public int getMatchRate(boolean[][] neighborhoodTilePattern)
	{
		int matchRate = 0;
		if(tileConnections[1][1] == EMPTY)
			return Integer.MIN_VALUE;
		for(int yi=0; yi<3; yi++)
		{
			for(int xi=0; xi<3; xi++)
			{
				switch(tileConnections[xi][yi])
				{
					case EMPTY:
						if(neighborhoodTilePattern[xi][yi])
						{
							matchRate-=4;
						}else
						{
							matchRate++;
						}
						break;
					case OCCUPIED:
						if(!neighborhoodTilePattern[xi][yi])
						{
							matchRate-=4;
						}else
						{
							matchRate++;
						}
						break;
					case EITHER:
						break;
				}
			}
		}
		return matchRate;
	}

	public TilePatternTile cloneTranslated(int xtrans, int ytrans)
	{
		int tileSetIndex = tileIndex / 65536;
		Int2d xy = TileSet.getTileXYFromIndex(tileIndex);
		int newTileIndex= tileSetIndex * 65536 + (xy.y + ytrans) * 256 + (xy.x + xtrans);		
		TilePatternTile result = new TilePatternTile(newTileIndex);
		for(int yi=0; yi<3; yi++)
		{
			for(int xi=0; xi<3; xi++)
			{
				result.tileConnections[xi][yi] = tileConnections[xi][yi];
			}
		}
		return result;
	}
	
}
