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
import com.aquarius.rpg1.behavior.hateno.SweepingCharacter;


public class Resources {
	public enum ItemTileLocation implements Position{
		BROOM(14,12),
		SWORD(7,107),
		HAMMER(2,101),
		PIE(14,19),
		SOUP(12,19),
		DIAMOND(1,28),
		BROCCOLI_RING(14,69),
		STONE(4,28);
		public int x,y,index;
		ItemTileLocation(int index){
			this.index=index;
			x = index%256;
			y = index/256;
		}
		ItemTileLocation(int x, int y){
			this.x=x;
			this.y=y;
			index = x+y*256;
		}
		@Override
		public int getX() {
			return x;
		}
		@Override
		public int getY() {
			return y;
		}
	}
	public interface CharacterCreatorFunction {
		public GameObject create(CharacterDrawer characterDrawer, ObjectPosition position, Direction direction);
	}
	public interface ObjectCreatorFunction {
		public GameObject create(TileDrawer tileDrawer, ObjectPosition position, LevelState levelState);
	}
	
	public final static StorableObjectType allStorableObjectTypes[] = {
			new FoodObjectType("cookie", ItemTileLocation.PIE.index, 100),
			new FoodObjectType("soup", ItemTileLocation.SOUP.index, 60),
			new ValueObjectType("diamond", ItemTileLocation.DIAMOND.index, 100),
			new RingObjectType("broccoli ring", ItemTileLocation.BROCCOLI_RING.index, ObjectColor.GREEN),
			new UsableObjectType("Hammer", ItemTileLocation.HAMMER.index),
			new StorableObjectType("Stone", ItemTileLocation.STONE.index)
		};
	public final static HashMap<String, StorableObjectType> allStorableObjectTypesHashMap;
	
	public final static String PARAM_BACKGROUND_SOUND = "background_sound";
	public final static TileSet[] levelTileSets = {
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
	public final static TileSet swordAttack = new TileSet(-3, "/swords.png", 63, 63, 1, 1, false);
	public final static TileSet itemTileSet = new TileSet(-2, "/_sheet.png", 16, 16, 0, 0, false);
	public final static TileSet heartTileSet = new TileSet(-2, "/_hearts_sheet.png", 10, 10, 0, 0, false);
	public final static ArrayList<CharacterTileSet> characterTileSets = new ArrayList<CharacterTileSet>();
	public static ArrayList<DialogStyle> dialogStyles = new ArrayList<DialogStyle>();
    public final static HashMap<String, CharacterCreatorFunction> characterSubClasses = new HashMap<>();
    public final static HashMap<String, ObjectCreatorFunction> objectSubClasses = new HashMap<>();
	protected final static String defaultLevelParameters[] = {PARAM_BACKGROUND_SOUND};
    

    static {
    	System.out.println("Resources.static");
		dialogStyles.add(new DialogStyle(levelTileSets[0].tilePatterns.get(0), true));
		dialogStyles.add(new DialogStyle(levelTileSets[3].tilePatterns.get(0), true));
		
		addCharacterSubClass("HenryCharacter", (drawer, pos, dir) -> {return new HenryCharacter(drawer, pos, dir);});
		addCharacterSubClass("SoupCharacter", (drawer, pos, dir) -> {return new SoupCharacter(drawer, pos, dir);});
		addCharacterSubClass("HoppingCharacter", (drawer, pos, dir) -> {return new HoppingCharacter(drawer, pos, dir);});
		addCharacterSubClass("RunningCharacter", (drawer, pos, dir) -> {return new RunningCharacter(drawer, pos, dir);});
		addCharacterSubClass("ProximityRunCharacter", (drawer, pos, dir) -> {return new ProximityRunCharacter(drawer, pos, dir);});
		addCharacterSubClass("StraightLineRunCharacter", (drawer, pos, dir) -> {return new StraightLineRunCharacter(drawer, pos, dir);});
		addCharacterSubClass("HidingCharacter", (drawer, pos, dir) -> {return new HidingCharacter(drawer, pos, dir);});
		addCharacterSubClass("SweepingCharacter", (drawer, pos, dir) -> {return new SweepingCharacter(drawer, pos, dir);});
		

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
		addCharacterTileSets(1,"/chara5.png", 26, 36, 0, 0);

		allStorableObjectTypesHashMap = new HashMap<>();
		for(StorableObjectType sot: allStorableObjectTypes){
			allStorableObjectTypesHashMap.put(sot.name, sot);
		}
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
		return levelTileSets[i / 65536].getTileImageFromIndexSafe(i % 65536);
	}
	public static boolean getTileCollisionFromIndex(int i) {
		return levelTileSets[i / 65536].getTileCollisionFromIndexSafe(i % 65536);
	}
	public static boolean getLayerHeightFromIndex(int i) {
		return levelTileSets[i / 65536].getLayerHeightFromIndexSafe(i % 65536);
	}

	public static int getCoverageFromIndex(int i) {
		return levelTileSets[i / 65536].getCoverageFromIndexSafe(i % 65536);

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
