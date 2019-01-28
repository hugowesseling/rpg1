package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;

import com.aquarius.common2dgraphics.util.Input;
import com.aquarius.rpg1.behavior.GameObjectType;
import com.aquarius.rpg1.drawers.CharacterDrawer;
import com.aquarius.rpg1.objects.GameObject;

public class LevelState {
	static final String USER_SAVE_FOLDER = "saves/";
	public String levelBasename = "level";
	public Layer bottom_layer, top_layer;
	public HashMap<String, String> levelKeyValues;	// Any definable level values
	public Vector<GameObject> allGameObjects;	// All characters in this level
	public LevelStack levelStack;
	public Int2d levelPos= new Int2d(0,0);
	private String latestLoadedFileName = null;
	public Vector<GameObject> gameObjectsToAdd;
	private String previousBackgroundMusic;

	Player player;
	private WorldTime worldTime;
	private int tickCounter;
	
	public LevelState(Layer bottom_layer, Layer top_layer) {
		this.bottom_layer = bottom_layer;
		this.top_layer = top_layer;
		levelKeyValues = new HashMap<>();
		allGameObjects = new Vector<>();
		gameObjectsToAdd = new Vector<>();
		levelStack = new LevelStack(bottom_layer, top_layer);

		tickCounter = 0;
		player = new Player(new CharacterDrawer(0), new ObjectPosition(0,0), Direction.SOUTH);
		player.setBeginPosition(this);
		worldTime = new WorldTime();
	
	}

	public void worldTick() {
		worldTime.setTimeMs(System.currentTimeMillis());

		player.inputPlayerMovement(Input.instance, this);

		tickCounter++;
		if(tickCounter % 10 == 0) {
			think(player, worldTime);
		}

		doActionsWeaponAndMovement(worldTime);
		
		player.doActionAndWeapon(worldTime, this);
		player.determineInteractionGameObject(allGameObjects);
		player.checkIfTouching(this);
	}

	public void deleteFilesInUserFolder() {
		File dir= new File(LevelState.USER_SAVE_FOLDER);
		for(File file: dir.listFiles()) 
		    if (!file.isDirectory() && file.toString().endsWith(".rpg1")) {
		    	System.out.println("Deleting "+ file);
		        file.delete();
		    }
	}
	
	public GameObject findRandomCharacterInNeighborhood(GameObjectType objectType, Int2d position, int distance) {
		ArrayList<GameObject> eligibles = new ArrayList<>();
		for(GameObject character : allGameObjects)
		{
			if(character.getPosition().tileAsInt2d().nearby(position, distance))
			{
				eligibles.add(character);
			}
		}
		if(eligibles.size() == 0)
			return null;
		int rnd = new Random().nextInt(eligibles.size());
	    return eligibles.get(rnd);
	}

	public Int2d findRandomPositionInNeighborhood(int tileIndex, Int2d position, int distance) {
		// Searches in a square with sides of distance * 2
		int x1 = position.x - distance, x2 = position.x + distance;
		int y1 = position.y - distance, y2 = position.y + distance;
		if(x1 < 0) x1 = 0;
		if(y1 < 0) y1 = 0;
		if(x2 > bottom_layer.getWidth())x2 = bottom_layer.getWidth();
		if(y2 > bottom_layer.getHeight())y2 = bottom_layer.getHeight();
		ArrayList<Int2d> positions = new ArrayList<>();
		for(int y = y1; y < y2; y++)
		{
			for(int x = x1; x < x2; x++) {
				if(bottom_layer.getTileIndexForUncheckedXY(x,y) == tileIndex ||
				   top_layer.getTileIndexForUncheckedXY(x,y) == tileIndex) {
					positions.add(new Int2d(x,y));
				}
			}
		}
		if(!positions.isEmpty()) {
			int rnd = new Random().nextInt(positions.size());
		    return positions.get(rnd);
		}else {
			return null;			
		}
	}

	public int getWidth() {
		return bottom_layer.getWidth();
	}

	public int getHeight() {
		return bottom_layer.getHeight();
	}

	public void draw(Graphics2D imageGraphics, int imageWidth, int imageHeight, int screenx, int screeny, int frameCounter, boolean simulating, Player player) {
		bottom_layer.drawLayer(imageGraphics, imageWidth, imageHeight, screenx, screeny, false, Layer.DRAW_LOW_AND_HIGH);
		top_layer.drawLayer(imageGraphics,imageWidth, imageHeight, screenx, screeny, !simulating, Layer.DRAW_LOW);
		//imageGraphics.drawImage(characterTileSet.getTileImageFromXY((frameCounter/10) % 3, charDirection), 100, 100, null);
		for(GameObject gameCharacter: allGameObjects){
			gameCharacter.draw(imageGraphics, frameCounter, screenx, screeny, simulating);
		}
		if(simulating)
			player.draw(imageGraphics, frameCounter, screenx, screeny, simulating);
		top_layer.drawLayer(imageGraphics,imageWidth, imageHeight, screenx, screeny, !simulating, Layer.DRAW_HIGH);
	}

