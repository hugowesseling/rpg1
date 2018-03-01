package com.aquarius.rpg1.behavior;

import com.aquarius.rpg1.WorldState;

public class WaitAction implements CharacterAction {

	private long startTime;
	private long standingaroundDuration;

	public WaitAction(WorldState worldState, long standingaroundDuration) {
		startTime = worldState.getTimeMs();
		this.standingaroundDuration = standingaroundDuration;
	}

	@Override
	public boolean doAction(WorldState worldState) {
		return worldState.getTimeMs() > startTime + standingaroundDuration;
	}
}
