package com.aquarius.rpg1.behavior;

import java.awt.Graphics2D;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.Resources;
import com.aquarius.rpg1.WorldTime;
import com.aquarius.rpg1.drawers.AnimationDrawer;
import com.aquarius.rpg1.objects.GameObject;

public class FireBall extends GameObject {
	public FireBall(ObjectPosition position, Int2d movement) {
		super("fireball", new AnimationDrawer(Resources.crystalBallTileSet), position, Direction.NORTH);
		setAction(new FlyAction(this, movement));
	}

	private static final long serialVersionUID = -8813248849940680183L;

	@Override
	public void think(Player player, WorldTime worldState, LevelState levelState) {
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
