package com.aquarius.rpg1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DoorwayObject extends GameObject {
	private static final long serialVersionUID = 8928597995294293843L;
	protected Int2d entryPoint;
	protected Int2d levelPos;
	protected String levelBasename;

	public DoorwayObject(String name, ObjectDrawer objectDrawer, ObjectPosition position, Direction direction,
			String levelBasename, Int2d levelPos, Int2d entryPoint) {
		super(name, objectDrawer, position, direction);
		this.levelBasename = levelBasename;
		this.entryPoint = entryPoint;
		this.levelPos = levelPos;
	}

	public DoorwayObject(TileDrawer tileDrawer, ObjectPosition position){
		// Open a dialog with settings
		super("doorway", tileDrawer, position, Direction.NORTH);
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
		
		JTextField entryXTextField = new JTextField("0"); 
		JTextField entryYTextField = new JTextField("0");
		JComboBox<String> levelNameComboBox = new JComboBox<String>(fileNameStrings);
		JTextField levelBaseNameTextField = new JTextField("level");
		JTextField levelPosXTextField = new JTextField("500"); 
		JTextField levelPosYTextField = new JTextField("500");		
		levelNameComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				@SuppressWarnings("unchecked")
				JComboBox<String> comboBox = (JComboBox<String>) ae.getSource();
				String fileName = (String) comboBox.getSelectedItem();
				System.out.println("Selected: " + fileName);
				Matcher m = getLevelPosFromFileName(fileName);
				if(m != null) {
					System.out.println("Found match!");
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
		Object objectSettings[] = {"Specify doorway settings", levelNameComboBox, levelBaseNameTextField, "level pos:", levelPosXTextField, levelPosYTextField, "entrypoint:", "x:",entryXTextField, entryYTextField};
		
		JOptionPane optionPane = new JOptionPane();
	    optionPane.setMessage(objectSettings);
	    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
	    JDialog dialog = optionPane.createDialog(null, "Doorway Settings");
	    dialog.setVisible(true);

	    entryPoint = new Int2d(Integer.parseInt(entryXTextField.getText()), Integer.parseInt(entryYTextField.getText()));
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
		ObjectPosition positionBehindEntryPoint = ObjectPosition.createFromTilePosition(entryPoint);
		positionBehindEntryPoint.x -= player.direction.movement.x * Constant.TILE_WIDTH * 3/2;
		positionBehindEntryPoint.y -= player.direction.movement.y * Constant.TILE_HEIGHT * 3/2;
		DoorwayObject backDoorway = 
				new DoorwayObject(levelBasename, new TileDrawer(0), positionBehindEntryPoint,
						Direction.NORTH, levelState.levelBasename, levelState.levelPos, 
						new Int2d((position.x-player.direction.movement.x*2)/Constant.TILE_WIDTH, 
								  (position.y-player.direction.movement.y*2)/Constant.TILE_HEIGHT));
		System.out.println("Loading level:" + levelBasename + "_" + levelPos.x + "_" + levelPos.y + ", at entry point: " + entryPoint);
		levelState.setLevelPos(levelBasename, levelPos);
		player.position = ObjectPosition.createFromTilePosition(entryPoint);
		// Create doorway object behind player back to previous location
		levelState.gameObjectsToAdd.add(backDoorway);
	}
}
