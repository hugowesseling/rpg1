package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import com.aquarius.rpg1.AudioSystemPlayer.RandomSound;

public class Hammer extends Weapon {

	private GameObject user;
	private int slashCounter = 1000;


	public Hammer(GameObject user) {
		super();
		this.user = user;
	}

	@Override
	public void startUse() {
		AudioSystemPlayer.playRandom(RandomSound.WHOOSH);
		slashCounter  = 0;
	}

	@Override
	public void draw(Graphics2D graphics, int frameCounter, int screenx, int screeny) {
		// TODO Auto-generated method stub
		if(slashCounter < 16) {
			Image hammerimage = Resources.itemTileSet.getTileImageFromPositionUnsafe(Resources.ItemTileLocation.HAMMER);
			AffineTransform oldTransform = graphics.getTransform();
			graphics.translate(user.position.x - screenx + user.getDirection().movement.x*2, user.position.y - screeny - 7 + user.getDirection().movement.y*4);
			graphics.rotate(Math.toRadians(slashCounter * 10 + user.getDirection().degrees));
			graphics.drawImage(hammerimage, -15, -15, null);
			graphics.setTransform(oldTransform);
		}
	}
	
	@Override
	public void think(WorldState worldState, LevelState levelState) {
		if(slashCounter <16)
			slashCounter += 1;
		if(slashCounter == 6) {
			
			int tileX = ObjectPosition.getXTileFromX(user.position.x + user.direction.movement.x*16);
			int tileY = ObjectPosition.getYTileFromY(user.position.y + user.direction.movement.y*16);
			int tileIndex = levelState.getTopCollidingTileForXYChecked(tileX, tileY);
			System.out.println("Hammer hits " + tileIndex);
			if(tileIndex != -1){
				//Check if there is a DoorWayObject one tile above (x,y-1)
				boolean foundDoorway = false;
				for(GameObject gameObject: levelState.allGameObjects) {
					if(gameObject instanceof DoorwayObject) {
						if(gameObject.position.getXTile() == tileX && gameObject.position.getYTile() == tileY - 1) {
							levelState.replaceTopCollidingTileForXYChecked(tileX, tileY, 68867);
							levelState.replaceTopCollidingTileForXYChecked(tileX, tileY - 1, 68611);
							AudioSystemPlayer.playRandom(RandomSound.ROCK_HEAVY_SLAM);
							AudioSystemPlayer.playRandom(RandomSound.ROCK_BREAK);
							foundDoorway = true;
						}
					}
				}
				if(!foundDoorway)
					AudioSystemPlayer.playRandom(RandomSound.ROCK_IMPACT);
			}

		}
		
	}
}

