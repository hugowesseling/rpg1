package com.aquarius.rpg1;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DoorwayObject extends GameObject {
	private String levelToLoad;
	
	public DoorwayObject(TileDrawer tileDrawer, ObjectPosition position, Int2d levelpos){
		super(tileDrawer, position, Direction.NORTH);
		init();

		// Search for all levels with the name sub_xxx_yyy
		File dir = new File(".");
		String fileNamePrefix = String.format("sub_%03d_%03d_", levelpos.x, levelpos.y);
		File[] files = dir.listFiles((dir1, name) -> name.startsWith(fileNamePrefix) && name.endsWith(".rpg1"));
		
		String[] fileNameStrings = new String[files.length];
		int index = 0;
		for(File file:files) {
			String fileName = file.toString();
			fileNameStrings[index] = fileName.substring(14, fileName.length()-5);  
			System.out.println(fileNameStrings[index]);
			index++;
		}
		
		JComboBox<String> levelNameComboBox = new JComboBox<String>(fileNameStrings);
		levelNameComboBox.setEditable(true);
		levelNameComboBox.getEditor().setItem("level_name");
		Object objectSettings[] = {"Specify doorway settings", levelNameComboBox};
		
		JOptionPane optionPane = new JOptionPane();
	    optionPane.setMessage(objectSettings);
	    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	    JDialog dialog = optionPane.createDialog(null, "Doorway Settings");
	    dialog.setVisible(true);

	    levelToLoad = fileNamePrefix + levelNameComboBox.getSelectedItem().toString() + ".rpg1";
	    System.out.println("levelToLoad: " + levelToLoad);
	    File levelToLoadFile = new File(levelToLoad);
	    if(!levelToLoadFile.exists()) {
	    	createLevelSettings();
	    }
	}
	
	private void createLevelSettings() {
		JTextField widthTextField = new JTextField("20");
		JTextField heightTextField = new JTextField("20");
    	Object objectSettings[] = {"Create new level: " + levelToLoad, "width:", widthTextField, "height:", heightTextField};

    	JOptionPane optionPane = new JOptionPane();
	    optionPane.setMessage(objectSettings);
	    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	    JDialog dialog = optionPane.createDialog(null, "New level Settings");
	    dialog.setVisible(true);
	    
	    Resources.createLevel(levelToLoad, Integer.parseInt(widthTextField.getText()), Integer.parseInt(heightTextField.getText()));
	}

	private void init() {
		interactionPossibilities = new HashSet<>(Arrays.asList(InteractionPossibility.TOUCH));
	}
}
