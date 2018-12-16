package com.aquarius.rpg1;

/*
Need a simple story:
- Go find a person in another town and talk to some people that then tell different things.
	- Retrieve object after searching

Minimal additions:
- Collision with objects and other characters
	- Check if colliding
		- for each object, check world for colliding objects
			- define what are colliding objects:
				xMmark with tile editor special mode
					- houses except roofs
					- bottom of trees
				- Save all TileSet.tileCollision's into config
					Add to Rpg.saveConfigToFile()
					- First split up the tilePatterns to different tilesets
				
	- push both objects back until not colliding, position on pixel basis
- Random walking around midpoint behavior
- world state dependent dialogs
- Objects in the world:
	- Needs new drawing routine: Make the Actions responsible for deciding which image to draw and which collision model to use
- Stored objects:
	- Are only object types. Stored in a hashmap of {String:Object Behavior Class instance} for behavior definition.
	- Multiple object types can share behavior, maybe differ on parameters (appearance, strength, duration,...)
	- Objects stored can also appear in the world, then the objecttype is part of a StorableObject, which uses the objecttype for drawing
- Wandering person in village lost a special ring (with engraving of dead mother), only knows location where lost
	- At that location, find a person running around, catch him, but he will only give ring back if you help him
		-> He needs something to give to his girlfriend
		-> Find a nice flower and tell him that if she does not accept this, then it was not meant to be
	- Find girlfriend, she keeps hiding behind trees, does not want to meet him. Ask him to stay behind and tell her that he's sorry, he can then approach and make things up to her
	- you get the ring and bring it back to person in village,that gives you soup in his house (his mother's recipe)

	*Needed additions:
	TODO: following behavior
	DONE straightline, hiding
	DONE: Entering house
	DONE: Object storage, behavior and pickup
	DONE: Dialogue based on stored object
	TODO: Worldstate containing everything including player?

Then:
- World changing events:
	- Doors opening
		-> Needs storage of layer animations:
			-> Go in animation edit mode, control frame.
				-> Change some tiles
				-> Go to next frame, change some tiles
				-> Store entire sequence as animation
				-> Only store forward, reverse editing (to t-1) by reloading full map for only affected tiles and playing animation until point t-1
				-> Storage: anim:[{top:[x,y, tileIndex],bottom:[x,y, tileIndex]}]  (anim[0] has first frame 
	- Rocks moving
	- Characters/objects appearing
 

Possible game objects:
- Level push-on-stack and teleport (to go into house for instance)
	Should show possible levels to choose from, or create a new one (text field with name)
	Levels to jump to are in context of the current map coordinates
		-> What about jumping towards another point (actual warp?)
			- Maybe use separate teleport object
- treasure chest
- box of treasure
- Switch that opens door
- Block that can be pushed
- Switch with two states (red/green) 

Behavior:
The object should also do something with its contents

Should it be possible to have different images for the same type object? Almost no game does this!
-> hardcoding images used is no problem!

Each object type has a function:
Object[] getObjectSettings():
	{"Specify object settings", comboBox, comboBox}

When starting to add an object: Show the object type selection, then specify parameters, then create with those parameters and keep moving the object around with mouse
A left-click them simply does not control the object anymore, keeping it in its last place.

*Create level generator:
features:
- Tilepattern for any two materials
	-> Only thing needed is to assign two materials to a tilepattern, one for red, one for green
		-> Assign using other tiles
- Needs tile patterns over multiple tilesets...
	-> Major refactor..

*First create small forest scene for crystal quest


SFX:
Music:
	Town peaceful: https://musopen.org/music/85-berceuse-op-57/
	Town bit playful: https://musopen.org/music/2491-2-arabesques-l-66/ (first 2 mins or so)
	Sacred forest hideout: https://musopen.org/music/4478-danse-sacree-et-danse-profane/
	
	Other music:  D:\download\humble_bundle\gamedev\sfx\prosoundcollection_audio\prosoundcollection\Gamemaster Audio - Pro Sound Collection v1.3 - 16bit 48k\zzzBonus_Music_16bit44kOnlyzzz
		town: music_harp_peaceful_loop.wav
	forest: "D:\download\humble_bundle\gamedev\sfx\prosoundcollection_audio\prosoundcollection\Gamemaster Audio - Pro Sound Collection v1.3 - 16bit 48k\Animals_Nature_Ambiences\swamp_ambience_frogs_03_loop.wav"
	


 */

