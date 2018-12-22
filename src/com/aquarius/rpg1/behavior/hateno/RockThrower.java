package com.aquarius.rpg1.behavior.hateno;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.behavior.ThrowRocksRandomlyAction;
import com.aquarius.rpg1.objects.GameObject;

public class RockThrower extends GameObject {
	private static final long serialVersionUID = 3488046662343188031L;
	public RockThrower(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("RockThrower", objectDrawer, position, direction);
	}
	@Override
	public void think(Player player, WorldState worldState, LevelState levelState){
		if(getAction() == null){
			setAction(new ThrowRocksRandomlyAction(this));
		}
	}
	@Override
	protected void init() {
		super.init();
		damage = 1;
	}

}
