package com.aquarius.rpg1;

import java.util.HashSet;

public class ConnectionGraphNode {
	// Each node has 8 connections with each a list of possible tileIndices
	int index;
	HashSet<Integer>[][] connections;
	public ConnectionGraphNode(int index) {
		super();
		this.index = index;
		connections = new HashSet[3][3];
		for(int x=0;x<3;x++)
			for(int y=0;y<3;y++)
				connections[x][y]= new HashSet<>();
	}
	
	public void addConnectionWithoutXYCheck(int x, int y, int index) {
		connections[x][y].add(index);
	}
	public HashSet<Integer> getConnectionWithoutXYCheck(int x, int y) {
		return connections[x][y];
	}
	
}
