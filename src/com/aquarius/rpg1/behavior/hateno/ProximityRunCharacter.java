package com.aquarius.rpg1.behavior.hateno;

import com.aquarius.rpg1.AudioSystemPlayer;
import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.behavior.RunRandomlyAction;
import com.aquarius.rpg1.behavior.WaitAction;

public class ProximityRunCharacter extends GameObject
{
	private static final long serialVersionUID = -3565606964531244825L;
	private final static int STANDINGAROUND_DURATION = 10000;

	public ProximityRunCharacter(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("ProximityRunCharacter", objectDrawer, position, direction);
	}

	@Override
	public void think(Player player, WorldState worldState, LevelState levelState)
	{
		if(!(getAction() instanceof RunRandomlyAction)){
			if(player.getPosition().isNearby(position, 70)) {
				AudioSystemPlayer.playRandomChicken();
				setFrameDivider(FRAME_DIVIDER_DEFAULT / 4);
				setAction(new RunRandomlyAction(this, worldState, 5000, 3));
				System.out.println("ProximityRunCharacter.think: Run!");
				return;
			}
		}
		if(getAction() == null)	{
			setDirection(Direction.SOUTH);
			setFrameDivider(FRAME_DIVIDER_DEFAULT * 2);
			setAction(new WaitAction(worldState, STANDINGAROUND_DURATION));
			System.out.println("ProximityRunCharacter.think: WaitAction");
		}
	}
}
