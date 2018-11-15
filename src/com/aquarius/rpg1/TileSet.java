package com.aquarius.rpg1;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import com.aquarius.common2dgraphics.Art;

public class TileSet
{
	public BufferedImage[][] tiles;
    boolean[][] tileCollision;

	public String fileName;
	public int index;
    public Vector<TilePattern> tilePatterns = new Vector<>();
	private boolean hasTilePattern;
	

	public TileSet(int index, String fileName, int tileWidth, int tileHeight, int marginWidth, int marginHeight, boolean hasTilePattern)
	{
		System.out.println("Loading tileSet " + fileName);
		this.index = index;
		this.fileName = fileName;
		this.hasTilePattern = hasTilePattern;
		tiles = Art.split(Art.load(fileName), tileWidth, tileHeight, marginWidth, marginHeight);
		tileCollision = new boolean[tiles.length][tiles[0].length];
		if(this.hasTilePattern) {
			loadTilePattern();
		}
	}
	
	public void saveTileSetData()
	{
		if(!hasTilePattern)
		{
			System.err.println("No tile pattern data to save for " + fileName);
		}
		String tilePatternFileName = getTilePatternFileName();
		System.out.println("Saving game to " + tilePatternFileName);
		try {
			FileOutputStream fos = new FileOutputStream(tilePatternFileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(tilePatterns);
			oos.writeObject(tileCollision);
			//oos.writeObject(tileCollision);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private String getTilePatternFileName() {
		return getFileNamePart(replaceExtension(this.fileName, "config"));
	}

	private static String replaceExtension(String fileName, String newExtension)
	{
		  int i = fileName.lastIndexOf('.');
		  String newName = fileName.substring(0,i+1);
		  return newName + newExtension;
	}

	private static String getFileNamePart(String fileName) {
		int idx = fileName.replaceAll("\\\\", "/").lastIndexOf("/");
		return idx >= 0 ? fileName.substring(idx + 1) : fileName;
	}
	public TilePattern getTilePatternFromTile(int tileX, int tileY)
	{
		for(TilePattern tilePattern:tilePatterns)
		{
			if(tilePattern.isTileInTilePattern(tileX,tileY))
			{
				return tilePattern;
			}
		}
		return null;
	}

	public void readTileSetDataFromInputStream(FileInputStream fileInputStream)
	{
		try
		{
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			tilePatterns = (Vector<TilePattern>) objectInputStream.readObject();
			tileCollision = (boolean[][]) objectInputStream.readObject();
		}catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void loadTilePattern()
	{
		String tilePatternFileName = getTilePatternFileName();
		FileInputStream fis;
		try {
			fis = new FileInputStream(tilePatternFileName);
			readTileSetDataFromInputStream(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public boolean getTileCollisionFromIndex(int i)
	{
		int y = (i / 256) % 256;
		int x = i % 256;
		return getTileCollisionFromXY(x, y);
	}
	
	public boolean getTileCollisionFromXY(int x, int y) {
		if(x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length)
			return tileCollision[x][y];
		return false;
	}

	public Image getTileImageFromXY(int x, int y)
	{
		if(x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length)
			return tiles[x][y];
		return tiles[0][0];
	}
}