	public void doActionsWeaponAndMovement(WorldTime worldState) {
		// Do actions and movement
		for(GameObject gameCharacter: allGameObjects) {
			gameCharacter.doActionAndWeapon(worldState, this);
			//gameCharacter.doMovement(this);
		}
	}
	public void think(Player player, WorldTime worldState) {
		// Do actions and movement
		Vector<GameObject> newGameObjects = new Vector<>();
		for(GameObject gameCharacter: allGameObjects) {
			gameCharacter.think(player, worldState, this);
			//Remove everything with zero or less health
			if(gameCharacter.getHealth() > 0)
				newGameObjects.add(gameCharacter);
		}
		player.think(player, worldState, this);
		if(gameObjectsToAdd.size()>0) {
			newGameObjects.addAll(gameObjectsToAdd);
			gameObjectsToAdd.clear();
		}
		allGameObjects = newGameObjects;
	}

	public void writeToFileOutputStream(FileOutputStream fileOutputStream) {
		bottom_layer.writeToFileOutputStream(fileOutputStream);
		top_layer.writeToFileOutputStream(fileOutputStream);
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new  ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(allGameObjects);
			objectOutputStream.writeObject(levelKeyValues);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void readFromFileInputStream(FileInputStream fileInputStream) {
		bottom_layer.readFromFileInputStream(fileInputStream);
		top_layer.readFromFileInputStream(fileInputStream);
		levelStack.clearStack();
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(fileInputStream);
						
			allGameObjects = (Vector<GameObject>) ois.readObject();
			for(GameObject character:allGameObjects) {
				System.out.println("Read GameCharacter: " + character);
			}
			levelKeyValues = (HashMap<String, String>) ois.readObject();
			for(Entry<String, String> entry:levelKeyValues.entrySet()) {
				System.out.println("Read key value: \"" + entry.getKey() + "\" = \"" + entry.getValue() + "\"");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void loadLevelByPosition()
	{
		String fileName = levelPos2FileName(levelBasename, levelPos);
		System.out.println("Reading layer from " + fileName);
		loadLevel(fileName);
		
		String backgroundFileNameToPlay = levelKeyValues.get(Resources.PARAM_BACKGROUND_SOUND);
		if(backgroundFileNameToPlay != null) {
			if(backgroundFileNameToPlay.equals(previousBackgroundMusic))
				AudioSystemPlayer.stopAllExcept(new ArrayList<String>(Arrays.asList(new String[]{AudioSystemPlayer.AUDIO_FOLDER + previousBackgroundMusic})));
			else
				AudioSystemPlayer.stopAll();
			AudioSystemPlayer.playSound(AudioSystemPlayer.AUDIO_FOLDER + backgroundFileNameToPlay, true);
			previousBackgroundMusic = backgroundFileNameToPlay; 
		}else {
			AudioSystemPlayer.stopAllExcept(new ArrayList<String>(Arrays.asList(new String[]{AudioSystemPlayer.AUDIO_FOLDER + previousBackgroundMusic})));			
		}
	}
	
	public void loadLevel(String fileName) {
		if(latestLoadedFileName != null)
			saveToFile(USER_SAVE_FOLDER + latestLoadedFileName);
		latestLoadedFileName = fileName;
		FileInputStream fileInputStream;
		try {
			// Try to load level from save folder
			File fileToLoad = new File(USER_SAVE_FOLDER + fileName);
			if(!fileToLoad.exists()) {
				System.out.println("User saved file " + fileToLoad + " does not exist, loading beginning level");
				fileToLoad =  new File(fileName);
			}
			fileInputStream = new FileInputStream(fileToLoad);
			readFromFileInputStream(fileInputStream);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveToFile(String fileName)
	{
		System.out.println("Saving game to " + fileName);
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(fileName);
			writeToFileOutputStream(fileOutputStream);
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public static String levelPos2FileName(String levelBasename, Int2d levelpos) {
		return String.format(levelBasename  + "_%03d_%03d.rpg1", levelpos.x, levelpos.y);
	}

	public void setLevelPos(String levelBasename, Int2d levelPos) {
		if(!this.levelPos.is(levelPos) || this.levelBasename != levelBasename) {
			this.levelPos = levelPos;
			this.levelBasename = levelBasename;
			// Load level at beginning location
			loadLevelByPosition();
		}
	}

	public String getLatestLevelFileName() {
		if(latestLoadedFileName != null)
			return latestLoadedFileName;
		return "";
	}

	public void loadLevelByExit(int dx, int dy, Player player) {
		levelPos.x += dx;
		levelPos.y += dy;
		ObjectPosition newPosition = player.getPosition();
		if(dx<0)
			newPosition.x = (getWidth()-2) * Constant.TILE_WIDTH;
		if(dx>0)
			newPosition.x = Constant.TILE_WIDTH * 2;
		if(dy<0)
			newPosition.y = (getHeight()-2) * Constant.TILE_HEIGHT;
		if(dy>0)
			newPosition.y = Constant.TILE_HEIGHT * 2;
		player.setPosition(newPosition);
		loadLevelByPosition();
	}

	public Int2d getLevelPos() {
		return levelPos;
	}

	public void resizeLevel(int newWidth, int newHeight) {
		bottom_layer.resize(newWidth, newHeight);
		top_layer.resize(newWidth, newHeight);
	}

	public boolean collidesTilePosition(int xTile, int yTile) {
		// Return true if one of the layers collides
		if(top_layer.collidesForCheckedXY(xTile, yTile))
			return true;
		return bottom_layer.collidesForCheckedXY(xTile, yTile); 
	}
	
	public boolean collidesObjectPositionHitbox(int x, int y, int xMin, int yMin, int xMax, int yMax) {
		if(collidesTilePosition(ObjectPosition.getXTileFromX(x+xMax), ObjectPosition.getYTileFromY(y+yMax)))return true;
		if(collidesTilePosition(ObjectPosition.getXTileFromX(x+xMin), ObjectPosition.getYTileFromY(y+yMax)))return true;
		if(collidesTilePosition(ObjectPosition.getXTileFromX(x+xMin), ObjectPosition.getYTileFromY(y+yMin)))return true;
		if(collidesTilePosition(ObjectPosition.getXTileFromX(x+xMax), ObjectPosition.getYTileFromY(y+yMin)))return true;
		return false;
	}

	public int getTopCollidingTileForXYChecked(int tileX, int tileY) {
		// Returns the tileIndex of the top most colliding tile, if out of bounds or nothing is colliding, return -1
		if(top_layer.collidesForCheckedXY(tileX, tileY))
			return top_layer.getTile(tileX, tileY);
		if(bottom_layer.collidesForCheckedXY(tileX, tileY))
			return bottom_layer.getTile(tileX, tileY);
		return -1;
	}

	public void replaceTopCollidingTileForXYChecked(int tileX, int tileY, int tileIndex) {
		if(top_layer.collidesForCheckedXY(tileX, tileY))
			top_layer.setTileIndexForCheckedXY(tileX, tileY, tileIndex);
		if(bottom_layer.collidesForCheckedXY(tileX, tileY))
			bottom_layer.setTileIndexForCheckedXY(tileX, tileY, tileIndex);
	}

	public Int2d findHidingPlace(ObjectPosition position, int radius, boolean doubleHeight) {
		// returns null if nothing found
		int tileX = position.getXTile(), tileY = position.getYTile();
		// get all hiding places and find closest
		Int2d tilePosition = top_layer.findBestCoverage(tileX, tileY, 20, doubleHeight);
		return tilePosition;
	}

	public void replaceTilesFromTileSet(TileSet tileSet) {
		HashMap<Integer, Integer> replacementHashMap = new HashMap<>();
		
		ArrayList<TileSet> tileSetsToChooseFrom = new ArrayList<>();
		for(TileSet tileSetToChooseFrom: Resources.levelTileSets) {
			if(tileSetToChooseFrom != tileSet)
				tileSetsToChooseFrom.add(tileSetToChooseFrom);
		}
		levelStack.pushLayers();
		top_layer.replaceTileFromTileSet(tileSet, tileSetsToChooseFrom , replacementHashMap);
		bottom_layer.replaceTileFromTileSet(tileSet, tileSetsToChooseFrom, replacementHashMap);
		levelStack.popLayersIfNoChange();
	}

	public void loadLevelByPlayerPosition(Player player)
	{
		if(player.getPosition().x < Constant.TILE_WIDTH * 1)
		{
			loadLevelByExit(-1, 0, player);
		}
		if(player.getPosition().x > (getWidth()-1) * Constant.TILE_WIDTH)
		{
			loadLevelByExit(1, 0, player);
		}
		if(player.getPosition().y < Constant.TILE_HEIGHT * 1)
		{
			loadLevelByExit(0, -1, player);
		}
		if(player.getPosition().y > (getHeight()-1) * Constant.TILE_HEIGHT)
		{
			loadLevelByExit(0, 1, player);
		}
	}
}
