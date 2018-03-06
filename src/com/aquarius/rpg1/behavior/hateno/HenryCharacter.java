package com.aquarius.rpg1.behavior.hateno;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

import com.aquarius.rpg1.CharacterPosition;
import com.aquarius.rpg1.CharacterTileSet;
import com.aquarius.rpg1.Dialogue;
import com.aquarius.rpg1.DialogueBlock;
import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameCharacter;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.InteractionPossibility;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.Resources;
import com.aquarius.rpg1.TileObjectIndex;
import com.aquarius.rpg1.WorldState;
import com.aquarius.rpg1.behavior.WaitAction;
import com.aquarius.rpg1.behavior.WalkToCharacterAction;
import com.aquarius.rpg1.behavior.WalkToTilePositionAction;

public class HenryCharacter extends GameCharacter implements Serializable
{
	private static final long serialVersionUID = 2895154313614688180L;

	static {
		System.out.println("HenryCharacter.static");
		Resources.addCharacterSubClass(HenryCharacter.class.getSimpleName());
	}
	
	public HenryCharacter()
	{
		super();
		System.out.println("HenryCharacter: Empty Constructor position: "  +position + ", for name " + name);
	}
	
	public HenryCharacter(CharacterPosition position, CharacterTileSet characterTileSet, Direction direction) {
		super(position, characterTileSet, direction);
		init();
		System.out.println("HenryCharacter: Constructor position: "  +position + ", for name " + name);
	}

	private void init() {
		interactionPossibilities = new HashSet<>(Arrays.asList(InteractionPossibility.TALK));
		DialogueBlock dialogueBlock = new DialogueBlock("Hello");
		dialogueBlock.add(new DialogueBlock("How are you doing?"))
					 .add(new DialogueBlock("I'm doing great!"))
				     .add(new DialogueBlock("See you later!"));
		dialogue = new Dialogue(dialogueBlock, null); // Resources.dialogStyles.get(0));
		System.out.println("HenryCharacter: init()");
	}

	private final static int STANDINGAROUND_DURATION = 10000;
	
	public void think(Player player, WorldState worldState, LevelState levelState)
	{
		// Henry's behavior: walk from tree to tree, if player gets near, walk up to him and wait
		// Player can then initiate dialog
	 
		if(!(getAction() instanceof WalkToCharacterAction)){
			if(player.getPosition().subnearby(position, 80)) {
				setAction(new WalkToCharacterAction(this, player, 20, 100));
				//System.out.println("HenryCharacter.think: WalkToCharacterAction");
				return;
			}
		}
		if(getAction() == null)	{
			Int2d treePosition = levelState.findRandomPositionInNeighborhood(TileObjectIndex.TREE1, position.tileAsInt2d(), 10);
			if(treePosition != null) {
				setAction(new WalkToTilePositionAction(this, treePosition));
				//System.out.println("HenryCharacter.think: WalkToPositionAction");
			}else {
				setAction(new WaitAction(worldState, STANDINGAROUND_DURATION));
				//System.out.println("HenryCharacter.think: WaitAction");
			}
		}
	}

	private void readObject(ObjectInputStream ois)
		    throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		init();
		//name = (String)ois.readObject();
		//position = (CharacterPosition) ois.readObject();
		//direction = (Direction) ois.readObject();
		System.out.println("HenryCharacter:Read position: "  +position + ", for name " + name);
	}	
}
