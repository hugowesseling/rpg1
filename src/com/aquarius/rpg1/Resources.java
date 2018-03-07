package com.aquarius.rpg1;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Vector;

public class Resources {
	public static TileSet[] levelTileSets = {
			new TileSet(0, "/roguelikeSheet_transparent.png", 16, 16, 1, 1),
			new TileSet(1, "/tileA5_outside.png", 16, 16, 0, 0)
	};
	public static TileSet characterTileSets = new TileSet(-1, "/characters1.png", 26, 36, 0, 0);
	public static ArrayList<DialogStyle> dialogStyles = new ArrayList<DialogStyle>();
    public static Vector<TilePattern> tilePatterns = new Vector<>();
    public static ArrayList<String> characterSubClasses = new ArrayList<String>(); 

    static {
    	System.out.println("Resources.static");
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
			dialogStyles.add(new DialogStyle(Resources.tilePatterns.get(4), levelTileSets[0]));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void addCharacterSubClass(String string) {
		System.out.println("addCharacterSubClass: Adding " + string);
		characterSubClasses.add(string);
	}

	public static Image getTileImageFromIndex(int i) {
		return levelTileSets[i / 65536].getTileImageFromIndex(i % 65536);
	}

	public static String[] getTileSetNames() {
		String[] names = new String[levelTileSets.length];
		int index = 0;
		for(TileSet tileSet: levelTileSets)
			names[index++] = tileSet.fileName;
		return names;
	}
}
