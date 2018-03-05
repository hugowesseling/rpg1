package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import com.aquarius.rpg1.behavior.GameObjectType;

public class LevelState {
	public Layer bottom_layer, top_layer;
	public HashMap<String, String> levelKeyValues;	// Any definable level values
	public Vector<GameCharacter> allCharacters;	// All characters in this level
	
	public LevelState(Layer bottom_layer, Layer top_layer) {
		this.bottom_layer = bottom_layer;
		this.top_layer = top_layer;
		levelKeyValues = new HashMap<>();
		allCharacters = new Vector<>();
	}

	public GameCharacter findRandomCharacterInNeighborhood(GameObjectType objectType, Int2d position, int distance) {
		ArrayList<GameCharacter> eligibles = new ArrayList<>();
		for(GameCharacter character : allCharacters)
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

	public void draw(Graphics2D imageGraphics, int imageWidth, int imageHeight, int screenx, int screeny, int frameCounter) {
		bottom_layer.drawLayer(imageGraphics, imageWidth, imageHeight, screenx, screeny, false);
		top_layer.drawLayer(imageGraphics,imageWidth, imageHeight, screenx, screeny, true);
		//imageGraphics.drawImage(characterTileSet.getTileImageFromXY((frameCounter/10) % 3, charDirection), 100, 100, null);
		for(GameCharacter gameCharacter: allCharacters){
			gameCharacter.draw(imageGraphics, frameCounter, screenx, screeny);
		}
			}

	public void doActions(WorldState worldState) {
		// Do actions and thinking
		for(GameCharacter gameCharacter: allCharacters) {
			gameCharacter.doAction(worldState);
			gameCharacter.doMovement();
		}
	}

	public void think(Player player, WorldState worldState, LevelState levelState) {
		for(GameCharacter gameCharacter: levelState.allCharacters) {
			gameCharacter.think(player, worldState, levelState);
		}
	}

	public void writeToFileOutputStream(FileOutputStream fileOutputStream) {
		bottom_layer.writeToFileOutputStream(fileOutputStream);
		top_layer.writeToFileOutputStream(fileOutputStream);
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new  ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(allCharacters);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void readFromFileInputStream(FileInputStream fileInputStream) {
		bottom_layer.readFromFileInputStream(fileInputStream);
		top_layer.readFromFileInputStream(fileInputStream);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(fileInputStream);
			allCharacters = (Vector<GameCharacter>) ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
