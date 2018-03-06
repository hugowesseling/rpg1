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
	public BufferedImage[][] tiles ;
	private String fileName;

	public TileSet(String fileName, int tileWidth, int tileHeight, int marginWidth, int marginHeight)
	{
		System.out.println("Loading tileSet " + fileName);
		this.fileName = fileName; 
		tiles = Art.split(Art.load(fileName), tileWidth, tileHeight, marginWidth, marginHeight);
	}

	public int getTileIndexFromXY(int x, int y)
	{
		return y * tiles.length + x;
	}
	
	public Int2d getTileXYFromIndex(int index)
	{
		return new Int2d(index % tiles.length, index / tiles.length);
	}

	public Image getTileImageFromIndex(int i)
	{
		int y = i / tiles.length;
		int x = i - y * tiles.length;
		return getTileImageFromXY(x,y);
	}

	public Image getTileImageFromXY(int x, int y)
	{
		if(x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length)
			return tiles[x][y];
		return tiles[0][0];
	}

	public void writeToFileOutputStream(FileOutputStream fileOutputStream)
	{
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new  ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(fileName);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void readFromFileInputStream(FileInputStream fileInputStream)
	{
		ObjectInputStream objectInputStream;
		try {
			objectInputStream = new  ObjectInputStream(fileInputStream);
			fileName = (String) objectInputStream.readObject();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
