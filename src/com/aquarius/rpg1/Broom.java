package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import com.aquarius.rpg1.AudioSystemPlayer.RandomSound;

public class Broom extends Weapon {

	private static final long serialVersionUID = -1396688434692942887L;
	private GameObject user;
	private int swishCounter = 1000;
	private final static int radius = 32;


	public Broom(GameObject user) {
		super();
		this.user = user;
	}

	@Override
	public void startUse() {
		AudioSystemPlayer.playRandom(RandomSound.SWISH);
		swishCounter  = 0;
	}

	@Override
	public void draw(Graphics2D graphics, int frameCounter, int screenx, int screeny) {
		Image broomImage = Resources.itemTileSet.getTileImageFromPositionUnsafe(Resources.ItemTileLocation.BROOM);
		AffineTransform oldTransform = graphics.getTransform();
		graphics.translate(user.position.x - screenx + user.getDirection().movement.x*2, user.position.y - screeny - 7 + user.getDirection().movement.y*4);
		graphics.rotate(Math.toRadians(Math.cos(swishCounter*Math.PI*0.1)*22.5f + user.getDirection().degrees + 212.5f));
		graphics.drawImage(broomImage, 0, 0, null);
		graphics.setTransform(oldTransform);

		if(swishCounter < 20) {
			swishCounter += 1;
		}
	}
	
	@Override
	public void think(WorldState worldState, LevelState levelState) {
		//System.out.println("Broom.think()");
		// Check for dust on top_layer, if found, remove and get coins or something
		/*if(slashCounter < 3) {
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
							character.getDamage(levelState, 5);
						}
					}
				}
			}
		}*/
		
	}
}

