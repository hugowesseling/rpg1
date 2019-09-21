package com.aquarius.rpg1;

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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.swing.JButton;
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
import com.aquarius.rpg1.Resources.CharacterCreatorFunction;
import com.aquarius.rpg1.Resources.ObjectCreatorFunction;
import com.aquarius.rpg1.drawers.CharacterDrawer;
import com.aquarius.rpg1.drawers.TileDrawer;
import com.aquarius.rpg1.objects.GameObject;

public class LevelView extends JComponent implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
	private static final long serialVersionUID = 910926183968392643L;


	private static final int MENUBAR_HEIGHT = 45;
	
	protected LevelState levelState;
	protected TileSelectorFrame tileSelectorFrame;
	volatile private boolean mouseDownLeft = false;
	volatile private boolean mouseDownRight = false;
	protected Int2d mouseStart = new Int2d(0,0);
	protected int screenx;
	protected int screeny;
	protected GameObject addObject = null;
	protected Selection mapSelection = null;
	protected JFrame frame;
	private boolean editClusters;
	protected EditConfiguration editConfiguration;
	
	public LevelView(LevelState levelState, EditConfiguration editConfiguration) {
		this.levelState = levelState;
		this.editConfiguration = editConfiguration;
		editClusters = false;

		frame = new JFrame("Level view");
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
		    	LevelView.this.closing();
		    }			
		});
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.addMouseWheelListener(this);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem prepareRemovalMenuItem= new JMenuItem("Replace tiles from current selected tileset");
		prepareRemovalMenuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("Preparing removal by replacing all tiles from current frame with similar ones");
				levelState.replaceTilesFromTileSet(tileSelectorFrame.currentTileSet);
				
			}
		});
		fileMenu.add(prepareRemovalMenuItem);
		

		JMenuItem loadMenuItem = new JMenuItem("Load"); 
		loadMenuItem.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				System.out.println("Load clicked");
				//String fileName = levelState.getLatestLevelFileName();
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int retVal = fc.showOpenDialog(frame);
				if(retVal == JFileChooser.APPROVE_OPTION) {
					levelState.loadLevel(fc.getSelectedFile().getPath());
	            }
				
			}
		});
		fileMenu.add(loadMenuItem);

		JMenuItem saveMenuItem = new JMenuItem("Save"); 
		saveMenuItem.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				System.out.println("Save clicked");
				String fileName = levelState.getLatestLevelFileName();
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
				int retVal = fc.showSaveDialog(frame);
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
		
		
		JMenu editMenu = new JMenu("Edit");
		JMenuItem addCharacterMenuItem = new JMenuItem("Add Character"); 
		addCharacterMenuItem.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent mouseEvent) {
				System.out.println("addCharacter clicked");
				Int2d mouseLocation = getMousePixelLocation(mouseEvent);
				String[] characterSubClassesStrings = Resources.characterSubClasses.keySet().toArray(new String[Resources.characterSubClasses.size()]);
				JComboBox<String> characterSubClassComboBox = new JComboBox<String>(characterSubClassesStrings);
				characterSubClassComboBox.setMaximumRowCount(50);
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
					JButton jOpenFileButton = new JButton("Choose");
					jOpenFileButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							final JFileChooser fc = new JFileChooser();
							fc.setCurrentDirectory(new File(AudioSystemPlayer.AUDIO_FOLDER));
							int returnVal = fc.showOpenDialog(frame);
							if(returnVal == JFileChooser.APPROVE_OPTION) {
								String path = fc.getSelectedFile().toString();
								path = path.replaceFirst("^"+Pattern.quote(AudioSystemPlayer.AUDIO_FOLDER), "");
								jTextField.setText(path);
							}
						}
					});
					parameterTextFields.put(jComboBox, jTextField);
					paneContents.add(jComboBox);
					paneContents.add(jTextField);
					paneContents.add(jOpenFileButton);
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
					JButton jOpenFileButton = new JButton("Choose");
					jOpenFileButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							final JFileChooser fc = new JFileChooser();
							fc.setCurrentDirectory(new File(AudioSystemPlayer.AUDIO_FOLDER));
							int returnVal = fc.showOpenDialog(frame);
							if(returnVal == JFileChooser.APPROVE_OPTION) {
								String path = fc.getSelectedFile().toString();
								path = path.replaceFirst("^"+Pattern.quote(AudioSystemPlayer.AUDIO_FOLDER), "");
								jTextField.setText(path);
							}
						}
					});
					parameterTextFields.put(jComboBox, jTextField);
					paneContents.add(jComboBox);
					paneContents.add(jTextField);
					paneContents.add(jOpenFileButton);

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
		
		JMenu editExampleTilesMenu  = new JMenu("Cluster"); 
		editExampleTilesMenu.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				editConfiguration.clearSelectedTileCluster();
				editClusters = !editClusters;
				JMenu menu = (JMenu)e.getSource();
				if(editClusters) {
					menu.setText("*Cluster*");
				} else {
					menu.setText("Cluster");
				}
			}
		});
		menuBar.add(editExampleTilesMenu);
		
		JMenu analyzeMenu  = new JMenu("Analyze");
		JMenuItem analyzeMenuItem = new JMenuItem("Analyze tiles");
		analyzeMenuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("Analyzing..");
				// Create possible connection graph:
				editConfiguration.connectionGraph.addLayerConnections(levelState.bottom_layer);
			}
		});
		analyzeMenu.add(analyzeMenuItem);
		menuBar.add(analyzeMenu);
		
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
		
	}

	@Override
	public void keyPressed(KeyEvent ke) 
	{
		//System.out.println("Key pressed");
		int keyCode = ke.getKeyCode();
		Input.instance.set(keyCode, true);
		if(keyCode == KeyEvent.VK_C) {
			PipedOutputStream pipedOutputStream = new PipedOutputStream();
			PipedInputStream pipedInputStream = new PipedInputStream();
			try {
				pipedInputStream.connect(pipedOutputStream);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(pipedOutputStream);
				ObjectInputStream objectInputStream = new ObjectInputStream(pipedInputStream);
				objectOutputStream.writeObject(addObject);
				addObject = (GameObject) objectInputStream.readObject();
				System.out.println("Created copy: " + addObject);
				levelState.allGameObjects.add(addObject);
				objectInputStream.close();
				objectOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		if(keyCode == KeyEvent.VK_DELETE) {
			if(addObject != null) {
				levelState.allGameObjects.remove(addObject);
				addObject = null;
			}
		}
	}
	
	
	protected void closing()
	{
		System.out.println("Closing");
	}

	protected void mouseCornerActions(Dimension size, int mouseX, int mouseY) {
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
	
	@Override
	public void mouseClicked(MouseEvent mouseEvent){
	}


	@Override
	public void mousePressed(MouseEvent mouseEvent)
	{
		if(mouseEvent.getButton() == MouseEvent.BUTTON1)
		{
			System.out.println("Mouse button 1 pressed: " + mouseEvent.getX() + ", " + mouseEvent.getY());
			mouseStart = getMousePixelLocation(mouseEvent);
			if(editClusters) {
				Int2d mouseLocation = getMousePixelLocation(mouseEvent);
				int tileX = mouseLocation.x / Constant.TILE_WIDTH;
				int tileY = mouseLocation.y / Constant.TILE_HEIGHT;
				int index = levelState.bottom_layer.getTile(tileX, tileY);
				editConfiguration.addIndexToTileCluster(index);
			} else {
				mouseDownLeft = true;
				if(addObject != null) {
					addObject = null;
				}else {
					float mindistance = 20;
					GameObject closestObject = null;
					ObjectPosition mousePosition = new ObjectPosition(mouseStart.x, mouseStart.y);
					for(GameObject gameObject:levelState.allGameObjects) {
						//System.out.println(gameObject.name + ": " + gameObject.position.x +"," +gameObject.position.y);
						if(gameObject.getPosition().distanceTo(mousePosition) < mindistance) {
							mindistance = gameObject.getPosition().distanceTo(mousePosition);
							closestObject = gameObject;
						}
					}
					if(closestObject != null)
					{
						addObject = closestObject;
						mouseDownLeft = false; //no more effects after selecting an objects
					}
				}
			}
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
		int mouseX = mouseEvent.getX();
		int mouseY = mouseEvent.getY() - MENUBAR_HEIGHT;
		Int2d mouseLocation = getMousePixelLocation(mouseEvent);
		int tileX = mouseLocation.x / Constant.TILE_WIDTH;
		int tileY = mouseLocation.y / Constant.TILE_HEIGHT;
		Dimension size = frame.getSize();
		
		mouseCornerActions(size, mouseX, mouseY);

		if(addObject  != null) {
			addObject.setPosition(new ObjectPosition(
					mouseX / 2 + screenx, mouseY / 2 + screeny));
			if(mouseDownLeft) {
				addObject = null;
				mouseDownLeft = false; //no ongoing clicking
			}
		}else
		if(Input.instance.buttons[Input.SHIFT] && mouseDownLeft)
		{
			int tileXStart = mouseStart.x / Constant.TILE_WIDTH;
			int tileYStart = mouseStart.y / Constant.TILE_HEIGHT;
			mapSelection = new Selection(tileXStart, tileYStart, tileX, tileY);
			new ClipBoard(mapSelection, levelState.bottom_layer, levelState.top_layer);
		}else
		if(mouseDownRight || mouseDownLeft)
		{
			if(Input.instance.buttons[Input.CONTROL])
			{
				if(ClipBoard.instance != null)
				{
					levelState.levelStack.pushLayers();
					ClipBoard.instance.copyToLayer(
							mouseDownRight?levelState.bottom_layer:null, 
							mouseDownLeft?levelState.top_layer:null,
							tileX, tileY);
					levelState.levelStack.popLayersIfNoChange();
				}
			}else
			{
				Layer layer = mouseDownLeft ? levelState.top_layer : levelState.bottom_layer;
				if(editConfiguration.getSelectedTilePattern() != null)
				{
					levelState.levelStack.pushLayers();
					editConfiguration.getSelectedTilePattern().place(layer, tileX, tileY, true);
					levelState.levelStack.popLayersIfNoChange();
				}else if(editConfiguration.getSelectedTileCluster() != null) {
					levelState.levelStack.pushLayers();
					//editConfiguration.placeSelectedTileCluster3(layer, tileX, tileY);
					editConfiguration.placeBestTileClusterMiddleAndSurroundings(layer, tileX, tileY);
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
	public void keyReleased(KeyEvent ke)
	{
		int keyCode = ke.getKeyCode();
		Input.instance.set(keyCode, false);
	}

	private void saveConfig() {
		Resources.saveTileSetData();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paint(Graphics g) {
		Dimension size2 = this.getSize();
		int imageWidth = size2.width / 2;
		int imageHeight = size2.height / 2;
		BufferedImage image = new BufferedImage(imageWidth, imageHeight,
	            BufferedImage.TYPE_INT_ARGB);
		Graphics2D imageGraphics=image.createGraphics();
		imageGraphics.setColor(Color.WHITE);
		imageGraphics.fillRect(0, 0, imageWidth, imageHeight);

		drawLevel(imageGraphics, imageWidth, imageHeight);
		if(editClusters) {
			drawClusters(imageGraphics, editConfiguration, levelState.bottom_layer, imageWidth, imageHeight, screenx, screeny);
		}
		
		imageGraphics.dispose();

		g.drawImage(image, 0, 0, imageWidth * 2, imageHeight * 2, 0, 0,imageWidth,imageHeight,null);
		
	}

	protected void drawLevel(Graphics2D imageGraphics, int imageWidth, int imageHeight) {
		levelState.draw(imageGraphics, imageWidth, imageHeight, screenx, screeny, 0, false);
		if(mapSelection != null)
			mapSelection.draw(imageGraphics, screenx, screeny);
	}

	private static void drawClusters(Graphics2D imageGraphics, EditConfiguration editConfiguration, Layer layer, int imageWidth, int imageHeight,
			int screenx, int screeny) {
		//Draw borders, but only if the border is between different clusters
		int tileswidth = imageWidth/16 + 2;
		int tilesheight = imageHeight/16 + 2;
		int screenblockx=screenx/16,screenblocky=screeny/16;
		int ystart = screenblocky, yend = screenblocky+tilesheight;
		int maxwidth = layer.getWidth();
		int maxheight = layer.getHeight(); 
		if(ystart<0)ystart=0;
		if(yend > maxheight)yend = maxheight;
		int xstart = screenblockx, xend = screenblockx+tileswidth;
		if(xstart<0)xstart=0;
		if(xend>maxwidth)xend=maxwidth;
		for(int y=ystart;y<yend;y++)
		{
			for(int x=xstart;x<xend;x++)
			{
				int index = layer.getTile(x, y);
				TileCluster foundTileCluster = editConfiguration.findClusterForTileIndex(index);
				if(foundTileCluster != null) {
					imageGraphics.setColor(foundTileCluster.color);
					imageGraphics.drawRect(x*16-screenx+2, y*16-screeny+2, 12, 12);
				}
			}
		}
	}

}
