package com.aquarius.rpg1.behavior.hateno;

import java.util.Arrays;
import java.util.HashSet;

import com.aquarius.rpg1.Dialogue;
import com.aquarius.rpg1.DialogueBlock;
import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.InteractionPossibility;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.TileObjectIndex;
import com.aquarius.rpg1.WorldTime;
import com.aquarius.rpg1.behavior.WaitAction;
import com.aquarius.rpg1.behavior.WalkToCharacterAction;
import com.aquarius.rpg1.behavior.WalkToPositionAction;
import com.aquarius.rpg1.objects.GameObject;

public class HenryCharacter extends GameObject
{
	private static final long serialVersionUID = 2895154313614688180L;
	protected transient Dialogue dialogue1 = null, dialogue2 = null;

	/*public HenryCharacter()
	{
		super();
		System.out.println("HenryCharacter: Empty Constructor position: "  +position + ", for name " + name);
	}*/
	
	public HenryCharacter(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("henryCharacter", objectDrawer, position, direction);
		System.out.println("HenryCharacter: Constructor position: "  +position + ", for name " + name);
	}

	protected void init() {
		System.out.println("HenryCharacter: init()");
		interactionPossibilities = new HashSet<>(Arrays.asList(InteractionPossibility.TALK));
		DialogueBlock dialogueBlock = new DialogueBlock("Hello, we're now higher");
		dialogueBlock.add(new DialogueBlock("How are you doing?"))
					 .add(new DialogueBlock("I'm doing great!"))
				     .add(new DialogueBlock("See you later!"));
		dialogue1 = new Dialogue(dialogueBlock, null); // Resources.dialogStyles.get(0));
		dialogueBlock = new DialogueBlock("Hello, we're now lower");
		dialogueBlock.add(new DialogueBlock("Goodbye"));
		dialogue2 = new Dialogue(dialogueBlock, null); // Resources.dialogStyles.get(0));
	}

	private final static int STANDINGAROUND_DURATION = 10000;
	
	@Override
	public void think(Player player, WorldTime worldState, LevelState levelState)
	{
		// Henry's behavior: walk from tree to tree, if player gets near, walk up to him and wait
		// Player can then initiate dialog
	 
		if(!(getAction() instanceof WalkToCharacterAction)){
			if(player.getPosition().isNearby(position, 40)) {
				setAction(new WalkToCharacterAction(this, player, 20, 100));
				System.out.println("HenryCharacter.think: WalkToCharacterAction");
				return;
			}
		}
		if(getAction() == null)	{
			Int2d treePosition = levelState.findRandomPositionInNeighborhood(TileObjectIndex.TREE1, position.tileAsInt2d(), 10);
			if(treePosition != null) {
				setAction(new WalkToPositionAction(this, worldState, treePosition, 0, 1));
				System.out.println("HenryCharacter.think: WalkToPositionAction");
			}else {
				setAction(new WaitAction(worldState, STANDINGAROUND_DURATION));
				System.out.println("HenryCharacter.think: WaitAction");
			}
		}
	}
	
	@Override
	public Dialogue startDialog(Player player, LevelState levelState) {
		if(position.y<100)
			return dialogue1;
		else
			return dialogue2;
	}
}
