package com.aquarius.rpg1.behavior.hateno;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldTime;
import com.aquarius.rpg1.behavior.HopRandomlyAction;
import com.aquarius.rpg1.objects.GameObject;

public class HoppingCharacter extends GameObject {
	private static final long serialVersionUID = -8441410676271321272L;
	public HoppingCharacter(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("HoppingCharacter", objectDrawer, position, direction);
	}
	@Override
	public void think(Player player, WorldTime worldState, LevelState levelState){
		if(getAction() == null){
			setAction(new HopRandomlyAction(this, worldState, 0));
		}
	}
	@Override
	protected void init() {
		super.init();
		damage = 1;
	}

}
