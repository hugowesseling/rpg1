package com.aquarius.rpg1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class ConnectionGraph {
	HashMap<Integer, ConnectionGraphNode> graph;

	public ConnectionGraph() {
		graph = new HashMap<>();
	} 
	protected void addLayerConnections(Layer layer) {
		for(int x=0;x<layer.getWidth(); x++) {
			for(int y=0;y<layer.getHeight(); y++) {
				int index = layer.getTile(x, y);
				ConnectionGraphNode node;
				if(graph.containsKey(index)) {
					node = graph.get(index); 
				}else {
					node = new ConnectionGraphNode(index);
					graph.put(index, node);
				}
				for(int xd = -1; xd<2; xd++) {
					int xs = x+xd;
					if(xs>=0 && xs<layer.getWidth()) {
						for(int yd= -1; yd<2; yd++) {
							int ys = y+yd;
							if(ys>=0 && ys<layer.getHeight()) {
								int index2 = layer.getTile(xs, ys);
								node.addConnectionWithoutXYCheck(xd+1, yd+1, index2);
							}
						}
					}
				}
			}
		}
			
		
	}
	public int calculateFitScore(int index, Layer layer, int tileX, int tileY) {
		if(!graph.containsKey(index))
			return -1;
		int totalScore = 0;
		ConnectionGraphNode node = graph.get(index);
		for(int xd = -1; xd<2; xd++) {
			int xs = tileX + xd;
			if(xs>=0 && xs<layer.getWidth()) {
				for(int yd= -1; yd<2; yd++) {
					int ys = tileY + yd;
					if(!(xd == 0 && yd == 0) && ys>=0 && ys<layer.getHeight()) {
						int index2 = layer.getTile(xs, ys);
						HashSet<Integer> connections = node.getConnectionWithoutXYCheck(xd+1, yd+1);
						if(connections.contains(index2)) {
							if(xd==0 || yd==0)
								totalScore+=2;
							else
								totalScore++;
						}
					}
				}
			}
		}
		return totalScore;
	}

	public int choosebestFittingIndex(Iterable<Integer> indexList,Layer layer, int tileX, int tileY) {
		int maxFitScore = Integer.MIN_VALUE;
		ArrayList<Integer> maxFitScoreIndices = new ArrayList<>();
		for(int index:indexList) {
			int fitScore = calculateFitScore(index, layer, tileX, tileY);
			System.out.println("Index: " + index + ", fit score: " + fitScore);
			if(fitScore>maxFitScore){
				maxFitScore = fitScore;
				maxFitScoreIndices = new ArrayList<>();
			}
			if(fitScore==maxFitScore) {
				maxFitScoreIndices.add(index);
			}
		}
		return maxFitScoreIndices.get(new Random().nextInt(maxFitScoreIndices.size()));
	}

	public int choosebestFittingIndex(Layer layer, int tileX, int tileY) {
		return choosebestFittingIndex(graph.keySet(), layer, tileX, tileY);
	}

}
