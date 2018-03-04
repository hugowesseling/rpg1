package com.aquarius.rpg1;

import java.util.ArrayList;
import java.util.HashMap;
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
}
