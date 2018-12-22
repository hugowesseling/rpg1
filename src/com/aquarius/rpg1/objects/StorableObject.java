package com.aquarius.rpg1.objects;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.aquarius.rpg1.AudioSystemPlayer;
import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.InteractionPossibility;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.Resources;
import com.aquarius.rpg1.drawers.ItemTileDrawer;

public class StorableObject extends GameObject {


	private static final long serialVersionUID = -103755530791188265L;
	private StorableObjectType storableObjectType;

	public StorableObject(StorableObjectType storableObjectType, ItemTileDrawer itemTileDrawer, ObjectPosition position){
		super("StorableObject", itemTileDrawer, position, Direction.NORTH);
		this.storableObjectType = storableObjectType;
		init();
	}

	@Override
	protected void init() {
		interactionPossibilities = new HashSet<>(Arrays.asList(InteractionPossibility.TOUCH));
	}

	public static GameObject createStorableObject(ObjectPosition position) {
		String[] storableObjectTypeStrings = Resources.allStorableObjectTypesHashMap.keySet().toArray(new String[Resources.allStorableObjectTypesHashMap.size()]);
		JComboBox<String> storableObjectTypeComboBox = new JComboBox<String>(storableObjectTypeStrings);
		Object objectSettings[] = {"Specify object settings", storableObjectTypeComboBox};
		
		JOptionPane optionPane = new JOptionPane();
	    optionPane.setMessage(objectSettings);
	    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	    JDialog dialog = optionPane.createDialog(null, "Storable Object Choice");
	    dialog.setVisible(true);
	    
	    String storableObjectTypeString = (String) storableObjectTypeComboBox.getSelectedItem();
	    StorableObjectType sot = Resources.allStorableObjectTypesHashMap.get(storableObjectTypeString);
	    return new StorableObject(sot, new ItemTileDrawer(sot.itemTileIndex), position);
	}

	@Override
	public void doTouchAction(LevelState levelState, Player player) {
		// Remove this from the level and add to player inventory
		//levelState.allGameObjects.remove(this);
		Vector<GameObject> newAllGameObjects = new Vector<>();
		for(GameObject go:levelState.allGameObjects)
			if(go != this)
				newAllGameObjects.add(go);
		levelState.allGameObjects = newAllGameObjects;
		player.startItemAbovePlayerAnimation(storableObjectType);
		AudioSystemPlayer.playSound(AudioSystemPlayer.AUDIO_FOLDER + "Collectibles_Items_Powerup\\collect_item_03.wav", false);
		player.inventory.add(storableObjectType.name);
		System.out.println("New player inventory:");
		player.inventory.println();
	}

}
