package com.aquarius.rpg1.behavior.hateno;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.HashSet;

import com.aquarius.rpg1.Broom;
import com.aquarius.rpg1.CharacterDrawer;
import com.aquarius.rpg1.Dialogue;
import com.aquarius.rpg1.DialogueBlock;
import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.InteractionPossibility;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.behavior.RunRandomlyAction;

public class SweepingCharacter extends GameObject {
	private static final long serialVersionUID = -1806278695573340379L;
	private transient Dialogue sweepingDialogue;
	private int sweepCounter = 0;

	public SweepingCharacter(CharacterDrawer characterDrawer, ObjectPosition objectPosition, Direction direction) {
		super("SweepingCharacter", characterDrawer, objectPosition, direction);
		weapon = new Broom(this);
	}

	@Override
	protected void init() {
		interactionPossibilities = new HashSet<>(Arrays.asList(InteractionPossibility.TALK));
		
		DialogueBlock dialogueBlock = new DialogueBlock("Hello there!");
		dialogueBlock.add(new DialogueBlock("Would you like to do some sweeping?"))
			.addAnswer("Yes!", new DialogueBlock("That's amazing!"))
			.addAnswer("No thank you.", new DialogueBlock("Maybe another time then"));
		sweepingDialogue = new Dialogue(dialogueBlock, null);
		
	}

	@Override
	public Dialogue startDialog(Player player, WorldState worldState, LevelState levelState) {
		return sweepingDialogue;
	}

	@Override
	public void think(Player player, WorldState worldState, LevelState levelState){
		if(getAction() == null){
			setAction(new RunRandomlyAction(this, worldState, 0,1));
		}
		sweepCounter--;
		if(sweepCounter<=0) {
			weapon.startUse();
			sweepCounter = 4;
		}
			
	}

	@Override
	public void draw(Graphics2D graphics, int frameCounter, int screenx, int screeny, boolean simulating) {
		super.draw(graphics, frameCounter, screenx, screeny, simulating);
		// Draw broom
		
		//graphics.drawImage(Resources.itemTileSet.getTileImageFromPositionUnsafe(Resources.ItemTileLocation.BROOM), position.x - screenx,position.y - screeny, null);
	}
	
}
