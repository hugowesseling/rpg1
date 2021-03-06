package com.aquarius.rpg1.weapons;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import com.aquarius.rpg1.AudioSystemPlayer;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.Resources;
import com.aquarius.rpg1.WorldTime;
import com.aquarius.rpg1.AudioSystemPlayer.RandomSound;
import com.aquarius.rpg1.Resources.ItemTileLocation;
import com.aquarius.rpg1.objects.GameObject;

public class BeamWeapon extends Weapon {

	private GameObject user;
	private int slashCounter = 1000;
	private final static int radius = 32;


	public BeamWeapon(GameObject user) {
		super();
		this.user = user;
	}

	@Override
	public void startUse() {
		AudioSystemPlayer.playRandom(RandomSound.SWISH);
		slashCounter  = 0;
	}

	@Override
	public void draw(Graphics2D graphics, int frameCounter, int screenx, int screeny) {
		// TODO Auto-generated method stub
		if(slashCounter < 8) {
			Image swordimage = Resources.itemTileSet.getTileImageFromPositionUnsafe(Resources.ItemTileLocation.SWORD);
			AffineTransform oldTransform = graphics.getTransform();
			graphics.translate(user.getPosition().x - screenx + user.getDirection().movement.x*2, user.getPosition().y - screeny - 7 + user.getDirection().movement.y*4);
			graphics.rotate(Math.toRadians(slashCounter * 20 + user.getDirection().degrees));
			graphics.drawImage(swordimage, -15, -15, null);
			graphics.setTransform(oldTransform);

			slashCounter += 1;
		}
	}
	
	@Override
	public void think(WorldTime worldState, LevelState levelState) {
		//System.out.println("Sword.think()");
		if(slashCounter < 3) {
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
							character.doDamage(levelState, 5);
						}
					}
				}
			}
		}
		
	}
}

