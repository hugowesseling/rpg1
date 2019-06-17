package com.aquarius.rpg1;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Layer {

	public static final int DRAW_LOW = 1;
	public static final int DRAW_HIGH = 2;
	public static final int DRAW_LOW_AND_HIGH = 3;
	private static int drawCounter = 0;
	private int[][] tiles;
	private Image[][] images;
	private boolean[][] collisions;
	private boolean[][] layerHeights;
	private int[][] coverages;
	
	public Layer(int width, int height)
	{
		tiles = new int[width][height];
		for(int[] column:tiles) {
			Arrays.fill(column, 1280);
		}
		
		initAndUpdateImagesCollisionsLayerHeights(); 
	}

	public Layer(int[][] tiles) {
		this.tiles = tiles;
		initAndUpdateImagesCollisionsLayerHeights(); 
	}

	private void initAndUpdateImagesCollisionsLayerHeights() {
		int width = getWidth(), height = getHeight();
		images = new Image[width][height];
		collisions = new boolean[width][height];
		layerHeights = new boolean[width][height];
		coverages = new int[width][height];
		updateImagesCollisionsLayerHeightsCoverage();
	}

	private void updateImagesCollisionsLayerHeightsCoverage() {
		System.out.println("updateImagesCollisionsLayerHeights()");
		for(int x=0;x<tiles.length;x++) {
			for(int y=0;y<tiles[0].length;y++) {
				updateImageCollisionLayerHeightCoverageForUncheckedXY(x, y);
			}
		}
	}

	private void updateImageCollisionLayerHeightCoverageForUncheckedXY(int x, int y) {
		updateImageForUnCheckedXY(x, y);
		updateCollisionsForUncheckedXY(x, y);
		updateLayerHeightsForUncheckedXY(x, y);
		updateCoverageForUncheckedXY(x, y);
	}
	
	private void updateLayerHeightsForUncheckedXY(int x, int y) {
		layerHeights[x][y] = Resources.getLayerHeightFromIndex(tiles[x][y]);
	}

	private void updateCollisionsForUncheckedXY(int x, int y) {
		collisions[x][y] = Resources.getTileCollisionFromIndex(tiles[x][y]);
	}

	private void updateCoverageForUncheckedXY(int x, int y) {
		coverages[x][y] = Resources.getCoverageFromIndex(tiles[x][y]);
	}

	private void updateImageForUnCheckedXY(int x, int y) {
		images[x][y] = Resources.getTileImageFromIndex(tiles[x][y]);
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
		if(yend>tiles[0].length)yend=tiles[0].length;
		int xstart = screenblockx, xend = screenblockx+tileswidth;
		if(xstart<0)xstart=0;
		if(xend>tiles.length)xend=tiles.length;
		boolean draw = false;
		for(int y=ystart;y<yend;y++)
		{
			for(int x=xstart;x<xend;x++)
			{
				switch (draw_low_high)
				{
					case DRAW_LOW_AND_HIGH:
						draw = true;
						break;
					case DRAW_LOW:
						draw = !layerHeights[x][y];
						break;
					case DRAW_HIGH:
						draw = layerHeights[x][y];
				}
				if(draw)
					graphics.drawImage(images[x][y], x*16-screenx, y*16-screeny, null);
			}
		}
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}

	public int getTile(int tileX, int tileY)
	{
		if(tileX >= 0 && tileX < getWidth())
			if(tileY >= 0 && tileY < getHeight())
			{
				//System.out.println("Getting tile " + tileX + "," + tileY + " to " + tiles[tileX][tileY]);
				return tiles[tileX][tileY];
			}
		return -1;
	}
	
	
	public void setTileIndexForCheckedXY(int tileX, int tileY, int tileIndex)
	{
		if(tileX >= 0 && tileX < getWidth())
			if(tileY >= 0 && tileY < getHeight())
			{
				//System.out.println("Setting tile " + tileX + "," + tileY + " to " + tile);
				tiles[tileX][tileY] = tileIndex;
				updateImageCollisionLayerHeightCoverageForUncheckedXY(tileX, tileY);
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
		for(int x=0;x<getWidth();x++)
			for(int y=0;y<getHeight();y++)
				tiles[x][y] = tileIndex;
		updateImagesCollisionsLayerHeightsCoverage();
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
		initAndUpdateImagesCollisionsLayerHeights();
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

	public boolean collidesForCheckedXY(int tileX, int tileY) {
		// returns true if x,y out of bounds to ensure that game objects will stay in level
		/*int tileIndex = getTile(xTile, yTile);
		if(tileIndex != -1) {
			boolean result = Resources.getTileCollisionFromIndex(tileIndex);
			//System.out.println("Checking " + xTile + ", " + yTile +": " + result);
			return result;
		} else {
			System.out.println("No tileSet found for index " + tileIndex);
			return false;
		}*/
		if(tileX >= 0 && tileX < getWidth())
			if(tileY >= 0 && tileY < getHeight())
				return collisions[tileX][tileY];
		return true;
	}

	public void resize(int newWidth, int newHeight) {
		int[][] newTiles = new int[newWidth][newHeight];
		int index;
		for(int x=0;x<newWidth;x++)
			for(int y=0;y<newHeight;y++) {
				index = getTile(x,y);
				newTiles[x][y] = index !=-1 ? index : 0;
			}
		setTiles(newTiles);
	}

	public int getTileIndexForUncheckedXY(int x, int y) {
		return tiles[x][y];
	}

	public int[][] cloneTiles() {
		return clone2dArray(tiles);
	}

	public static int[][] clone2dArray(int[][] array2d)
	{
		int [][] theClone = new int[array2d.length][];
		for(int i = 0; i < array2d.length; i++)
			theClone[i] = array2d[i].clone();
		return theClone;
	}

	public boolean areTilesEqual(int[][] tiles1) {
		return layerEqual(tiles1, tiles);
	}

	private boolean layerEqual(int[][] tiles1, int[][] tiles2) 
	{
		// returns true if all tiles are equal
		if(tiles1.length != tiles2.length || tiles1[0].length != tiles2[0].length)
			return false;
		for(int x=0;x<getWidth();x++)
			for(int y=0;y<getHeight();y++)
				if(tiles1[x][y] != tiles2[x][y])return false;
		/*System.out.println("equal layers: ");
		printLayer(tiles1);
		System.out.println("==");
		printLayer(tiles2);*/
		return true;
	}

	public void setTiles(int[][] tiles1) {
		tiles = tiles1;
		initAndUpdateImagesCollisionsLayerHeights();
	}

	public Int2d findClosestCollisionLayerHeight(int tileX, int tileY, boolean colliding, boolean layerHeight) {
		int closestDistance = tiles.length + tiles[0].length;
		int distance;
		boolean found = false;
		Int2d closestPosition = new Int2d(0,0); 
		for(int x=0;x<getWidth();x++)
			for(int y=0;y<getHeight();y++) {
				if(collisions[x][y] == colliding && layerHeights[x][y] == layerHeight) {
					distance = Math.abs(tileX - x) + Math.abs(tileY - y); 
					if(distance < closestDistance) {
						closestDistance = distance;
						closestPosition.x = x;
						closestPosition.y = y;
						found = true;
					}
				}
			}
		if(found)
			return closestPosition;
		return null;
	}

	public Int2d findBestCoverage(int tileX, int tileY, int range, boolean doubleHeight) {
		int xmin = tileX - range, xmax = tileX + range;
		int ymin = tileY - range, ymax = tileY + range;
		if(xmin<0)xmin=0;
		if(ymin<0)ymin=0;
		if(doubleHeight)
			if(ymin<1)ymin=1;

		if(xmax>getWidth())xmax=getWidth();
		if(ymax>getHeight())ymax=getHeight();

		int maxCoverage = 0;
		int coverage;
		boolean found = false;
		Int2d maxCoveragePosition = new Int2d(0,0); 
		for(int x=xmin;x<xmax;x++)
			for(int y=ymin;y<ymax;y++) {
				if((!collisions[x][y]) && layerHeights[x][y]) {
					coverage = coverages[x][y] - (Math.abs(x-tileX) + Math.abs(y-tileY)) * 10;
					if(doubleHeight)
						coverage += coverages[x][y-1];
					if(coverage>maxCoverage) {
						maxCoverage = coverage;
						found = true;
						maxCoveragePosition.x = x;
						maxCoveragePosition.y = y;
					}
				}
			}
		if(found)
			return maxCoveragePosition;
		return null;
	}

	public void replaceTileFromTileSet(TileSet tileSetToReplace, ArrayList<TileSet> tileSetsToChooseFrom, HashMap<Integer, Integer> replacementHashMap) {
		int index;
		for(int x=0;x<getWidth();x++)
			for(int y=0;y<getHeight();y++) {
				index = tiles[x][y];
				TileSet tileSet = Resources.getTileSetFromIndex(index);
				if(tileSet == tileSetToReplace) {
					Integer replacement =  replacementHashMap.get(index);
					int replacementIndex;
					if(replacement == null) {
						System.out.println("Finding replacement for "+ index);
						BufferedImage image = tileSet.getTileImageFromIndexSafe(index);
						replacementIndex = findBestMatchingIndex(image, tileSetsToChooseFrom);
						replacementHashMap.put(index, replacementIndex);
					}else {
						replacementIndex = replacement.intValue();
					}
					tiles[x][y] = replacementIndex;
				}
			}
		updateImagesCollisionsLayerHeightsCoverage();
	}

	private int findBestMatchingIndex(BufferedImage image, ArrayList<TileSet> tileSetsToChooseFrom) {
		
		for(TileSet tileSet: tileSetsToChooseFrom) {
			Int2d bestMatch = tileSet.findMatchingTileXY(image);
			if(bestMatch != null) {
				int index = tileSet.getTileIndexFromXY(bestMatch.x, bestMatch.y);
				System.out.println("Found match in tileSet " + tileSet.fileName + " at index " + index);
				return index;
			}
		}
		System.out.println("No replacement found");
		return 0;
	}


}
