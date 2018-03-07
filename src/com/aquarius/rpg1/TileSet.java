package com.aquarius.rpg1;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.aquarius.common2dgraphics.Art;

public class TileSet
{
	public BufferedImage[][] tiles;
    boolean[][] tileCollision;

	public String fileName;
	private int index;

	public TileSet(int index, String fileName, int tileWidth, int tileHeight, int marginWidth, int marginHeight)
	{
		System.out.println("Loading tileSet " + fileName);
		this.index = index;
		this.fileName = fileName;
		tiles = Art.split(Art.load(fileName), tileWidth, tileHeight, marginWidth, marginHeight);
		tileCollision = new boolean[tiles.length][tiles[0].length];
	}

	public int getTileIndexFromXY(int x, int y)
	{
		int retval = index * 65536 + y * 256 + x;
		System.out.println("getTileIndexFromXY: " + retval);
		return retval;
	}
	
	public static Int2d getTileXYFromIndex(int index)
	{
		return new Int2d(index % 256, (index / 256) % 256);
	}

	public Image getTileImageFromIndex(int i)
	{
		int y = (i / 256) % 256;
		int x = i % 256;
		return getTileImageFromXY(x,y);
	}

	public Image getTileImageFromXY(int x, int y)
	{
		if(x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length)
			return tiles[x][y];
		return tiles[0][0];
	}
}
