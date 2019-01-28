package com.aquarius.rpg1.behavior.hateno;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldTime;
import com.aquarius.rpg1.behavior.RunRandomlyAction;
import com.aquarius.rpg1.behavior.RunUntilWallAction;
import com.aquarius.rpg1.objects.GameObject;

public class StraightLineRunCharacter extends GameObject
{
	private static final long serialVersionUID = -6977692012225104765L;

	public StraightLineRunCharacter(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("StraightLineRunCharacter", objectDrawer, position, direction);
	}

	@Override
	public void think(Player player, WorldTime worldState, LevelState levelState)
	{
		if(!(getAction() instanceof RunUntilWallAction)){
			if(player.getPosition().isOnStraightLine(position, 16)) {
				//Check if the straight path is not blocked
				int x = position.getXTile(), y = position.getYTile();
				int endx = player.getPosition().getXTile(), endy = player.getPosition().getYTile();
				Direction directionToPlayer = position.getDirectionTo(player.getPosition());
				boolean blocked = false;
				while((x != endx || directionToPlayer.movement.x == 0) && 
					  (y != endy || directionToPlayer.movement.y == 0))
				{
					if(levelState.collidesTilePosition(x, y)) {
						blocked = true;
						break;
					}
					x+=directionToPlayer.movement.x;
					y+=directionToPlayer.movement.y;
				}
				if(!blocked) {
					setFrameDivider(FRAME_DIVIDER_DEFAULT / 4);
					setDirection(directionToPlayer );
					setAction(new RunUntilWallAction(this, 3));
					System.out.println("StraightLineRunCharacter.think: Run!");
					return;
				}
			}
		}
		if(getAction() == null)	{
			setDirection(Direction.SOUTH);
			setFrameDivider(FRAME_DIVIDER_DEFAULT);
			setAction(new RunRandomlyAction(this, worldState, 0, 1));
			//System.out.println("StraightLineRunCharacter.think: RunRandomlyAction");
		}
	}

	@Override
	protected void init() {
		super.init();
		damage = 2;
	}
	
}
