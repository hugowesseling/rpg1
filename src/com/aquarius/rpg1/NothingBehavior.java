package com.aquarius.rpg1;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.aquarius.rpg1.behavior.CharacterBehavior;

public class NothingBehavior implements CharacterBehavior {

	@Override
	public void think(Player player, WorldState worldState, LevelState levelState) {
	}

	@Override
	public void writeSaveState(FileOutputStream fileOutputStream) {
	}

	@Override
	public void readSaveState(FileInputStream fileInputStream) {
	}

}
