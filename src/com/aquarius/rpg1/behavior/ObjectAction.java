package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldState;

public interface ObjectAction extends Serializable{
	// Perform movements and whatever based on the action
	// If the action is done, return true
	public boolean doActionAndCheckIfDone(WorldState worldState, LevelState levelState);
}
