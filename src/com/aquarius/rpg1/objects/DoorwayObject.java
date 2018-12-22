package com.aquarius.rpg1.objects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.aquarius.rpg1.Constant;
import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.InteractionPossibility;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.ObjectDrawer;
import com.aquarius.rpg1.ObjectPosition;
import com.aquarius.rpg1.Player;
import com.aquarius.rpg1.PositionLabel;
import com.aquarius.rpg1.Resources;
import com.aquarius.rpg1.drawers.TileDrawer;

public class DoorwayObject extends GameObject implements PositionLabel{
	private static final long serialVersionUID = 8928597995294293843L;
	protected String entryLabel;
	protected String levelBasename;
	protected Int2d levelPos;

	public DoorwayObject(String name, ObjectDrawer objectDrawer, ObjectPosition position, Direction direction,
			String levelBasename, Int2d levelPos, String entryLabel) {
		super(name, objectDrawer, position, direction);
		this.levelBasename = levelBasename;
		this.entryLabel = entryLabel;
		this.levelPos = levelPos;
	}

	public DoorwayObject(TileDrawer tileDrawer, ObjectPosition position){
		// Open a dialog with settings
		super("door"+position.x+"_"+position.y, tileDrawer, position, Direction.NORTH);
		init();

		// Search for all levels with the name sub_xxx_yyy
		File dir = new File(".");
		//File[] filesJust500500 = dir.listFiles((dir1, name) -> name.endsWith("_500_500.rpg1"));
		File[] filesAll = dir.listFiles((dir1, name) -> name.endsWith(".rpg1"));
		
		String[] fileNameStrings = new String[filesAll.length];
		int index = 0;
		for(File file:filesAll) {
			String fileName = file.getName();
			fileNameStrings[index] = fileName;//.substring(0, fileName.length()-5);  
			System.out.println(fileNameStrings[index]);
			index++;
		}

		JTextField nameTextField = new JTextField(name);
		JComboBox<String> levelNameComboBox = new JComboBox<String>(fileNameStrings);
		JTextField levelBaseNameTextField = new JTextField("level");
		JTextField levelPosXTextField = new JTextField("500"); 
		JTextField levelPosYTextField = new JTextField("500");		
		JTextField entryLabelTextField = new JTextField("default"); 
		levelNameComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				@SuppressWarnings("unchecked")
				JComboBox<String> comboBox = (JComboBox<String>) ae.getSource();
				String fileName = (String) comboBox.getSelectedItem();
				System.out.println("Selected: " + fileName);
				Matcher m = getLevelPosFromFileName(fileName);
				if(m != null) {
					System.out.println("Extracted level and position");
					levelPosXTextField.setText(m.group(2));
					levelPosXTextField.setText(m.group(3));
					levelBaseNameTextField.setText(m.group(1));
				}
			}

			private Matcher getLevelPosFromFileName(String fileName) {
				Pattern pattern = Pattern.compile("(.*)_(\\d+)_(\\d+)\\.rpg1");
				Matcher m = pattern.matcher(fileName);
				if(m.matches()) {
					return m;
				}
				return null;
			}
		});
		Object objectSettings[] = {"Specify doorway settings", "From name/label:", nameTextField, levelNameComboBox, levelBaseNameTextField,
				"level pos:", levelPosXTextField, levelPosYTextField, "To entry label:",entryLabelTextField};
		
		JOptionPane optionPane = new JOptionPane();
	    optionPane.setMessage(objectSettings);
	    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	    JDialog dialog = optionPane.createDialog(null, "Doorway Settings");
	    dialog.setVisible(true);

	    name = nameTextField.getText();
	    entryLabel = entryLabelTextField.getText();
	    levelPos = new Int2d(Integer.parseInt(levelPosXTextField.getText()), Integer.parseInt(levelPosYTextField.getText()));
	    levelBasename = levelBaseNameTextField.getText();
	    String levelToLoad = LevelState.levelPos2FileName(levelBasename, levelPos);
	    System.out.println("levelToLoad: " + levelToLoad);
	    File levelToLoadFile = new File(levelToLoad);
	    if(!levelToLoadFile.exists()) {
	    	createLevelSettings();
	    }
	}
	
	private void createLevelSettings() {
		JTextField widthTextField = new JTextField("20");
		JTextField heightTextField = new JTextField("20");
	    String levelToLoad = LevelState.levelPos2FileName(levelBasename, levelPos);
    	Object objectSettings[] = {"Create new level: " + levelToLoad, "width:", widthTextField, "height:", heightTextField};

    	JOptionPane optionPane = new JOptionPane();
	    optionPane.setMessage(objectSettings);
	    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	    JDialog dialog = optionPane.createDialog(null, "New level Settings");
	    dialog.setVisible(true);

	    Resources.createLevel(levelToLoad, Integer.parseInt(widthTextField.getText()), Integer.parseInt(heightTextField.getText()));
	}

	protected void init() {
		interactionPossibilities = new HashSet<>(Arrays.asList(InteractionPossibility.TOUCH));
	}

	@Override
	public void doTouchAction(LevelState levelState, Player player) {
		// Create a doorway back to the level you're coming from, just behind the entry point.
		Int2d fromLevelPos = levelState.levelPos.copy();
		String fromLevelbasename = levelState.levelBasename;
		
		System.out.println("Loading level:" + levelBasename + "_" + levelPos.x + "_" + levelPos.y + ", at entry label: " + entryLabel);
		//Set location and load level
		levelState.setLevelPos(levelBasename, levelPos);
		//Find doorway
		PositionLabel foundObject = null;
		//System.out.println("Searching for entry label:"+ entryLabel);
		for(GameObject gameObject:levelState.allGameObjects) {
			//System.out.println("Checking: "+gameObject.name);
			if(PositionLabel.class.isInstance(gameObject)) {
				//System.out.println("Checking positionLabel: \""+gameObject.name+"\" == \"" + ((PositionLabel)gameObject).getLabel()+ "\"");
				if(((PositionLabel)gameObject).getLabel().equals(entryLabel)) {
					System.out.println("Found entry label");
					foundObject= (PositionLabel) gameObject;
					break;
				}
			}
		}
		if(foundObject == null) {
			//Create a new doorway in the middle of the level jumping back to this location
			ObjectPosition midLevelPos = new ObjectPosition(levelState.getWidth() * Constant.TILE_WIDTH/2, 
				levelState.getHeight() * Constant.TILE_HEIGHT/2);
			DoorwayObject backDoorway = 
					//String name, ObjectDrawer objectDrawer, ObjectPosition position, Direction direction,
					//String levelBasename, Int2d levelPos, String entryLabel)
					new DoorwayObject(entryLabel, new TileDrawer(1280), midLevelPos,Direction.NORTH, 
							fromLevelbasename, fromLevelPos, name);
			levelState.gameObjectsToAdd.add(backDoorway);
			foundObject = backDoorway;
			System.out.println("Created doorway at "+ backDoorway.position);
		}
		player.setPosition(foundObject.getPosition().createForward(player.getDirection(), GameObject.DEFAULT_RADIUS + 1));
		System.out.println("Player position after moving through doorway:" + player.getPosition());
	}

	@Override
	public String getLabel() {
		return name;
	}
}