// DONE: health & dying
// TODO: particles: bouncing & flying
// DONE: Add house interiors: Walking into another level and out into previous level again
// DONE: Multi tileset tile patterns
// DONE: Dual material tile patterns
// TODO: Level generator using fancy tile patterns
// DONE: Level stack
// TODO: Optimize graphics2 (See https://stackoverflow.com/questions/658059/graphics-drawimage-in-java-is-extremely-slow-on-some-computers-yet-much-faster)
// TODO: Add shield
// TODO: Add clipboard window (to copy paste level parts on and off) 


// DONE: Add collision with objects
// DONE: Optimize graphics1: only draw when needed
// DONE: Add sword fighting
// DONE: Add character editing dialog upon adding a character
// DONE: stitching levels together
// DONE: Automatic path routing
//		 Create boolean map from where there is the path type and where there isn't, create a 8 boolean (8 directions) to one index (0-28) mapping to choose the correct tile
// DONE: View screen resizing
//		 Would have to change to JFrame instead of Applet
// DONE: larger selection in tileselector
// DONE: Undo
// DONE: 2 layers
// DONE: Copy / paste of selection rectangle
// DONE: Scroll when being close to the border

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.aquarius.common2dgraphics.util.Input;
import com.aquarius.rpg1.AudioSystemPlayer.RandomSound;
import com.aquarius.rpg1.Resources.CharacterCreatorFunction;
import com.aquarius.rpg1.Resources.ObjectCreatorFunction;
import com.aquarius.rpg1.behavior.hateno.HenryCharacter;
import com.aquarius.rpg1.behavior.hateno.HoppingCharacter;
import com.aquarius.rpg1.behavior.hateno.ProximityRunCharacter;
import com.aquarius.rpg1.behavior.hateno.RunningCharacter;
import com.aquarius.rpg1.behavior.hateno.SoupCharacter;

