package com.aquarius.rpg1;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

import com.aquarius.rpg1.behavior.hateno.HenryCharacter;
import com.aquarius.rpg1.behavior.hateno.HidingCharacter;
import com.aquarius.rpg1.behavior.hateno.HoppingCharacter;
import com.aquarius.rpg1.behavior.hateno.ProximityRunCharacter;
import com.aquarius.rpg1.behavior.hateno.RunningCharacter;
import com.aquarius.rpg1.behavior.hateno.SoupCharacter;
import com.aquarius.rpg1.behavior.hateno.StraightLineRunCharacter;


public class Resources {
	public interface CharacterCreatorFunction {
		public GameObject create(CharacterDrawer characterDrawer, ObjectPosition position, Direction direction);
	}
	public interface ObjectCreatorFunction {
		public GameObject create(TileDrawer tileDrawer, ObjectPosition position, LevelState levelState);
	}
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
	public static TileSet itemTileSet = new TileSet(-2, "/_sheet.png", 16, 16, 0, 0, false);
	public static ArrayList<CharacterTileSet> characterTileSets = new ArrayList<CharacterTileSet>();
	public static ArrayList<DialogStyle> dialogStyles = new ArrayList<DialogStyle>();
    public static HashMap<String, CharacterCreatorFunction> characterSubClasses = new HashMap<>();
    public static HashMap<String, ObjectCreatorFunction> objectSubClasses = new HashMap<>();
	protected static String defaultLevelParameters[] = {PARAM_BACKGROUND_SOUND};
    

    static {
    	System.out.println("Resources.static");
		dialogStyles = new ArrayList<>();
		dialogStyles.add(new DialogStyle(levelTileSets[0].tilePatterns.get(0), true));
		dialogStyles.add(new DialogStyle(levelTileSets[3].tilePatterns.get(0), true));
		
		addCharacterSubClass("HenryCharacter", (drawer, pos, dir) -> {return new HenryCharacter(drawer, pos, dir);});
		addCharacterSubClass("SoupCharacter", (drawer, pos, dir) -> {return new SoupCharacter(drawer, pos, dir);});
		addCharacterSubClass("HoppingCharacter", (drawer, pos, dir) -> {return new HoppingCharacter(drawer, pos, dir);});
		addCharacterSubClass("RunningCharacter", (drawer, pos, dir) -> {return new RunningCharacter(drawer, pos, dir);});
		addCharacterSubClass("ProximityRunCharacter", (drawer, pos, dir) -> {return new ProximityRunCharacter(drawer, pos, dir);});
		addCharacterSubClass("StraightLineRunCharacter", (drawer, pos, dir) -> {return new StraightLineRunCharacter(drawer, pos, dir);});
		addCharacterSubClass("HidingCharacter", (drawer, pos, dir) -> {return new HidingCharacter(drawer, pos, dir);});		

		addObjectSubClass("TreasureObject", (drawer, pos, state) -> {return new TreasureObject(drawer, pos);});
		addObjectSubClass("DoorwayObject", (drawer, pos, state) -> {return new DoorwayObject(drawer, pos, state.getLevelPos());});
		addObjectSubClass("LockedDoorwayObject", (drawer, pos, state) -> {return new LockedDoorwayObject(drawer, pos, state.getLevelPos());});
		addObjectSubClass("StorableObject", (drawer, pos, state) -> {return StorableObject.createStorableObject(pos);});
		
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

	public static void addCharacterSubClass(String string, CharacterCreatorFunction func) {
		System.out.println("addCharacterSubClass: Adding " + string);
		characterSubClasses.put(string, func);
	}

	public static void addObjectSubClass(String string, ObjectCreatorFunction func) {
		System.out.println("addObjectSubClass: Adding " + string);
		objectSubClasses.put(string, func);
	}

	private static void addCharacterTileSets(int index, String fileName, int tileWidth, int tileHeight, int marginWidth, int marginHeight) {
		TileSet tileSet = new TileSet(index, fileName, tileWidth, tileHeight, marginWidth, marginHeight, false);
		for(int x = 0; x < tileSet.tiles.length; x+=3)
			for(int y = 0; y < tileSet.tiles[0].length; y+=4)
				characterTileSets.add(new CharacterTileSet(tileSet, new Int2d(x, y)));
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

	public static int getCoverageFromIndex(int i) {
		return levelTileSets[i / 65536].getCoverageFromIndex(i % 65536);

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
