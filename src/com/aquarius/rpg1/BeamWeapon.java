package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

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
		AudioSystemPlayer.playRandomSwish();
		slashCounter  = 0;
	}

	@Override
	public void draw(Graphics2D graphics, int frameCounter, int screenx, int screeny) {
		// TODO Auto-generated method stub
		if(slashCounter < 8) {
			Image swordimage = Resources.weaponTileSet.tiles[0][0];
			AffineTransform oldTransform = graphics.getTransform();
			graphics.translate(user.position.x - screenx + user.getDirection().movement.x*2, user.position.y - screeny - 7 + user.getDirection().movement.y*4);
			graphics.rotate(Math.toRadians(slashCounter * 20 + user.getDirection().degrees));
			graphics.drawImage(swordimage, -15, -15, null);
			graphics.setTransform(oldTransform);

			slashCounter += 1;
		}
	}
	
	@Override
	public void think(WorldState worldState, LevelState levelState) {
		//System.out.println("Sword.think()");
		if(slashCounter < 3) {
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
							for(int i=0;i<10;i++)
								character.moveAndLevelCollide(levelState, vf.x * 2, vf.y * 2);
							AudioSystemPlayer.playRandomImpact();
						}
					}
				}
			}
		}
		
	}
}

