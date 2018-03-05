package com.aquarius.rpg1.behavior.hateno;

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

public class HenryCharacter extends GameCharacter
{
	private static final long serialVersionUID = 2895154313614688180L;

	public HenryCharacter(CharacterPosition position, CharacterTileSet characterTileSet, Direction direction) {
		super(position, characterTileSet, direction);
		getInteractionPossibilities().add(InteractionPossibility.TALK);
		
		DialogueBlock dialogueBlock = new DialogueBlock("Hello");
		dialogueBlock.add(new DialogueBlock("How are you doing?"))
					 .add(new DialogueBlock("I'm doing great!"))
				     .add(new DialogueBlock("See you later!"));
		dialogue = new Dialogue(dialogueBlock, Resources.dialogStyles.get(0));
	}

	private final static int STANDINGAROUND_DURATION = 10000;
	
	public void think(Player player, WorldState worldState, LevelState levelState)
	{
		// Henry's behavior: walk from tree to tree, if player gets near, walk up to him and wait
		// Player can then initiate dialog
	 
		if(!(getAction() instanceof WalkToCharacterAction)){
			if(player.getPosition().subnearby(position, 80)) {
				setAction(new WalkToCharacterAction(this, player, 20, 100));
				System.out.println("HenryCharacter.think: WalkToCharacterAction");
				return;
			}
		}
		if(getAction() == null)	{
			Int2d treePosition = levelState.findRandomPositionInNeighborhood(TileObjectIndex.TREE1, position.tileAsInt2d(), 10);
			if(treePosition != null) {
				setAction(new WalkToTilePositionAction(this, treePosition));
				System.out.println("HenryCharacter.think: WalkToPositionAction");
			}else {
				setAction(new WaitAction(worldState, STANDINGAROUND_DURATION));
				System.out.println("HenryCharacter.think: WaitAction");
			}
		}
	}
}
