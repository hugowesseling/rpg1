package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;
import com.aquarius.rpg1.behavior.GameObjectType;

public class LevelState {
	public Layer bottom_layer, top_layer;
	public HashMap<String, String> levelKeyValues;	// Any definable level values
	public Vector<GameObject> allGameObjects;	// All characters in this level
	public LevelStack levelStack;
	private Int2d levelPos= new Int2d(0,0);
	private String latestLoadedFileName = "";
	private ObjectPosition playerPositionInSuperLevel;

	
	public LevelState(Layer bottom_layer, Layer top_layer) {
		this.bottom_layer = bottom_layer;
		this.top_layer = top_layer;
		levelKeyValues = new HashMap<>();
		allGameObjects = new Vector<>();
		levelStack = new LevelStack(bottom_layer, top_layer);
		playerPositionInSuperLevel = null;
	}

	public GameObject findRandomCharacterInNeighborhood(GameObjectType objectType, Int2d position, int distance) {
		ArrayList<GameObject> eligibles = new ArrayList<>();
		for(GameObject character : allGameObjects)
		{
			if(character.position.tileAsInt2d().nearby(position, distance))
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
			for(int x = x1; x < x2; x++)
			{
				if(bottom_layer.tiles[x][y] == tileIndex || top_layer.tiles[x][y] == tileIndex)
				{
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

	public void draw(Graphics2D imageGraphics, int imageWidth, int imageHeight, int screenx, int screeny, int frameCounter, boolean doFade) {
		bottom_layer.drawLayer(imageGraphics, imageWidth, imageHeight, screenx, screeny, false);
		top_layer.drawLayer(imageGraphics,imageWidth, imageHeight, screenx, screeny, true && doFade);
		//imageGraphics.drawImage(characterTileSet.getTileImageFromXY((frameCounter/10) % 3, charDirection), 100, 100, null);
		for(GameObject gameCharacter: allGameObjects){
			gameCharacter.draw(imageGraphics, frameCounter, screenx, screeny);
		}
			}

	public void doActionsWeaponAndMovement(WorldState worldState) {
		// Do actions and movement
		for(GameObject gameCharacter: allGameObjects) {
			gameCharacter.doActionAndWeapon(worldState, this);
			gameCharacter.doMovement();
		}
	}
	public void think(Player player, WorldState worldState) {
		// Do actions and movement
		for(GameObject gameCharacter: allGameObjects) {
			gameCharacter.think(player, worldState, this);
		}
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
			//allCharacters = new Vector<>();
			allGameObjects = (Vector<GameObject>) ois.readObject();
			for(GameObject character:allGameObjects) {
				System.out.println("Read GameCharacter: " + character);
			}
			levelKeyValues = (HashMap<String, String>) ois.readObject();
			for(Entry<String, String> entry:levelKeyValues.entrySet()) {
				System.out.println("Read key value: \"" + entry.getKey() + "\" = \"" + entry.getValue() + "\"");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean collides(ObjectPosition position, int radius) {
		// Return true if one of the layers collides
		if(this.top_layer.collides(position.getXTile(), position.getYTile(), radius))
			return true;
		return this.bottom_layer.collides(position.getXTile(), position.getYTile(), radius); 
	}
	public void loadLevelByPosition()
	{
		String fileName = levelPos2FileName(levelPos);
		System.out.println("Reading layer from " + fileName);
		loadLevel(fileName);
		
		String backgroundFileNameToPlay = levelKeyValues.get(Resources.PARAM_BACKGROUND_SOUND);
		if(backgroundFileNameToPlay != null) {
			AudioSystemPlayer.playSound(backgroundFileNameToPlay, true);
		}
	}
	
	public void loadLevel(String fileName) {
		latestLoadedFileName = fileName;
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(fileName);
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
	
	private static String levelPos2FileName(Int2d levelpos) {
		return String.format("level_%03d_%03d.rpg1", levelpos.x, levelpos.y);
	}

	public void setLevelPos(Int2d levelPos) {
		this.levelPos = levelPos;
		
	}

	public String getLevelFileName() {
		return latestLoadedFileName;
	}

	public void loadLevelByExit(int dx, int dy, Player player) {
		if(playerPositionInSuperLevel != null) {
			player.position = playerPositionInSuperLevel;
			player.position.x += dx*2;
			player.position.y += dy*2;
			playerPositionInSuperLevel = null;
		}else {
			levelPos.x += dx;
			levelPos.y += dy;
			if(dx<0)
				player.position.x = (getWidth()-2) * Constant.TILE_WIDTH;
			if(dx>0)
				player.position.x = Constant.TILE_WIDTH * 2;
			if(dy<0)
				player.position.y = (getHeight()-2) * Constant.TILE_HEIGHT;
			if(dy>0)
				player.position.y = Constant.TILE_HEIGHT * 2;
		}
		loadLevelByPosition();
		AudioSystemPlayer.stopAll();
		AudioSystemPlayer.playSound("D:\\download\\humble_bundle\\gamedev\\sfx\\prosoundcollection_audio\\prosoundcollection\\Gamemaster Audio - Pro Sound Collection v1.3 - 16bit 48k\\Animals_Nature_Ambiences\\swamp_ambience_frogs_03_loop.wav", true);
	}

	public Int2d getLevelPos() {
		return levelPos;
	}

	public void loadSubLevel(String levelToLoad, Player player) {
		loadLevel(levelToLoad);
		playerPositionInSuperLevel = player.position.clone();
	}

	public void resizeLevel(int newWidth, int newHeight) {
		bottom_layer.resize(newWidth, newHeight);
		top_layer.resize(newWidth, newHeight);
	}

}
