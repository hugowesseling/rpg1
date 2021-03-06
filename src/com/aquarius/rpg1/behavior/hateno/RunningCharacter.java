package com.aquarius.rpg1.behavior.hateno;

import java.util.HashSet;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldTime;
import com.aquarius.rpg1.behavior.RunAction;
import com.aquarius.rpg1.objects.GameObject;

public class RunningCharacter extends GameObject {
	private static final long serialVersionUID = -1952527416412810356L;

	public RunningCharacter(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("RunningCharacter", objectDrawer, position, direction);
	}
	@Override
	protected void init() {
		interactionPossibilities = new HashSet<>();
	}
	@Override
	public void think(Player player, WorldTime worldState, LevelState levelState){
		if(getAction() == null){
			setAction(new RunAction(this));
		}
	}

}
