package com.aquarius.rpg1.behavior.hateno;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.behavior.RunRandomlyAction;
import com.aquarius.rpg1.behavior.RunUntilWallAction;

public class StraightLineRunCharacter extends GameObject
{
	private static final long serialVersionUID = -6977692012225104765L;

	public StraightLineRunCharacter(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("StraightLineRunCharacter", objectDrawer, position, direction);
	}

	@Override
	public void think(Player player, WorldState worldState, LevelState levelState)
	{
		if(!(getAction() instanceof RunUntilWallAction)){
			if(player.getPosition().isOnStraightLine(position, 16)) {
				setFrameDivider(FRAME_DIVIDER_DEFAULT / 4);
				setDirection(position.getDirectionTo(player.getPosition()));
				setAction(new RunUntilWallAction(this, 4));
				System.out.println("StraightLineRunCharacter.think: Run!");
				return;
			}
		}
		if(getAction() == null)	{
			setDirection(Direction.SOUTH);
			setFrameDivider(FRAME_DIVIDER_DEFAULT);
			setAction(new RunRandomlyAction(this, worldState, -1, 1));
			System.out.println("StraightLineRunCharacter.think: RunRandomlyAction");
		}
	}
}
