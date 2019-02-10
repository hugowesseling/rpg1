package com.aquarius.rpg1;

import java.util.ArrayList;
import java.util.HashMap;

public class EditConfiguration {

	public ArrayList<TileCluster> tileClusters;
	public TilePattern selectedTilePattern = null;
	public TileCluster selectedTileCluster = null;

	ConnectionGraph connectionGraph;
	
	public EditConfiguration() {
		tileClusters = new ArrayList<>();
		connectionGraph = new ConnectionGraph();
	}

	public void clearSelectedTilePattern() {
		selectedTilePattern = null;
	}

	public TilePattern getSelectedTilePattern() {
		return selectedTilePattern;
	}

	public void setSelectedTilePattern(TilePattern tilePattern) {
		selectedTilePattern = tilePattern;
		clearSelectedTileCluster();
		System.out.println("Tile pattern is selected");
	}

	public void clearSelectedTileCluster() {
		selectedTileCluster = null;
	}

	public void addIndexToTileCluster(int index) {
		if(selectedTileCluster != null) {
			selectedTileCluster.addIndex(index);
		} else {
			selectedTileCluster  = findClusterForTileIndex(tileClusters, index);
			if(selectedTileCluster == null){
				//Create new cluster
				selectedTileCluster = new TileCluster();
				tileClusters.add(selectedTileCluster);
				selectedTileCluster.addIndex(index);
			}
			System.out.println("Tile cluster is selected");
			clearSelectedTilePattern();
		}
	}

	private static TileCluster findClusterForTileIndex(ArrayList<TileCluster> tileClusters, int index) {
		for(TileCluster tileCluster:tileClusters) {
			if(tileCluster.isInList(index)) {
				//System.out.println("Found cluster for index " + index + " in " + tileClusters.size() + " clusters");
				return tileCluster;
			}
		}
		//System.out.println("Did not found cluster for index " + index + " in " + tileClusters.size() + " clusters");
		return null;
	}

	public ArrayList<TileCluster> getTileClusters() {
		return tileClusters;
	}

	public TileCluster getSelectedTileCluster() {
		return selectedTileCluster;
	}
	
	public TileCluster findClusterForTileIndex(int index) {
		TileCluster foundTileCluster = null;
		if(selectedTileCluster == null){
			foundTileCluster = findClusterForTileIndex(tileClusters, index);
		} else{
			if(selectedTileCluster.isInList(index))
				foundTileCluster = selectedTileCluster;
		}
		return foundTileCluster;
	}

	public void placeSelectedTileCluster1(Layer layer, int tileX, int tileY) {
		// Find best cluster to place
		int tileIndex = selectedTileCluster.choosebestFittingIndex(connectionGraph, layer, tileX, tileY);
		layer.setTileIndexForCheckedXY(tileX, tileY, tileIndex);
	}

	public void placeSelectedTileCluster3(Layer layer, int tileX, int tileY) {
		placeSelectedTileCluster1(layer, tileX, tileY);
		
		for(int xd = -1; xd<2; xd++) {
			int xs = tileX + xd;
			if(xs>=0 && xs<layer.getWidth()) {
				for(int yd= -1; yd<2; yd++) {
					int ys = tileY + yd;
					if(!(xd == 0 && yd == 0) && ys>=0 && ys<layer.getHeight()) {
						int tileIndex = connectionGraph.choosebestFittingIndex(layer, xs, ys);
						layer.setTileIndexForCheckedXY(xs, ys, tileIndex);
					}
				}
			}
		}
	}

}
