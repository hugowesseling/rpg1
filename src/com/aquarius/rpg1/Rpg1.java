package com.aquarius.rpg1;

// TODO: stitching levels together
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
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.aquarius.common2dgraphics.util.Input;
import com.aquarius.rpg1.behavior.hateno.HenryCharacter;

public class Rpg1 extends JComponent implements Runnable, KeyListener, MouseListener, MouseMotionListener {
	volatile private boolean mouseDownLeft = false;
	volatile private boolean mouseDownRight = false;
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_SAVE_FILENAME = "test.rpg1";
	private static final int MENUBAR_HEIGHT = 45;
	private boolean running = false;
	private Input input;
	private int screenx;
	private int screeny;
	private EditorState editorState;
	private Layer bottom_layer, top_layer;
	private TileSelectorFrame tileSelectorFrame;
	private LevelStack levelStack;
	private TileSet characterTileSet;
	Int2d mouseStart = new Int2d(0,0);
	int mouseX=0, mouseY=0;
	int frameCounter;
	private boolean mouseInFrame;
	private HenryCharacter henry;
	private Player player;
	private LevelState levelState;
	private WorldState worldState;
	private Dialogue dialogue;
	private ArrayList<DialogStyle> dialogStyles;
	private TileSet levelTileSet;

	public Rpg1()
	{
		frameCounter = 0;
		mouseInFrame = true;
		editorState = new EditorState();
		levelTileSet = new TileSet("/roguelikeSheet_transparent.png", 16, 16, 1, 1);
		tileSelectorFrame = new TileSelectorFrame("TileSet", levelTileSet, editorState);
		characterTileSet = new TileSet("/characters1.png", 26, 36, 0, 0);
		bottom_layer = new Layer(levelTileSet);
		top_layer = new Layer(levelTileSet);
		levelStack = new LevelStack(bottom_layer, top_layer);
		input=new com.aquarius.common2dgraphics.util.Input();
		dialogStyles = new ArrayList<DialogStyle>();
		dialogue = null;

		readFromFile(DEFAULT_SAVE_FILENAME);
		
		henry = new HenryCharacter(CharacterPosition.createFromTilePosition(new Int2d(10, 10)), new CharacterTileSet(new Int2d(3,0), characterTileSet), Direction.SOUTH, dialogStyles);
		player = new Player(CharacterPosition.createFromTilePosition(new Int2d(5, 5)), new CharacterTileSet(new Int2d(0,0), characterTileSet), Direction.SOUTH);
		levelState = new LevelState(bottom_layer, top_layer);
		levelState.allCharacters.add(henry);
		worldState = new WorldState();

		JFrame frame = new JFrame("Rpg");
		//frame.setLayout(new BorderLayout());
		frame.add(this);
		//setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		//frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(800,600));
		frame.setVisible(true);
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
				saveToFile(DEFAULT_SAVE_FILENAME);
	            } 
	          });
		fileMenu.add(saveMenuItem);
		JMenuItem exitMenuItem = new JMenuItem("Exit"); 
		exitMenuItem.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				System.out.println("Exit clicked");
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	            } 
	          });
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);
		
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
		//System.out.println("Key typed: " + keyEvent);
		if(keyEvent.getKeyChar() == '')
		{
			System.out.println("Ctrl-Z typed");
			levelStack.popIntoLayers();
		}
	}
	@Override
	public void keyPressed(KeyEvent ke) 
	{
		int keyCode = ke.getKeyCode();
		input.set(keyCode, true);
		if(keyCode == KeyEvent.VK_A)
		{
			System.out.println("Do action");
			if(dialogue != null) {
				if(!dialogue.confirm()) {
					dialogue = null;
				}
			}else {
				if(player.getTalkActionCharacter() != null)
				{
					startDialog();
				}				
			}
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
	}
	private void startDialog() {
		System.out.println("Starting dialogue");
		dialogue = player.getTalkActionCharacter().startDialog();
		
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
		worldState.setTimeMs(System.currentTimeMillis());
		Dimension size = this.getSize();
		int imageWidth = size.width / 2;
		int imageHeight = size.height / 2;
		//System.out.println("Size: " + imageWidth + "," + imageHeight);
		if(mouseInFrame)
		{
			if(mouseX < 20)
			{
				screenx -= 6;
				if(screenx<0)screenx=0;
			}
			if(mouseX > size.getWidth() - 20)
			{
				screenx += 6;
				if(screenx > bottom_layer.getWidth() * Constant.TILE_WIDTH)
				{
					screenx = bottom_layer.getWidth()*Constant.TILE_WIDTH;
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
				if(screeny > bottom_layer.getHeight() * Constant.TILE_HEIGHT)
				{
					screeny = bottom_layer.getHeight() * Constant.TILE_HEIGHT;
				}
			}
			
			boolean playerMoved = false;
			if(input.buttons[Input.LEFT])
			{
				player.position.x -= 2;
				player.setDirection(Direction.WEST);
				playerMoved = true;
			}
			if(input.buttons[Input.RIGHT])
			{
				player.position.x += 2;
				player.setDirection(Direction.EAST);
				playerMoved = true;
			}
			if(input.buttons[Input.UP])
			{
				player.position.y -= 2;
				player.setDirection(Direction.NORTH);
				playerMoved = true;
			}
			if(input.buttons[Input.DOWN])
			{
				player.position.y += 2;
				player.setDirection(Direction.SOUTH);
				playerMoved = true;
			}
			if(playerMoved){
				screenx = player.position.x - 100;
				screeny = player.position.y - 100;

				if(screenx<0)screenx=0;
				if(screeny<0)screeny=0;
				if(screenx > bottom_layer.getWidth() * Constant.TILE_WIDTH) {
					screenx = bottom_layer.getWidth()*Constant.TILE_WIDTH;
				}
				if(screeny > bottom_layer.getHeight() * Constant.TILE_HEIGHT) {
					screeny = bottom_layer.getHeight() * Constant.TILE_HEIGHT;
				}				
				// TODO: get rid of screenx, screeny
			}
		}
		//Image image=createVolatileImage(imageWidth,imageHeight);
		BufferedImage image = new BufferedImage(imageWidth, imageHeight,
	            BufferedImage.TYPE_INT_ARGB);
		Graphics2D imageGraphics=image.createGraphics();
		imageGraphics.setColor(Color.WHITE);
		imageGraphics.fillRect(0, 0, imageWidth, imageHeight);
		//imageG.setFont(new Font("Courier",10,10));
		//System.out.println("Print tiles here");
		bottom_layer.drawLayer(imageGraphics, imageWidth, imageHeight, screenx, screeny, false);
		top_layer.drawLayer(imageGraphics,imageWidth, imageHeight, screenx, screeny, true);
		editorState.drawMapSelection(imageGraphics, screenx, screeny);
		//imageGraphics.drawImage(characterTileSet.getTileImageFromXY((frameCounter/10) % 3, charDirection), 100, 100, null);
		henry.draw(imageGraphics, frameCounter, screenx, screeny);
		player.draw(imageGraphics, frameCounter, screenx, screeny);
		
		/*
		imageGraphics.drawImage(characterTileSet.getTileImageFromXY((frameCounter/10) % 3 + 3, charDirection), 130, 100, null);
		imageGraphics.drawImage(characterTileSet.getTileImageFromXY((frameCounter/10) % 3 + 6, charDirection), 160, 100, null);
		imageGraphics.drawImage(characterTileSet.getTileImageFromXY((frameCounter/10) % 3 + 9, charDirection), 190, 100, null);

		imageGraphics.drawImage(characterTileSet.getTileImageFromXY((frameCounter/10) % 3, charDirection + 4), 100, 140, null);
		imageGraphics.drawImage(characterTileSet.getTileImageFromXY((frameCounter/10) % 3 + 3, charDirection + 4), 130, 140, null);
		imageGraphics.drawImage(characterTileSet.getTileImageFromXY((frameCounter/10) % 3 + 6, charDirection + 4), 160, 140, null);
		imageGraphics.drawImage(characterTileSet.getTileImageFromXY((frameCounter/10) % 3 + 9, charDirection + 4), 190, 140, null);
		*/
		if(dialogue != null) {
			//System.out.println("Drawing dialogue");
			dialogue.draw(imageGraphics, 
					50, imageHeight-100, 
					imageWidth-100, 2 * Constant.TILE_HEIGHT);
		}else {		
			if(player.getTalkActionCharacter() != null) {
				imageGraphics.setColor(Color.BLUE);
				imageGraphics.fillOval(imageWidth - 80,  10,  60, 40);
				imageGraphics.setColor(Color.WHITE);
				imageGraphics.drawString("Talk", imageWidth - 60, 40);
			}
		}
		
		imageGraphics.dispose();

		g.drawImage(image, 0, 0, imageWidth * 2, imageHeight * 2, 0, 0,imageWidth,imageHeight,null);
		try
		{
			Thread.sleep(10);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		frameCounter++;

		// Do actions and thinking
		player.setTalkActionCharacter(null);
		henry.doAction(worldState);
		henry.doMovement();
		if(frameCounter %10 == 0)
		{
			henry.think(player, worldState, levelState);
		}
		
		for(GameCharacter character: levelState.allCharacters)
		{
			if(player.hasInSight(character.getPosition(), 64))
			{
				if(character.getInteractionPossibilities().contains(InteractionPossibility.TALK))
				{
					player.setTalkActionCharacter(character);
					//System.out.println("Player has in sight!");
				}
			}
		}
	}
	

	public static void main(String[] args)
	{
		Rpg1 rpg1=new Rpg1();
		rpg1.start();
	}
	protected void closing()
	{
		System.out.println("Closing");
	}

	public void saveToFile(String fileName)
	{
		System.out.println("Saving layer to " + fileName);
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(fileName);
			bottom_layer.writeToFileOutputStream(fileOutputStream);
			top_layer.writeToFileOutputStream(fileOutputStream);
			tileSelectorFrame.writeTilePatternsToFileOutputStream(fileOutputStream);
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void readFromFile(String fileName)
	{
		System.out.println("Reading layer from " + fileName);
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(fileName);
			bottom_layer.readFromFileInputStream(fileInputStream);
			top_layer.readFromFileInputStream(fileInputStream);
			tileSelectorFrame.readTilePatternsFromOutputStream(fileInputStream);
			levelStack = new LevelStack(bottom_layer, top_layer);
			dialogStyles.add(new DialogStyle(tileSelectorFrame.tilePatterns.get(4), levelTileSet));
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				Layer layer = mouseDownLeft ? top_layer : bottom_layer;
				levelStack.pushLayers();
				for(int x = 0; x <= bottomright.x - topleft.x; x++)
				{
					for(int y = 0; y <= bottomright.y - topleft.y; y++)
					{
						layer.setTile(x + tileX, y + tileY, layer.getTile(x + topleft.x, y + topleft.y));
					}
				}
				levelStack.popLayersIfNoChange();
			}
		}else
		{
			if(mouseDownLeft || mouseDownRight)
			{
				Layer layer = mouseDownLeft ? top_layer : bottom_layer;
				if(tileSelectorFrame.selectedTilePattern != null)
				{
					levelStack.pushLayers();
					tileSelectorFrame.selectedTilePattern.place(layer, tileX, tileY, true);
					levelStack.popLayersIfNoChange();
				}else
				if(editorState.tileSelection != null)
				{
					Int2d topleft = editorState.tileSelection.topleft;
					Int2d bottomright = editorState.tileSelection.bottomright;
					levelStack.pushLayers();
					for(int x = 0; x <= bottomright.x - topleft.x; x++)
					{
						for(int y = 0; y <= bottomright.y - topleft.y; y++)
						{
							layer.setTile(x + tileX, y + tileY, tileSelectorFrame.tileSet.getTileIndexFromXY(x + topleft.x, y + topleft.y));
						}
					}
					levelStack.popLayersIfNoChange();
				}
			}
		}
	}
}
