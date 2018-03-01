package com.aquarius.rpg1;

public class WorldState {
	public int timeOfDay;
	public long timeMs;
	

	public WorldState() {
		timeMs = 0;
		timeOfDay = 0;
	}
	public void setTimeMs(long timeMs)
	{
		this.timeMs = timeMs;
	}


	public long getTimeMs() {
		return timeMs;
	}
}
