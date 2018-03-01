package com.aquarius.rpg1.behavior;

import com.aquarius.rpg1.WorldState;

public interface CharacterAction {
	// Perform movements and whatever based on the action
	// If the action is done, return true
	public boolean doAction(WorldState worldState);
}
