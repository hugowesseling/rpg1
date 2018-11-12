package com.aquarius.rpg1;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Layer {

	public int[][] tiles;
	private int drawCounter = 0;
	
	public Layer(int width, int height)
	{
		tiles = new int[width][width];
	}

	public void drawLayer(Graphics2D graphics, int imageWidth, int imageHeight, int screenx, int screeny, boolean animate)
	{
		float alpha = 1f;
		if(animate)
		{
			drawCounter = (drawCounter+1) % 100;
			alpha = (float)(Math.sin(Math.PI*drawCounter/50)) * 0.25f + 0.75f;
		}
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		int tileswidth = imageWidth/16 + 2;
		int tilesheight = imageHeight/16 + 2;
		int screenblockx=screenx/16,screenblocky=screeny/16;
		for(int y=screenblocky;y<screenblocky+tilesheight;y++)
		{
			if(y >= 0 && y < tiles[0].length)
			{
				for(int x=screenblockx;x<screenblockx+tileswidth;x++)
				{
					if(x >= 0 && x < tiles.length)
					{
						graphics.drawImage(Resources.getTileImageFromIndex(tiles[x][y]), x*16-screenx, y*16-screeny, null);
					}
				}
			}
		}
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}

	public int getTile(int tileX, int tileY)
	{
		if(tileX >= 0 && tileX < tiles[0].length)
			if(tileY >= 0 && tileY < tiles.length)
			{
				//System.out.println("Getting tile " + tileX + "," + tileY + " to " + tiles[tileX][tileY]);
				return tiles[tileX][tileY];
			}
		return -1;
	}
	
	
	public void setTile(int tileX, int tileY, int tile)
	{
		if(tileX >= 0 && tileX < tiles[0].length)
			if(tileY >= 0 && tileY < tiles.length)
			{
				//System.out.println("Setting tile " + tileX + "," + tileY + " to " + tile);
				tiles[tileX][tileY] = tile;
			}
	}

	public void writeToFileOutputStream(FileOutputStream fileOutputStream)
	{
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new  ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(tiles);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void growLayer()
	{
		int newwidth=100,newheight=100;
		int[][] newTiles = new int[newwidth][newheight];
		for(int x=0;x<tiles.length;x++)
			for(int y=0;y<tiles[0].length;y++)
				newTiles[x][y]=tiles[x][y];
		tiles = newTiles;
	}
	
	public void setAllLayer(int tileIndex)
	{
		for(int x=0;x<tiles.length;x++)
			for(int y=0;y<tiles[0].length;y++)
				tiles[x][y] = tileIndex;
	}
	
	public void readFromFileInputStream(FileInputStream fileInputStream)
	{
		/* temporary string reader
		try {
			new ObjectInputStream(fileInputStream).readObject();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try
		{
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			tiles = (int[][]) objectInputStream.readObject();
			//convert2newIndex();
		}catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*private void convert2newIndex() {
		for(int x=0;x<tiles.length;x++)
			for(int y=0;y<tiles[0].length;y++) {
				int tileX = tiles[x][y] % tileSet.tiles.length;
				int tileY = tiles[x][y] / tileSet.tiles.length;
				tiles[x][y] = TileSet.getTileIndexFromXY(tileX, tileY);
			}
	}*/

	public int getHeight() 
	{
		return tiles[0].length;
	}
	public int getWidth()
	{
		return tiles.length;
	}

	public boolean collides(int xTile, int yTile, int radius) {
		int tileIndex = getTile(xTile, yTile);
		if(tileIndex != -1) {
			boolean result = Resources.getTileCollisionFromIndex(tileIndex);
			System.out.println("Checking " + xTile + ", " + yTile +": " + result);
			return result;
		} else {
			System.out.println("No tileSet found for index " + tileIndex);
			return false;
		}
	}

}
