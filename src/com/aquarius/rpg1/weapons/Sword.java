package com.aquarius.rpg1.weapons;

import java.awt.Graphics2D;
import java.awt.Image;

import com.aquarius.rpg1.AudioSystemPlayer;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.Resources;
import com.aquarius.rpg1.WorldTime;
import com.aquarius.rpg1.AudioSystemPlayer.RandomSound;
import com.aquarius.rpg1.objects.GameObject;

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
			Image swordimage = Resources.swordAttack.getTileImageFromXYSafe(
					swordSlashCounter, 
					SWORD_DIRECTION_ORDER[user.getDirection().tileOffset % SWORD_DIRECTION_ORDER.length]);
			graphics.drawImage(swordimage, user.getPosition().x - screenx - 19, user.getPosition().y - screeny - 15, null);
			if(frameCounter % 3 == 0) {
				swordSlashCounter += 1;
			}
		}
	}
	
	@Override
	public void think(WorldTime worldState, LevelState levelState) {
		//System.out.println("Sword.think()");
		if(swordSlashCounter < 3) {
			//Check for collisions in 90 degrees forward quarter circle:
			//rel position:rp  ,forward vector:vf , side vector: vs
			//hypot(rp)^2<radius^2 && 
			//frontal 45 degrees: inprod(vf + vs,rp)>0 && inprod(vf - vs, rp)>0
			//just front: inprod(vf, rp) > 0
			for(GameObject character: levelState.allGameObjects) {
				if(character != user) {
					int rx = character.getPosition().x - user.getPosition().x, ry = character.getPosition().y - user.getPosition().y;
					Int2d vf = user.getDirection().movement;
					//Int2d vs = vf.rotate90();
					if(rx *rx + ry *ry < radius*radius) {
						System.out.println("Character " + character.getName() + " in range");
						if( vf.x  * rx + vf.y *ry > 0) {
							System.out.println("Hitting character " + character.getName());
							for(int i=0;i<10;i++)
								character.moveAndLevelCollide(levelState, vf.x * 2, vf.y * 2);
							AudioSystemPlayer.playRandom(RandomSound.FLESH_IMPACT);
						}
					}
				}
			}
		}
		
	}
}

