package com.aquarius.rpg1;

public class WorldTime {
	public int timeOfDay;
	public long timeMs;
	

	public WorldTime() {
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
