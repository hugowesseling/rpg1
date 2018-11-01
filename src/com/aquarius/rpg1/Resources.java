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
			new TileSet(0, "/roguelikeSheet_transparent.png", 16, 16, 1, 1, true),
			new TileSet(1, "/tileA5_outside.png", 16, 16, 0, 0, true)
	};
	public static TileSet swordAttack = new TileSet(-3, "/swords.png", 63, 63, 1, 1, false);
	public static TileSet characterTileSet = new TileSet(-1, "/characters1.png", 26, 36, 0, 0, false);
	public static ArrayList<DialogStyle> dialogStyles = new ArrayList<DialogStyle>();
    public static ArrayList<String> characterSubClasses = new ArrayList<String>(); 

    static {
    	System.out.println("Resources.static");
		dialogStyles = new ArrayList<>();
		dialogStyles.add(new DialogStyle(4, levelTileSets[0]));
    }

	public static void addCharacterSubClass(String string) {
		System.out.println("addCharacterSubClass: Adding " + string);
		characterSubClasses.add(string);
	}

	public static Image getTileImageFromIndex(int i) {
		return levelTileSets[i / 65536].getTileImageFromIndex(i % 65536);
	}
	 public static boolean getTileCollisionFromIndex(int i) {
		 return levelTileSets[i / 65536].getTileCollisionFromIndex(i % 65536);
	 }

	public static String[] getTileSetNames() {
		String[] names = new String[levelTileSets.length];
		int index = 0;
		for(TileSet tileSet: levelTileSets)
			names[index++] = tileSet.fileName;
		return names;
	}

	public static void saveTileSetData() {
		for(TileSet tileSet:levelTileSets)
		{
			tileSet.saveTileSetData();
		}
	}
}
