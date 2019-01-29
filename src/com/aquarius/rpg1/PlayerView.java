package com.aquarius.rpg1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.aquarius.rpg1.AudioSystemPlayer.RandomSound;
import com.aquarius.rpg1.objects.GameObject;
import com.aquarius.rpg1.objects.StorableObjectType;
import com.aquarius.rpg1.weapons.BeamWeapon;
import com.aquarius.rpg1.weapons.Hammer;

public class PlayerView extends LevelView{

	/*
	 * Contains everything needed for viewing a level from a certain viewpoint
	 * This can be the player. So multiple players could be supported in multiple viewpoints
	 * 
	 * This should be hierarchical:
	 * Basic editor
	 * 		screenx, screeny
	 * 		mouseFunctions
	 *  |
	 *  Playing editor
	 *  	Player
	 *  	player actions
	 */
	private static final long serialVersionUID = -823746928911865468L;
	
	private boolean showInventory = false;
	private InventoryMenu inventoryMenu;
	private Dialogue dialogue;
	private boolean simulating = false;
	private int frameCounter = 0;
	private Player player;

	
	public PlayerView(LevelState levelState, TileSelectorFrame tileSelectorFrame) {
		super(levelState, tileSelectorFrame);

		dialogue = null;
		player = levelState.player;
		inventoryMenu = new InventoryMenu(Resources.dialogStyles.get(1));
		
		JMenuBar menuBar = frame.getJMenuBar();
		JMenu simulateMenu = new JMenu("Simulate");
		JCheckBoxMenuItem simulateMenuItem = new JCheckBoxMenuItem("Simulate");
		simulateMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				simulating = ((AbstractButton)event.getSource()).getModel().isSelected();
			}
		});
		simulateMenu.add(simulateMenuItem);
		menuBar.add(simulateMenu);

	}
	@Override
	public void keyPressed(KeyEvent ke) 
	{
		super.keyPressed(ke);
		int keyCode = ke.getKeyCode();

		if(keyCode == KeyEvent.VK_A)
		{
			System.out.println("Do action");
			if(dialogue != null) {
				if(!dialogue.confirm(levelState, player)) {
					dialogue = null;
				}
			}else {
				if(player.getInteractionGameObject() != null)
				{
					switch(player.getInteractionPossiblity()) {
					case OPEN:
						StorableObjectType storableObjectType = player.getInteractionGameObject().open();
						if(storableObjectType != null) {
							player.startItemAbovePlayerAnimation(storableObjectType);
							player.inventory.add(storableObjectType.name, storableObjectType.amount);
							AudioSystemPlayer.playSound(AudioSystemPlayer.AUDIO_FOLDER + "Collectibles_Items_Powerup\\collect_item_05.wav", false);
						}
						break;
					case UNLOCK:
						player.getInteractionGameObject().unlock(player, levelState);
						break;
					case TALK:
						startDialog(player.getInteractionGameObject());
						AudioSystemPlayer.playRandom(RandomSound.EXPRESSION);
						break;
					case TOUCH:
						break;
					default:
						break;
					
					}
				}				
			}
		}
		if(keyCode == KeyEvent.VK_X) {
			player.setWeapon(new BeamWeapon(player));
			player.useWeapon();
		}
		if(keyCode == KeyEvent.VK_H) {
			player.setWeapon(new Hammer(player));
			player.useWeapon();
		}
		if(keyCode == KeyEvent.VK_S) {
			simulating = !simulating;
		}
		if(keyCode == KeyEvent.VK_LEFT)
		{
			if(dialogue != null)
			{
				dialogue.left();
			}
		}
		if(keyCode == KeyEvent.VK_RIGHT)
		{
			if(dialogue != null)
			{
				dialogue.right();
			}
		}
		if(keyCode == KeyEvent.VK_I) {
			showInventory = !showInventory;
		}
	}


	@Override
	protected void drawLevel(Graphics2D imageGraphics, int imageWidth, int imageHeight) {
		if(simulating && dialogue==null) {
			levelState.worldTick();
		}
		setScreenPositionFromTrackingPosition(levelState.player.getPosition());

		levelState.draw(imageGraphics, imageWidth, imageHeight, screenx, screeny, frameCounter, simulating);

		if(mapSelection != null)
			mapSelection.draw(imageGraphics, screenx, screeny);
		
		if(simulating) {
			if(dialogue != null) {
				//System.out.println("Drawing dialogue");
				dialogue.draw(imageGraphics, 
						50, imageHeight-100, 
						imageWidth-100, 2 * Constant.TILE_HEIGHT);
			}else {		
				if(player.getInteractionGameObject() != null) {
					imageGraphics.setColor(Color.BLUE);
					imageGraphics.fillOval(imageWidth - 80,  10,  60, 40);
					imageGraphics.setColor(Color.WHITE);
					String actionText = player.getInteractionPossiblity().toString();
					int estimatedTextWidth = 4 * actionText.length();
					imageGraphics.drawString(actionText, imageWidth - 50 - estimatedTextWidth, 35);
				}
			}
			if(showInventory)
			{
				inventoryMenu.draw(imageGraphics,
						imageWidth/2 - 100, imageHeight/2 - 100,
						200, 200, player.inventory);
			}
			drawHUD(imageGraphics);
		}
		frameCounter++;
	}
	
	private void drawHUD(Graphics2D graphics) {
		int currentHeartPos = 20;
		for(int i=3;i<player.getHealth();i+=4) {
			graphics.drawImage(Resources.heartTileSet.tiles[0][10], currentHeartPos , 20 ,null);
			currentHeartPos+=14;
		}
		int lastHeart = player.getHealth() % 4; // = 0..3
		if(lastHeart>0)
			graphics.drawImage(Resources.heartTileSet.tiles[0][14 - lastHeart], currentHeartPos , 20 ,null);
	}

	private void startDialog(GameObject dialogueGameObject) {
		System.out.println("Starting dialogue");
		dialogue = dialogueGameObject.startDialog(player, levelState);
		
	}
	public void setScreenPositionFromTrackingPosition(ObjectPosition position)
	{
		screenx = position.x - 150;
		screeny = position.y - 150;
		Dimension size = frame.getSize();
		int imageWidth = size.width / 2;
		int imageHeight = size.height / 2;

		if(screenx<0)screenx=0;
		if(screeny<0)screeny=0;
		if(screenx > levelState.getWidth() * Constant.TILE_WIDTH - imageWidth) {
			screenx = levelState.getWidth()*Constant.TILE_WIDTH - imageWidth;
		}
		if(screeny > levelState.getHeight() * Constant.TILE_HEIGHT - imageHeight) {
			screeny = levelState.getHeight() * Constant.TILE_HEIGHT - imageHeight;
		}				
	}
	@Override
	protected void mouseCornerActions(Dimension size, int mouseX, int mouseY) {
		if(!simulating)
			super.mouseCornerActions(size, mouseX, mouseY);
	}


}
