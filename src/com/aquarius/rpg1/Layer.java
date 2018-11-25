package com.aquarius.rpg1;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Layer {

	public static final int DRAW_LOW = 1;
	public static final int DRAW_HIGH = 2;
	public static final int DRAW_LOW_AND_HIGH = 3;
	public int[][] tiles;
	private int drawCounter = 0;
	
	public Layer(int width, int height)
	{
		tiles = new int[width][width];
	}

	public void drawLayer(Graphics2D graphics, int imageWidth, int imageHeight, int screenx, int screeny, boolean animate, int draw_low_high)
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
		int ystart = screenblocky, yend = screenblocky+tilesheight;
		if(ystart<0)ystart=0;
		if(yend>=tiles[0].length)yend=tiles[0].length-1;
		int xstart = screenblockx, xend = screenblockx+tileswidth;
		if(xstart<0)xstart=0;
		if(xend>=tiles.length)xend=tiles.length-1;
		int index;
		boolean draw = false;
		for(int y=ystart;y<yend;y++)
		{
			for(int x=xstart;x<xend;x++)
			{
				index = tiles[x][y];
				switch (draw_low_high)
				{
					case DRAW_LOW_AND_HIGH:
						draw = true;
						break;
					case DRAW_LOW:
						draw = !Resources.getLayerHeightFromIndex(index);
						break;
					case DRAW_HIGH:
						draw = Resources.getLayerHeightFromIndex(index);
				}
				if(draw)
					graphics.drawImage(Resources.getTileImageFromIndex(index), x*16-screenx, y*16-screeny, null);
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
			//System.out.println("Checking " + xTile + ", " + yTile +": " + result);
			return result;
		} else {
			System.out.println("No tileSet found for index " + tileIndex);
			return false;
		}
	}

	public void resize(int newWidth, int newHeight) {
		int[][] newTiles = new int[newWidth][newHeight];
		int index;
		for(int x=0;x<newTiles.length;x++)
			for(int y=0;y<newTiles[0].length;y++) {
				index = getTile(x,y);
				newTiles[x][y] = index !=-1 ? index : 0;
			}
		tiles = newTiles;
	}

}