public class Rpg1 extends JComponent implements Runnable, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	volatile private boolean mouseDownLeft = false;
	volatile private boolean mouseDownRight = false;
	private static final long serialVersionUID = 1L;
	private static final int MENUBAR_HEIGHT = 45;
	private boolean running = false;
	private boolean simulating = false;;
	private Input input;
	private int screenx;
	private int screeny;
	private EditorState editorState;
	private TileSelectorFrame tileSelectorFrame;
	//private LevelStack levelStack;
	Int2d mouseStart = new Int2d(0,0);
	int mouseX=0, mouseY=0;
	int frameCounter;
	private boolean mouseInFrame;
	private Player player;
	private LevelState levelState;
	private WorldState worldState;
	private Dialogue dialogue;
	private boolean showInventory = false;
	private InventoryMenu inventoryMenu;
	private JFrame frame;
	private GameObject addObject = null;
	public Rpg1()
	{
		deleteFilesInUserFolder();
		frameCounter = 0;
		mouseInFrame = true;
		editorState = new EditorState();
		input=new com.aquarius.common2dgraphics.util.Input();
		dialogue = null;
		inventoryMenu = new InventoryMenu(Resources.dialogStyles.get(1));
		tileSelectorFrame = new TileSelectorFrame("TileSet", editorState);
		levelState = new LevelState(new Layer(1,1), new Layer(1,1));
		player = new Player(new CharacterDrawer(0), new ObjectPosition(0,0), Direction.SOUTH);
		player.setBeginPosition(levelState);

		System.out.println("Character sub classes: " + String.join(", ", Resources.characterSubClasses.keySet()));
		
		//levelState.allCharacters.add(new HenryCharacter(CharacterPosition.createFromTilePosition(new Int2d(10, 15)), new CharacterTileSet(new Int2d(3,0)), Direction.SOUTH));
		//levelState.allCharacters.add(new HenryCharacter(CharacterPosition.createFromTilePosition(new Int2d(15, 10)), new CharacterTileSet(new Int2d(3,0)), Direction.SOUTH));
		worldState = new WorldState();

		frame = new JFrame("Rpg");
		//frame.setLayout(new BorderLayout());
		frame.add(this);
		//setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		//frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(800,600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){			
		    public void windowClosing(WindowEvent e)
		    {
		    	Rpg1.this.closing();
		    }			
		});
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.addMouseWheelListener(this);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem gameModeMenuItem = new JMenuItem("Game mode"); 
		gameModeMenuItem.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				System.out.println("Game mode clicked");
	            } 
	          });
		fileMenu.add(gameModeMenuItem);
		
		JMenuItem saveMenuItem = new JMenuItem("Save"); 
		saveMenuItem.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				System.out.println("Save clicked");
				String fileName = levelState.getLevelFileName();
			    int retVal = JOptionPane.showConfirmDialog (null, "Would you like to overwrite " + fileName,"Warning", JOptionPane.YES_NO_OPTION);
			    if (retVal == JOptionPane.YES_OPTION) {
			    	levelState.saveToFile(fileName);
	            }
			}
		});
		fileMenu.add(saveMenuItem);

		JMenuItem saveAsMenuItem = new JMenuItem("Save As"); 
		saveAsMenuItem.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				System.out.println("Save As clicked");
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int retVal = fc.showSaveDialog(Rpg1.this);
				if(retVal == JFileChooser.APPROVE_OPTION) {
					levelState.saveToFile(fc.getSelectedFile().getPath());
	            }
			}
		});
		fileMenu.add(saveAsMenuItem);

		JMenuItem saveConfigMenuItem = new JMenuItem("Save config"); 
		saveConfigMenuItem.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				saveConfig();
			}

		});
		fileMenu.add(saveConfigMenuItem);
		
		JMenuItem exitMenuItem = new JMenuItem("Exit"); 
		exitMenuItem.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				System.out.println("Exit clicked");
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	            } 
	          });
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		
		JMenu simulateMenu = new JMenu("Simulate");
		JCheckBoxMenuItem simulateMenuItem = new JCheckBoxMenuItem("Simulate");
		simulateMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				simulating = ((AbstractButton)event.getSource()).getModel().isSelected();
			}
		});
		simulateMenu.add(simulateMenuItem);
		menuBar.add(simulateMenu);
		
		JMenu editMenu = new JMenu("Edit");
		JMenuItem addCharacterMenuItem = new JMenuItem("Add Character"); 
		addCharacterMenuItem.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent mouseEvent) {
				System.out.println("addCharacter clicked");
				Int2d mouseLocation = getMousePixelLocation(mouseEvent);
				String[] characterSubClassesStrings = Resources.characterSubClasses.keySet().toArray(new String[Resources.characterSubClasses.size()]);
				JComboBox<String> characterSubClassComboBox = new JComboBox<String>(characterSubClassesStrings);
				JComboBox<Direction> directionComboBox = new JComboBox<Direction>(Direction.values());
				CharacterTileSetChoosingLabel characterTileSetChoosingLabel = new CharacterTileSetChoosingLabel("character", frame, null);
				Object characterSettings[] = {"Specify character settings", characterSubClassComboBox, directionComboBox, characterTileSetChoosingLabel};
				
				JOptionPane optionPane = new JOptionPane();
			    optionPane.setMessage(characterSettings);
			    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog = optionPane.createDialog(null, "Character Settings");
			    dialog.setVisible(true);
			    
			    Direction direction = (Direction) directionComboBox.getSelectedItem();
			    String className = (String) characterSubClassComboBox.getSelectedItem();
				System.out.println("Placing character: " + className + ", " + direction);
				
				addObject = null;
				CharacterDrawer characterDrawer = new CharacterDrawer(characterTileSetChoosingLabel.getCharacterTileSetIndex());
				ObjectPosition position = new ObjectPosition(mouseLocation.x, mouseLocation.y);

				if(Resources.characterSubClasses.containsKey(className)) {
					CharacterCreatorFunction func = Resources.characterSubClasses.get(className);
					addObject = func.create(characterDrawer, position, direction);
				} else {
					System.err.println("Could not determine character sub class: " + className);
				}
				if(addObject != null)
					levelState.allGameObjects.add(addObject);
			} 
		});
		editMenu.add(addCharacterMenuItem);
		JMenuItem addObjectMenuItem = new JMenuItem("Add Object"); 
		addObjectMenuItem.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent mouseEvent) {

				Int2d mouseLocation = getMousePixelLocation(mouseEvent);
				String[] objectSubClassesStrings = Resources.objectSubClasses.keySet().toArray(new String[Resources.objectSubClasses.size()]);
				JComboBox<String> objectSubClassComboBox = new JComboBox<String>(objectSubClassesStrings);
				TileIndexChoosingLabel imageChoosingLabel = new TileIndexChoosingLabel("image",  frame, null); 
				Object objectSettings[] = {"Specify object settings", objectSubClassComboBox, imageChoosingLabel};
				
				JOptionPane optionPane = new JOptionPane();
			    optionPane.setMessage(objectSettings);
			    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
			    JDialog dialog = optionPane.createDialog(null, "Object Settings");
			    dialog.setVisible(true);
			    
			    String className = (String) objectSubClassComboBox.getSelectedItem();
				System.out.println("Adding object: " + className);

				addObject = null;
				int tileIndex = imageChoosingLabel.getTileIndex();
				if(Resources.objectSubClasses.containsKey(className)) {
					ObjectCreatorFunction func = Resources.objectSubClasses.get(className);
					addObject = func.create(new TileDrawer(tileIndex), new ObjectPosition(mouseLocation.x, mouseLocation.y), levelState);
				} else {
					System.err.println("Could not determine object sub class: " + className);
				}
				if(addObject != null)
					levelState.allGameObjects.add(addObject);
			} 
		});
		editMenu.add(addObjectMenuItem);
		
		JMenuItem resizeMapMenuItem = new JMenuItem("Resize map"); 
		resizeMapMenuItem.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				
				JTextField entryWidthTextField = new JTextField(Integer.toString(levelState.getWidth()));
				JTextField entryHeightTextField = new JTextField(Integer.toString(levelState.getHeight()));
				Object paneContents[] = {"Set new level size", "width:",entryWidthTextField, "height", entryHeightTextField};
				
				JOptionPane optionPane = new JOptionPane();
			    optionPane.setMessage(paneContents);
			    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
			    JDialog dialog = optionPane.createDialog(null, "Level size");
			    dialog.setVisible(true);
			    
				int newWidth = Integer.parseInt(entryWidthTextField.getText());
				int newHeight = Integer.parseInt(entryHeightTextField.getText());
				
				if(newWidth != levelState.getWidth() || newHeight != levelState.getHeight()) {
					levelState.levelStack.pushLayers();
				    levelState.resizeLevel(newWidth, newHeight); 
				}
			}
		});
		editMenu.add(resizeMapMenuItem);
		
		JMenuItem editMapParametersMenuItem = new JMenuItem("Edit map parameters"); 
		editMapParametersMenuItem.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JButton jAddParameterButton = new JButton("Add");
				HashMap<JComboBox<String>, JTextField> parameterTextFields = new HashMap<>();
				ArrayList<Object> paneContents = new ArrayList<Object>();
				paneContents.add("Set level parameters");
				paneContents.add(jAddParameterButton);
				for(Entry<String, String> entry:levelState.levelKeyValues.entrySet()) {
					
					JTextField jTextField = new JTextField(entry.getValue());
					JComboBox<String> jComboBox = new JComboBox<String>(Resources.defaultLevelParameters);
					jComboBox.setEditable(true);
					jComboBox.getEditor().setItem(entry.getKey());
					parameterTextFields.put(jComboBox, jTextField);
					paneContents.add(jComboBox);
					paneContents.add(jTextField);
				}
				JOptionPane optionPane = new JOptionPane();
			    optionPane.setMessage(paneContents.toArray());
			    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
			    JDialog dialog = optionPane.createDialog(null, "Level parameters");

				jAddParameterButton.addActionListener((ae) -> {
					JTextField jTextField = new JTextField("value");
					JComboBox<String> jComboBox = new JComboBox<String>(Resources.defaultLevelParameters);
					jComboBox.setEditable(true);
					jComboBox.getEditor().setItem("key");
					parameterTextFields.put(jComboBox, jTextField);
					paneContents.add(jComboBox);
					paneContents.add(jTextField);
					
					optionPane.setMessage(paneContents.toArray());
					dialog.pack();
					dialog.repaint();
				});
			    
			    dialog.setVisible(true);
			    
			    for(Entry<JComboBox<String>, JTextField> entry:parameterTextFields.entrySet()) {
			    	String key = (String) entry.getKey().getEditor().getItem();
			    	String value = entry.getValue().getText();
			    	System.out.println("levelKeyValues setting: " + key + "=" + value);
			    	levelState.levelKeyValues.put(key, value);
			    }
			}
		});
		editMenu.add(editMapParametersMenuItem);

		menuBar.add(editMenu);
		
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
	}
	private void deleteFilesInUserFolder() {
		File dir= new File(LevelState.USER_SAVE_FOLDER);
		for(File file: dir.listFiles()) 
		    if (!file.isDirectory() && file.toString().endsWith(".rpg1")) {
		    	System.out.println("Deleting "+ file);
		        file.delete();
		    }
	}
	public void start()
	{
		running=true;
		
		new Thread(this).start();
	}
	public void stop()
	{
		running=false;
	}

	@Override
	public void keyTyped(KeyEvent keyEvent)
	{
		System.out.println("Key typed: " + keyEvent);
		if(keyEvent.getKeyChar() == '')
		{
			System.out.println("Ctrl-Z typed");
			levelState.levelStack.popIntoLayers();
		}
	}
	@Override
	public void keyPressed(KeyEvent ke) 
	{
		//System.out.println("Key pressed");
		int keyCode = ke.getKeyCode();
		input.set(keyCode, true);
		if(keyCode == KeyEvent.VK_E) {
			if(addObject != null) {
				addObject = null;
			}else {
				float mindistance = 20;
				GameObject closestObject = null;
				ObjectPosition mousePosition = new ObjectPosition(mouseX/2 +screenx, mouseY/2 +screeny);
				for(GameObject gameObject:levelState.allGameObjects) {
					//System.out.println(gameObject.name + ": " + gameObject.position.x +"," +gameObject.position.y);
					if(gameObject.position.distanceTo(mousePosition) < mindistance) {
						mindistance = gameObject.position.distanceTo(mousePosition);
						closestObject = gameObject;
					}
				}
				if(closestObject != null)
					addObject = closestObject;
			}
		}
		if(keyCode == KeyEvent.VK_A)
		{
			System.out.println("Do action");
			if(dialogue != null) {
				if(!dialogue.confirm(levelState, player)) {
					dialogue = null;
				}
			}else {
				if(player.getInteractionGameObject() != null)
				{
					switch(player.getInteractionPossiblity()) {
					case OPEN:
						StorableObjectType storableObjectType = player.getInteractionGameObject().open();
						if(storableObjectType != null) {
							player.startItemAbovePlayerAnimation(storableObjectType);
							player.inventory.add(storableObjectType.name, storableObjectType.amount);
							AudioSystemPlayer.playSound(AudioSystemPlayer.AUDIO_FOLDER + "Collectibles_Items_Powerup\\collect_item_05.wav", false);
						}
						break;
					case UNLOCK:
						player.getInteractionGameObject().unlock(player, levelState);
						break;
					case TALK:
						startDialog(player.getInteractionGameObject());
						AudioSystemPlayer.playRandom(RandomSound.EXPRESSION);
						break;
					case TOUCH:
						break;
					default:
						break;
					
					}
				}				
			}
		}
		if(keyCode == KeyEvent.VK_X) {
			player.weapon = new BeamWeapon(player);
			player.useWeapon();
		}
		if(keyCode == KeyEvent.VK_H) {
			player.weapon = new Hammer(player);
			player.useWeapon();
		}
		if(keyCode == KeyEvent.VK_S) {
			simulating = !simulating;
		}
		if(keyCode == KeyEvent.VK_UP)
		{
			if(dialogue != null)
			{
				dialogue.up();
			}
		}
		if(keyCode == KeyEvent.VK_DOWN)
		{
			if(dialogue != null)
			{
				dialogue.down();
			}
		}
		if(keyCode == KeyEvent.VK_I) {
			showInventory = !showInventory;
		}
		if(keyCode == KeyEvent.VK_DELETE) {
			if(addObject != null) {
				levelState.allGameObjects.remove(addObject);
				addObject = null;
			}
		}
	}
	private void startDialog(GameObject dialogueGameObject) {
		System.out.println("Starting dialogue");
		dialogue = dialogueGameObject.startDialog(player, worldState, levelState);
		
	}
	@Override
	public void keyReleased(KeyEvent ke)
	{
		int keyCode = ke.getKeyCode();
		input.set(keyCode, false);
	}

	@Override
	public void run()
	{
		while(running)
		{
			repaint();
			tileSelectorFrame.updateFrame();
		}
	}
	
	@Override
	public void paint(Graphics g)
	{
		//super.paint(g);
		worldState.setTimeMs(System.currentTimeMillis());
		Dimension size = this.getSize();
		if(mouseInFrame)
		{
			if(!simulating)
				mouseCornerActions(size);
			if(addObject != null) {
				addObject.position.x = mouseX / 2 + screenx;
				addObject.position.y = mouseY / 2 + screeny;
			}

		}
		inputPlayerMovement();
		
		drawEverything(g);
		
		try
		{
			Thread.sleep(10);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		if(simulating) {
			frameCounter++;
			if(frameCounter % 10 == 0) {
				levelState.think(player, worldState);
			}
	
			levelState.doActionsWeaponAndMovement(worldState);
			
			player.doActionAndWeapon(worldState, levelState);
			player.determineInteractionGameObject(levelState.allGameObjects);
			player.checkIfTouching(levelState);
		}
	}
	private void drawEverything(Graphics g) {
		Dimension size2 = this.getSize();
		int imageWidth = size2.width / 2;
		int imageHeight = size2.height / 2;
		BufferedImage image = new BufferedImage(imageWidth, imageHeight,
	            BufferedImage.TYPE_INT_ARGB);
		Graphics2D imageGraphics=image.createGraphics();
		imageGraphics.setColor(Color.WHITE);
		imageGraphics.fillRect(0, 0, imageWidth, imageHeight);

		levelState.draw(imageGraphics, imageWidth, imageHeight, screenx, screeny, frameCounter, simulating, player);

		editorState.drawMapSelection(imageGraphics, screenx, screeny);
		
		if(simulating) {
			if(dialogue != null) {
				//System.out.println("Drawing dialogue");
				dialogue.draw(imageGraphics, 
						50, imageHeight-100, 
						imageWidth-100, 2 * Constant.TILE_HEIGHT);
			}else {		
				if(player.getInteractionGameObject() != null) {
					imageGraphics.setColor(Color.BLUE);
					imageGraphics.fillOval(imageWidth - 80,  10,  60, 40);
					imageGraphics.setColor(Color.WHITE);
					String actionText = player.getInteractionPossiblity().toString();
					int estimatedTextWidth = 4 * actionText.length();
					imageGraphics.drawString(actionText, imageWidth - 50 - estimatedTextWidth, 35);
				}
			}
			if(showInventory)
			{
				inventoryMenu.draw(imageGraphics,
						imageWidth/2 - 100, imageHeight/2 - 100,
						200, 200, player.inventory);
			}
		}
		drawHUD(imageGraphics, player);
		
		imageGraphics.dispose();

		g.drawImage(image, 0, 0, imageWidth * 2, imageHeight * 2, 0, 0,imageWidth,imageHeight,null);
	}
	
	private void drawHUD(Graphics2D graphics, Player player) {
		int currentHeartPos = 20;
		for(int i=3;i<player.health;i+=4) {
			graphics.drawImage(Resources.heartTileSet.tiles[0][10], currentHeartPos , 20 ,null);
			currentHeartPos+=14;
		}
		int lastHeart = player.health % 4; // = 0..3
		if(lastHeart>0)
			graphics.drawImage(Resources.heartTileSet.tiles[0][14 - lastHeart], currentHeartPos , 20 ,null);
			
	}
	private void inputPlayerMovement() {
		boolean playerMoved = false;
		int dx = 0, dy = 0;
		if(input.buttons[Input.LEFT])
		{
			dx = -2;
			player.setDirection(Direction.WEST);
			playerMoved = true;
		}
		if(input.buttons[Input.RIGHT])
		{
			dx = 2;
			player.setDirection(Direction.EAST);
			playerMoved = true;
		}
		if(input.buttons[Input.UP])
		{
			dy = -2;
			player.setDirection(Direction.NORTH);
			playerMoved = true;
		}
		if(input.buttons[Input.DOWN])
		{
			dy = 2;
			player.setDirection(Direction.SOUTH);
			playerMoved = true;
		}
		//Add dx,dy from colliding with other characters
		int bouncedx = 0, bouncedy = 0, bouncecount =0;
		for(GameObject gameObject: levelState.allGameObjects) {
			Int2d bounce = player.getCollisionBounce(gameObject);
			if(bounce != null)
			{
				bouncedx += bounce.x;
				bouncedy += bounce.y;
				bouncecount ++;
				player.getDamage(levelState,1);
			}
		}
		if(bouncecount != 0)
		{
			dx += bouncedx / bouncecount;
			dy += bouncedy / bouncecount;
		}

		
		playerMoved = player.moveAndLevelCollide(levelState, dx, dy);
		
		if(playerMoved){
			
			if(frameCounter % 15 == 0) {
				
				int walkSoundIndex = (int )(Math.random() * 12) + 1;
				String audioFileName = String.format(AudioSystemPlayer.AUDIO_FOLDER + "Footsteps\\footstep_dirt_walk_run_%02d.wav", 
						walkSoundIndex);
				AudioSystemPlayer.playSound(audioFileName, false);
			}
			screenx = player.position.x - 150;
			screeny = player.position.y - 150;
			Dimension size = this.getSize();
			int imageWidth = size.width / 2;
			int imageHeight = size.height / 2;

			if(screenx<0)screenx=0;
			if(screeny<0)screeny=0;
			if(screenx > levelState.getWidth() * Constant.TILE_WIDTH - imageWidth) {
				screenx = levelState.getWidth()*Constant.TILE_WIDTH - imageWidth;
			}
			if(screeny > levelState.getHeight() * Constant.TILE_HEIGHT - imageHeight) {
				screeny = levelState.getHeight() * Constant.TILE_HEIGHT - imageHeight;
			}				
		}
		if(player.position.x < Constant.TILE_WIDTH * 1)
		{
			levelState.loadLevelByExit(-1, 0, player);
		}
		if(player.position.x > (levelState.getWidth()-1) * Constant.TILE_WIDTH)
		{
			levelState.loadLevelByExit(1, 0, player);
		}
		if(player.position.y < Constant.TILE_HEIGHT * 1)
		{
			levelState.loadLevelByExit(0, -1, player);
		}
		if(player.position.y > (levelState.getHeight()-1) * Constant.TILE_HEIGHT)
		{
			levelState.loadLevelByExit(0, 1, player);
		}
	}
	private void mouseCornerActions(Dimension size) {
		if(mouseX < 20)
		{
			screenx -= 6;
			if(screenx<0)screenx=0;
		}
		if(mouseX > size.getWidth() - 20)
		{
			screenx += 6;
			if(screenx > levelState.getWidth() * Constant.TILE_WIDTH)
			{
				screenx = levelState.getWidth()*Constant.TILE_WIDTH;
			}
		}
		if(mouseY < 20)
		{
			screeny -= 6;
			if(screeny<0)screeny=0;
		}
		if(mouseY > size.getHeight() - 20)
		{
			screeny += 6;
			if(screeny > levelState.getHeight() * Constant.TILE_HEIGHT)
			{
				screeny = levelState.getHeight() * Constant.TILE_HEIGHT;
			}
		}
	}
	

	protected void closing()
	{
		System.out.println("Closing");
	}

	private void saveConfig() {
		Resources.saveTileSetData();
	}

	@Override
	public void mouseClicked(MouseEvent mouseEvent){
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseInFrame = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseInFrame = false;
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent)
	{
		if(mouseEvent.getButton() == MouseEvent.BUTTON1)
		{
			System.out.println("Mouse button 1 pressed: " + mouseEvent.getX() + ", " + mouseEvent.getY());
			mouseDownLeft = true;
			mouseStart = getMousePixelLocation(mouseEvent);
		}
		if(mouseEvent.getButton() == MouseEvent.BUTTON3)
		{
			System.out.println("Mouse button 3 pressed");
			mouseDownRight = true;
		}
		mouseMoved(mouseEvent);
	}
	private Int2d getMousePixelLocation(MouseEvent mouseEvent)
	{
		return new Int2d(mouseEvent.getX() / 2 + screenx,
						 (mouseEvent.getY() - MENUBAR_HEIGHT) / 2 + screeny);
	}
	@Override
	public void mouseReleased(MouseEvent mouseEvent)
	{
		if(mouseEvent.getButton() == MouseEvent.BUTTON1)
		{
			System.out.println("Mouse button 1 released");
			mouseDownLeft = false;
			
		}
		if(mouseEvent.getButton() == MouseEvent.BUTTON3)
		{
			System.out.println("Mouse button 3 released");
			mouseDownRight = false;
		}
	}
	@Override
	public void mouseDragged(MouseEvent mouseEvent)
	{
		mouseMoved(mouseEvent);
	}
	@Override
	public void mouseMoved(MouseEvent mouseEvent)
	{
		mouseX = mouseEvent.getX();
		mouseY = mouseEvent.getY() - MENUBAR_HEIGHT;
		Int2d mouseLocation = getMousePixelLocation(mouseEvent);
		int tileX = mouseLocation.x / Constant.TILE_WIDTH;
		int tileY = mouseLocation.y / Constant.TILE_HEIGHT;

		if(addObject  != null) {
			if(mouseDownLeft) {
				addObject = null;
				mouseDownLeft = false; //no ongoing clicking
			}
		}else
		if(input.buttons[Input.SHIFT] && mouseDownLeft)
		{
			int tileXStart = mouseStart.x / Constant.TILE_WIDTH;
			int tileYStart = mouseStart.y / Constant.TILE_HEIGHT;
			editorState.setMapSelection(tileXStart, tileYStart, tileX, tileY);
		}else
		if(input.buttons[Input.CONTROL] && (mouseDownLeft || mouseDownRight))
		{
			if(editorState.mapSelection != null)
			{
				Int2d topleft = editorState.mapSelection.topleft;
				Int2d bottomright = editorState.mapSelection.bottomright;
				Layer layer = mouseDownLeft ? levelState.top_layer : levelState.bottom_layer;
				levelState.levelStack.pushLayers();
				for(int x = 0; x <= bottomright.x - topleft.x; x++)
				{
					for(int y = 0; y <= bottomright.y - topleft.y; y++)
					{
						layer.setTileIndexForCheckedXY(x + tileX, y + tileY, layer.getTile(x + topleft.x, y + topleft.y));
					}
				}
				levelState.levelStack.popLayersIfNoChange();
			}
		}else
		{
			if(mouseDownLeft || mouseDownRight)
			{
				Layer layer = mouseDownLeft ? levelState.top_layer : levelState.bottom_layer;
				if(tileSelectorFrame.selectedTilePattern != null)
				{
					levelState.levelStack.pushLayers();
					tileSelectorFrame.selectedTilePattern.place(layer, tileX, tileY, true);
					levelState.levelStack.popLayersIfNoChange();
				}else
				if(editorState.tileSelection != null)
				{
					Int2d topleft = editorState.tileSelection.topleft;
					Int2d bottomright = editorState.tileSelection.bottomright;
					levelState.levelStack.pushLayers();
					for(int x = 0; x <= bottomright.x - topleft.x; x++)
					{
						for(int y = 0; y <= bottomright.y - topleft.y; y++)
						{
							layer.setTileIndexForCheckedXY(x + tileX, y + tileY, tileSelectorFrame.currentTileSet.getTileIndexFromXY(x + topleft.x, y + topleft.y));
						}
					}
					levelState.levelStack.popLayersIfNoChange();
				}
			}
		}
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent mwe) {
		boolean scrollup = mwe.getWheelRotation() <0;
		System.out.println("Mouse wheel: " + mwe.getWheelRotation() + ":"+ scrollup);
	}

	public static void main(String[] args)
	{
		Rpg1 rpg1=new Rpg1();
		rpg1.start();
	}
}
