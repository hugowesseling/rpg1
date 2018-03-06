package com.aquarius.rpg1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Vector;

public class Resources {
	public static TileSet levelTileSet = new TileSet("/roguelikeSheet_transparent.png", 16, 16, 1, 1);
	public static TileSet characterTileSets = new TileSet("/characters1.png", 26, 36, 0, 0);
	public static ArrayList<DialogStyle> dialogStyles = new ArrayList<DialogStyle>();
    public static Vector<TilePattern> tilePatterns = new Vector<>();

    static {
    	loadConfig("rpg1.config");
    }
	public static TilePattern getTilePatternFromTile(int tileX, int tileY)
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

	public static void readTilePatternsFromOutputStream(FileInputStream fileInputStream)
	{
		try
		{
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			tilePatterns = (Vector<TilePattern>) objectInputStream.readObject();
		}catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void loadConfig(String fileName)
	{
		FileInputStream fis;
		try {
			fis = new FileInputStream(fileName);
			readTilePatternsFromOutputStream(fis);
			dialogStyles = new ArrayList<>();
			dialogStyles.add(new DialogStyle(Resources.tilePatterns.get(4), levelTileSet));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
