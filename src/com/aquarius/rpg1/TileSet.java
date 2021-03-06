package com.aquarius.rpg1;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import com.aquarius.common2dgraphics.Art;
import com.aquarius.rpg1.Resources.ItemTileLocation;

public class TileSet
{
	public BufferedImage[][] tiles;
    boolean[][] tileCollision;
	boolean[][] layerHeight;
	int[][] coverage;

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
		layerHeight = new boolean[tiles.length][tiles[0].length];
		coverage = new int[tiles.length][tiles[0].length];
		determineCoverageForAll();
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
			oos.writeObject(layerHeight);
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
	public TilePattern getTilePatternFromTile(int tileIndex)
	{
		for(TilePattern tilePattern:tilePatterns)
		{
			if(tilePattern.isTileInTilePattern(tileIndex))
			{
				return tilePattern;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void readTileSetDataFromInputStream(FileInputStream fileInputStream)
	{
		try
		{
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			tilePatterns = (Vector<TilePattern>) objectInputStream.readObject();
			tileCollision = (boolean[][]) objectInputStream.readObject();
			layerHeight = (boolean[][]) objectInputStream.readObject();
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
		//System.out.println("getTileIndexFromXY: " + retval);
		return retval;
	}
	
	public static Int2d getTileXYFromIndex(int index)
	{
		return new Int2d(index % 256, (index / 256) % 256);
	}

	public BufferedImage getTileImageFromIndexSafe(int i)
	{
		int y = (i / 256) % 256;
		int x = i % 256;
		return getTileImageFromXYSafe(x,y);
	}
	
	public boolean getTileCollisionFromIndexSafe(int i)
	{
		int y = (i / 256) % 256;
		int x = i % 256;
		return getTileCollisionFromXYSafe(x, y);
	}
	
	public boolean getTileCollisionFromXYSafe(int x, int y) {
		if(x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length)
			return tileCollision[x][y];
		return false;
	}

	public boolean getLayerHeightFromIndexSafe(int i) {
		int y = (i / 256) % 256;
		int x = i % 256;
		return getLayerHeightFromXYSafe(x, y);
	}
	public boolean getLayerHeightFromXYSafe(int x, int y) {
		if(x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length)
			return layerHeight[x][y];
		return false;
	}

	public BufferedImage getTileImageFromXYSafe(int x, int y)
	{
		if(x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length)
			return tiles[x][y];
		return tiles[0][0];
	}

	public int getCoverageFromIndexSafe(int i) {
		int y = (i / 256) % 256;
		int x = i % 256;
		return getCoverageForXYSafe(x, y);
	}

	private int getCoverageForXYSafe(int x, int y) {
		if(x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length)
			return coverage[x][y];
		return 0;
	}

	public int getCoverageForXYUnsafe(int x, int y) {
		return coverage[x][y];
	}

	private static int determineCoverage(BufferedImage image) {
		final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int alphaCount = 0;
        for(int pixel:pixels) {
            if( (pixel & 0xff) != 0) alphaCount ++;
		}
		return alphaCount;
	}
	private void determineCoverageForAll() {

		for(int x=0 ; x< tiles.length ; x++) {
			for(int y =0; y<tiles[0].length; y++) {
				//System.out.println("Coverage for: "+x+","+y);
				coverage[x][y] = determineCoverage(tiles[x][y]);
			}
		}
	}

	public Image getTileImageFromPositionUnsafe(Position pos) {
		return tiles[pos.getX()][pos.getY()];
	}


	public Int2d findMatchingTileXY(BufferedImage image) {
		for(int x=0 ; x< tiles.length ; x++) {
			for(int y =0; y<tiles[0].length; y++) {
				if(imagesAreEqual(image, tiles[x][y])){
					return new Int2d(x,y);
				}
			}
		}
		return null;
	}


	private boolean imagesAreEqual(BufferedImage image1, BufferedImage image2) {
		final int[] pixels1 = ((DataBufferInt) image1.getRaster().getDataBuffer()).getData();
		final int[] pixels2 = ((DataBufferInt) image2.getRaster().getDataBuffer()).getData();

		if(pixels1.length != pixels2.length)return false;
        for(int i=0;i<pixels1.length;i++) {
        	if(pixels1[i] != pixels2[i] && ((pixels1[i] & 255) != (pixels2[i] & 255) || (pixels1[i] & 255) != 0))   
        		return false;
        }
		return true;
	}
}
