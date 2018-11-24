package com.aquarius.rpg1;

import java.awt.Graphics2D;

public abstract class Weapon {

	abstract public void draw(Graphics2D graphics, int frameCounter, int screenx, int screeny);

	abstract public void startUse();

	abstract public void think(WorldState worldState, LevelState levelState);
}
