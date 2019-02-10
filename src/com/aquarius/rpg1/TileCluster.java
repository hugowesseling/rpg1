package com.aquarius.rpg1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

public class TileCluster {
	ArrayList<Integer> tileIndices;

	public Color color;
	
	public TileCluster() {
		tileIndices = new ArrayList<>();
		color = new Color((int)(Math.random() * 0x1000000));
	}

	public boolean isInList(int tileIndex)
	{
		for(Integer searchIndex:tileIndices) {
			if(tileIndex == searchIndex)
				return true;
		}
		return false;
	}

	public void addIndex(int index) {
		tileIndices.add(index);
		
	}

	public int choosebestFittingIndex(ConnectionGraph connectionGraph, Layer layer, int tileX, int tileY) {
		return connectionGraph.choosebestFittingIndex(tileIndices, layer, tileX, tileY);
	}
}
