package com.aquarius.rpg1.behavior;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.Resources;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.drawers.RotatingDrawer;
import com.aquarius.rpg1.objects.GameObject;

public class BouncingRock extends GameObject {
	private static final long serialVersionUID = 8935352169165901619L;
	public BouncingRock(ObjectPosition position, Int2d movement) {
		super("BouncingRock", new RotatingDrawer(Resources.ItemTileLocation.ROCK.index), position, Direction.NORTH);
		setAction(new BounceAction(this, movement));
	}

	@Override
	public void think(Player player, WorldState worldState, LevelState levelState) {
		// End when hitting a wall
		if(getAction() == null)
			health = 0;
	}


	@Override
	protected void init() {
		super.init();
		damage = 2;
		health = 40;
		frameDivider = 1;
	}

	
}
