package com.aquarius.rpg1.behavior;

import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldTime;

public interface CharacterBehavior {
	public void think(Player player, WorldTime worldState, LevelState levelState);
	//public void writeSaveState(FileOutputStream fileOutputStream);
	//public void readSaveState(FileInputStream fileInputStream);
}
