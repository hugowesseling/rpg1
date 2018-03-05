package com.aquarius.rpg1;

import java.io.Serializable;

import com.aquarius.rpg1.behavior.CharacterBehavior;

public class NothingBehavior implements CharacterBehavior, Serializable {

	private static final long serialVersionUID = -5346378134317592044L;

	@Override
	public void think(Player player, WorldState worldState, LevelState levelState) {
	}
}
