package com.aquarius.rpg1;

import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class TreasureObject extends GameObject{
	private static final long serialVersionUID = 4226718004114866622L;
	private StorableObjectType storableObjectType;

	public TreasureObject(TileDrawer tileDrawer, ObjectPosition position){
		super("treasureObject", tileDrawer, position, Direction.NORTH);
		init();
		String[] storableObjectTypeStrings = Resources.allStorableObjectTypesHashMap.keySet().toArray(new String[Resources.allStorableObjectTypesHashMap.size()]);
		JComboBox<String> storableObjectTypeComboBox = new JComboBox<String>(storableObjectTypeStrings);
		Object objectSettings[] = {"Specify object settings", storableObjectTypeComboBox};
		
		JOptionPane optionPane = new JOptionPane();
	    optionPane.setMessage(objectSettings);
	    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	    JDialog dialog = optionPane.createDialog(null, "TreasureObject storable Object Choice");
	    dialog.setVisible(true);
	    
	    String storableObjectTypeString = (String) storableObjectTypeComboBox.getSelectedItem();
	    storableObjectType = Resources.allStorableObjectTypesHashMap.get(storableObjectTypeString);
	}
	
	protected void init() {
		interactionPossibilities = new HashSet<>(Arrays.asList(InteractionPossibility.OPEN));
	}

	public StorableObjectType getStorableObjectType() {
		return storableObjectType;
	}

	public StorableObjectType open() {
		StorableObjectType result = storableObjectType;
		storableObjectType = null;
		interactionPossibilities = new HashSet<>();
		return result;
	}
	

}
