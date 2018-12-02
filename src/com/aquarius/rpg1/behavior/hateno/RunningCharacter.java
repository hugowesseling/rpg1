package com.aquarius.rpg1.behavior.hateno;

import java.util.HashSet;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.behavior.RunAction;

public class RunningCharacter extends GameObject {

	private static final long serialVersionUID = -8441410676271321272L;

	public RunningCharacter(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("RunningCharacter", objectDrawer, position, direction);
	}
	@Override
	protected void init() {
		interactionPossibilities = new HashSet<>();
	}
	@Override
	public void think(Player player, WorldState worldState, LevelState levelState){
		if(getAction() == null){
			setAction(new RunAction(this));
		}
	}

}
