package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;

public class Sword extends Weapon {

	private GameObject user;
	private int swordSlashCounter = 1000;
	private final static int radius = 32;


	public Sword(GameObject user) {
		super();
		this.user = user;
	}

	@Override
	public void startUse() {
		swordSlashCounter  = 0;
	}

	@Override
	public void draw(Graphics2D graphics, int frameCounter, int screenx, int screeny) {
		// TODO Auto-generated method stub
		if(swordSlashCounter < 3) {
			int[] SWORD_DIRECTION_ORDER = {0,1,3,2};
			Image swordimage = Resources.swordAttack.getTileImageFromXY(
					swordSlashCounter, 
					SWORD_DIRECTION_ORDER[user.getDirection().tileOffset % SWORD_DIRECTION_ORDER.length]);
			graphics.drawImage(swordimage, user.position.x - screenx - 19, user.position.y - screeny - 15, null);
			if(frameCounter % 3 == 0) {
				swordSlashCounter += 1;
			}
		}
	}
	
	@Override
	public void think(WorldState worldState, LevelState levelState) {
		//System.out.println("Sword.think()");
		if(swordSlashCounter < 3) {
			//Check for collisions in 90 degrees forward quarter circle:
			//rel position:rp  ,forward vector:vf , side vector: vs
			//hypot(rp)^2<radius^2 && 
			//frontal 45 degrees: inprod(vf + vs,rp)>0 && inprod(vf - vs, rp)>0
			//just front: inprod(vf, rp) > 0
			for(GameObject character: levelState.allGameObjects) {
				if(character != user) {
					int rx = character.position.x - user.position.x, ry = character.position.y - user.position.y;
					Int2d vf = user.getDirection().movement;
					//Int2d vs = vf.rotate90();
					if(rx *rx + ry *ry < radius*radius) {
						System.out.println("Character " + character.name + " in range");
						if( vf.x  * rx + vf.y *ry > 0) {
							System.out.println("Hitting character " + character.name);
							character.position.x += vf.x * 20;	//bump away
							character.position.y += vf.y * 20;
						}
					}
				}
			}
		}
		
	}
}

