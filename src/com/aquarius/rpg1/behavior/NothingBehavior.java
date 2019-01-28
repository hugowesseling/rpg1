package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldTime;

public class NothingBehavior implements CharacterBehavior, Serializable {

	private static final long serialVersionUID = -5346378134317592044L;

	@Override
	public void think(Player player, WorldTime worldState, LevelState levelState) {
	}
}
