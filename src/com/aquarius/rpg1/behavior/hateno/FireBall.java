package com.aquarius.rpg1.behavior.hateno;

import java.awt.Graphics2D;

import com.aquarius.rpg1.AnimationDrawer;
import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.Resources;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.behavior.FlyAction;

public class FireBall extends GameObject {
	public FireBall(ObjectPosition position, Int2d movement) {
		super("fireball", new AnimationDrawer(Resources.crystalBallTileSet), position, Direction.NORTH);
		setAction(new FlyAction(this, movement));
	}

	private static final long serialVersionUID = -8813248849940680183L;

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
