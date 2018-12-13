package com.aquarius.rpg1.behavior.hateno;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.behavior.RunRandomlyAction;
import com.aquarius.rpg1.behavior.WalkToPositionAction;
import com.aquarius.rpg1.behavior.WalkToTilePositionAction;

public class HidingCharacter extends GameObject
{
	private static final long serialVersionUID = 5830432651489429585L;
	private long waitUntil;
	private int counter;

	public HidingCharacter(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("HidingCharacter", objectDrawer, position, direction);
		waitUntil = 0;
		counter = 0;
	}

	@Override
	public void think(Player player, WorldState worldState, LevelState levelState)
	{
		if(getAction() == null) {
			System.out.println("HidingCharacter: Choosing new action");
			if(player.getPosition().distanceTo(this.position) < 100) {
				// Hide behind higher layer object
				Int2d hidingPosition = levelState.findHidingPlace(position, 32);
				if(hidingPosition != null) {
					System.out.println("HidingCharacter: Hiding to " + hidingPosition);
					setAction(new WalkToTilePositionAction(this, worldState, hidingPosition, 5000));
				}
			}else {
				// Look around a bit and sometimes walk a bit
				counter ++;
				if(counter > 4) {
					setDirection(Direction.random());
					counter = 0;
				}
				if(worldState.timeMs > waitUntil){
					System.out.println("HidingCharacter: Running randomly");
					// Walk in current direction
					waitUntil = worldState.timeMs + 5000; // Wait 5 seconds
					setAction(new RunRandomlyAction(this, worldState, 500, 2));
				}
			}
		}

	}
}
