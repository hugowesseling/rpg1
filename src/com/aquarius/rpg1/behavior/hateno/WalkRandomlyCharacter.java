package com.aquarius.rpg1.behavior.hateno;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldTime;
import com.aquarius.rpg1.behavior.RunRandomlyAction;
import com.aquarius.rpg1.objects.GameObject;

public class WalkRandomlyCharacter extends GameObject{
	private static final long serialVersionUID = 4325930941936458411L;

	public WalkRandomlyCharacter(ObjectDrawer objectDrawer, ObjectPosition position) {
		super("WalkRandomlyCharacter", objectDrawer, position, Direction.SOUTH);
	}

	@Override
	public void think(Player player, WorldTime worldState, LevelState levelState)
	{
		if(getAction() == null)	{
			setAction(new RunRandomlyAction(this, worldState, 5000, 1));
		}
	}
}
