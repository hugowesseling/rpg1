package com.aquarius.rpg1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class StorableObject extends GameObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -103755530791188265L;
	private StorableObjectType storableObjectType;

	public StorableObject(StorableObjectType storableObjectType, TileDrawer tileDrawer, ObjectPosition position){
		super("StorableObject", tileDrawer, position, Direction.NORTH);
		this.storableObjectType = storableObjectType;
		init();
	}

	@Override
	protected void init() {
		interactionPossibilities = new HashSet<>(Arrays.asList(InteractionPossibility.TOUCH));
	}

	public static GameObject createStorableObject(int tileIndex, ObjectPosition position) {
		String[] storableObjectTypeStrings = StorableObjectType.allHashMap.keySet().toArray(new String[StorableObjectType.allHashMap.size()]);
		JComboBox<String> storableObjectTypeComboBox = new JComboBox<String>(storableObjectTypeStrings);
		Object objectSettings[] = {"Specify object settings", storableObjectTypeComboBox};
		
		JOptionPane optionPane = new JOptionPane();
	    optionPane.setMessage(objectSettings);
	    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	    JDialog dialog = optionPane.createDialog(null, "Storable Object Choice");
	    dialog.setVisible(true);
	    
	    String storableObjectTypeString = (String) storableObjectTypeComboBox.getSelectedItem();
	    StorableObjectType sot = StorableObjectType.allHashMap.get(storableObjectTypeString);
	    return new StorableObject(sot, new TileDrawer(sot.tileIndex), position );
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
		
		player.inventory.add(storableObjectType);
		System.out.println("New player inventory:");
		player.inventory.println();
	}

}
