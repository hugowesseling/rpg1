package com.aquarius.rpg1;

import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class StorableObject extends GameObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -103755530791188265L;

	public StorableObject(StorableObjectType storableObjectType, TileDrawer tileDrawer, ObjectPosition position){
		super("StorableObject", tileDrawer, position, Direction.NORTH);
		init();
	}

	@Override
	protected void init() {
		interactionPossibilities = new HashSet<>(Arrays.asList(InteractionPossibility.PICKUP));
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

}
