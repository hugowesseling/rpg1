package com.aquarius.rpg1;

import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class LockedDoorwayObject extends DoorwayObject {
	private static final long serialVersionUID = -2825870237300156872L;
	public StorableObjectType storableObjectType;
	public LockedDoorwayObject(TileDrawer tileDrawer, ObjectPosition position, Int2d levelpos) {
		super(tileDrawer, position, levelpos);
		JComboBox<StorableObjectType> objectTypeComboBox = new JComboBox<StorableObjectType>(StorableObjectType.all);

		Object objectSettings[] = {"Specify lock item", objectTypeComboBox};
		
		JOptionPane optionPane = new JOptionPane();
	    optionPane.setMessage(objectSettings);
	    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	    JDialog dialog = optionPane.createDialog(null, "Doorway lock Settings");
	    dialog.setVisible(true);
		
	    storableObjectType = (StorableObjectType) objectTypeComboBox.getSelectedItem();
	    objectDrawer = new TileObjectDrawer(tileDrawer.tileIndex, storableObjectType.itemTileIndex);
	}

	protected void init() {
		interactionPossibilities = new HashSet<>(Arrays.asList(InteractionPossibility.UNLOCK));
	}

	@Override
	public boolean unlock(Player player, LevelState levelState) {
		int possibleRing = player.inventory.getCount(storableObjectType.name);
		if(possibleRing >= storableObjectType.amount)
		{
			player.inventory.remove(storableObjectType.name, storableObjectType.amount);
			levelState.allGameObjects.add(new DoorwayObject(name, new TileDrawer(((TileObjectDrawer)objectDrawer).tileIndex), position, direction, levelToLoad, entryPoint));
			// TODO: This can be slightly dangerous
			levelState.allGameObjects.remove(this);
			AudioSystemPlayer.playSound(AudioSystemPlayer.AUDIO_FOLDER + "Collectibles_Items_Powerup\\jingle_chime_04_positive.wav", false);
			player.startItemAbovePlayerAnimation(storableObjectType);
			return true;
		}
		System.out.println("Player does not have the " + storableObjectType.name);
		return false;
	}


	
/*	@Override
	public void (LevelState levelState, Player player) {
		System.out.println("Opening door:" + this);
	}*/

}
