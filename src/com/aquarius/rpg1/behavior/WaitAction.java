package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.WorldState;

public class WaitAction implements CharacterAction, Serializable {
	private static final long serialVersionUID = -5127950924665960036L;
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
