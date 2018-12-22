package com.aquarius.rpg1.weapons;

import java.awt.Graphics2D;
import java.io.Serializable;

import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldState;

public abstract class Weapon implements Serializable{
	private static final long serialVersionUID = -3685739245093632795L;

	abstract public void draw(Graphics2D graphics, int frameCounter, int screenx, int screeny);

	abstract public void startUse();

	abstract public void think(WorldState worldState, LevelState levelState);
}
