package com.aquarius.rpg1.behavior;

import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldState;

public interface CharacterBehavior {
	public void think(Player player, WorldState worldState, LevelState levelState);
	//public void writeSaveState(FileOutputStream fileOutputStream);
	//public void readSaveState(FileInputStream fileInputStream);
}
