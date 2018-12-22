package com.aquarius.rpg1.behavior.hateno;

import java.util.Arrays;
import java.util.HashSet;

import com.aquarius.rpg1.Dialogue;
import com.aquarius.rpg1.DialogueBlock;
import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.InteractionPossibility;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.drawers.CharacterDrawer;
import com.aquarius.rpg1.objects.GameObject;

public class SoupCharacter extends GameObject {

	private static final long serialVersionUID = -1794298120753559536L;
	private transient Dialogue nosoupDialogue, soupDialogue;

	public SoupCharacter(CharacterDrawer characterDrawer, ObjectPosition objectPosition, Direction direction) {
		super("SoupCharacter", characterDrawer, objectPosition, direction);
	}

	@Override
	protected void init() {
		System.out.println("SoupCharacter: init()");
		interactionPossibilities = new HashSet<>(Arrays.asList(InteractionPossibility.TALK));
		
		DialogueBlock dialogueBlock = new DialogueBlock("Hi dear");
		dialogueBlock.add(new DialogueBlock("You're looking well today"))
					 .add(new DialogueBlock("Say, do you see that soup there?"))
				     .add(new DialogueBlock("Please get it for me"));
		nosoupDialogue = new Dialogue(dialogueBlock, null); // Resources.dialogStyles.get(0));
		
		dialogueBlock = new DialogueBlock("Hi again!");
		dialogueBlock.add(new DialogueBlock("It's so nice of you to finally bring me some soup"))
					 .add(new DialogueBlock("Thank you!", (l,p) -> p.inventory.remove("soup", 1)) )			 
					 .add(new DialogueBlock("Okay, I won't hold you up any longer"));
		soupDialogue = new Dialogue(dialogueBlock, null); // Resources.dialogStyles.get(0));
	}

	@Override
	public Dialogue startDialog(Player player, WorldState worldState, LevelState levelState) {
		if(player.inventory.getCount("soup") > 0) {
			return soupDialogue;
		}else
			return nosoupDialogue;
	}
}
