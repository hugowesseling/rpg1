package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldTime;

public class WaitAction implements ObjectAction {
	private static final long serialVersionUID = -5127950924665960036L;
	private long startTime;
	private long standingaroundDuration;

	public WaitAction(WorldTime worldState, long standingaroundDuration) {
		startTime = worldState.getTimeMs();
		this.standingaroundDuration = standingaroundDuration;
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldTime worldState, LevelState levelState) {
		return worldState.getTimeMs() > startTime + standingaroundDuration;
	}
}
