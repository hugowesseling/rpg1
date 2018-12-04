package com.aquarius.rpg1;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;

public class Resources {
	public static final String PARAM_BACKGROUND_SOUND = "background_sound";
	public static TileSet[] levelTileSets = {
			new TileSet(0, "/roguelikeSheet_transparent.png", 16, 16, 1, 1, true),
			new TileSet(1, "/tileA5_outside.png", 16, 16, 0, 0, true),
			new TileSet(2, "/tileB_inside.png", 16, 16, 0, 0, true),
			new TileSet(3, "/window2.png", 16,16,0,0, true),
			new TileSet(4, "/castle.png", 16,16,0,0, true),
			new TileSet(5, "/tileA1_outside.png", 16,16,0,0, true),
			new TileSet(6, "/tileA2_world.png", 16,16,0,0, true),
			new TileSet(7, "/tileA5_dungeon1.png", 16,16,0,0, true),
			new TileSet(8, "/tileB_outside.png", 16,16,0,0, true),
			new TileSet(9, "/tileC_town1.png", 16,16,0,0, true),
			new TileSet(10, "/tileB_dungeon.png", 16,16,0,0, true)
	};
	public static TileSet swordAttack = new TileSet(-3, "/swords.png", 63, 63, 1, 1, false);
	public static ArrayList<CharacterTileSet> characterTileSets = new ArrayList<CharacterTileSet>();
	public static ArrayList<DialogStyle> dialogStyles = new ArrayList<DialogStyle>();
    public static ArrayList<String> characterSubClasses = new ArrayList<String>();
    public static ArrayList<String> objectSubClasses = new ArrayList<String>();
	protected static String defaultLevelParameters[] = {PARAM_BACKGROUND_SOUND};
    

    static {
    	System.out.println("Resources.static");
		dialogStyles = new ArrayList<>();
		dialogStyles.add(new DialogStyle(levelTileSets[0].tilePatterns.get(0), true));
		dialogStyles.add(new DialogStyle(levelTileSets[3].tilePatterns.get(0), true));
		
		addCharacterSubClass("HenryCharacter");
		addCharacterSubClass("SoupCharacter");
		addCharacterSubClass("HoppingCharacter");
		addCharacterSubClass("RunningCharacter");
		addCharacterSubClass("ProximityRunCharacter");
		
		
		addObjectSubClass("TreasureObject");
		addObjectSubClass("DoorwayObject");
		addObjectSubClass("StorableObject");
		
		addCharacterTileSets(0,"/characters1.png", 26, 36, 0, 0);
		addCharacterTileSets(1,"/animals1.png", 26, 36, 0, 0);
		addCharacterTileSets(1,"/animals2.png", 42, 36, 0, 0);
		addCharacterTileSets(1,"/animals3.png", 42, 36, 0, 0);
		addCharacterTileSets(1,"/animals4.png", 42, 36, 0, 0);
		addCharacterTileSets(1,"/animals5.png", 50, 46, 0, 0);
		addCharacterTileSets(1,"/birds1.png", 42, 36, 0, 0);
		addCharacterTileSets(1,"/birds2.png", 42, 36, 0, 0);
		addCharacterTileSets(1,"/horse1.png", 60, 64, 0, 0);
		addCharacterTileSets(1,"/monster1.png", 60, 64, 0, 0);
		addCharacterTileSets(1,"/monster2.png", 60, 64, 0, 0);
    }

	public static void addCharacterSubClass(String string) {
		System.out.println("addCharacterSubClass: Adding " + string);
		characterSubClasses.add(string);
	}

	private static void addCharacterTileSets(int index, String fileName, int tileWidth, int tileHeight, int marginWidth, int marginHeight) {
		TileSet tileSet = new TileSet(index, fileName, tileWidth, tileHeight, marginWidth, marginHeight, false);
		for(int x = 0; x < tileSet.tiles.length; x+=3)
			for(int y = 0; y < tileSet.tiles[0].length; y+=4)
				characterTileSets.add(new CharacterTileSet(tileSet, new Int2d(x, y)));
	}

	public static void addObjectSubClass(String string) {
		System.out.println("addObjectSubClass: Adding " + string);
		objectSubClasses.add(string);

	}
	
	public static Image getTileImageFromIndex(int i) {
		return levelTileSets[i / 65536].getTileImageFromIndex(i % 65536);
	}
	public static boolean getTileCollisionFromIndex(int i) {
		return levelTileSets[i / 65536].getTileCollisionFromIndex(i % 65536);
	}
	public static boolean getLayerHeightFromIndex(int i) {
		return levelTileSets[i / 65536].getLayerHeightFromIndex(i % 65536);
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

	public static void createLevel(String fileName, int width, int height) {
		// Create level in current folder
		LevelState levelState = new LevelState(new Layer(width, height), new Layer(width, height));
		levelState.saveToFile(fileName);
	}


}
