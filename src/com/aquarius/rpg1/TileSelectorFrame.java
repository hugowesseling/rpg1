package com.aquarius.rpg1;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/*
TileSelectorFrame:
Things that belong together:
- imageWidth, imageHeight (can be set upon switching)
- tileSet
- tileCollision (should be a part of a tile set)
- selectedTilePattern (reset upon switching)

 */

public class TileSelectorFrame extends Component implements MouseListener, MouseMotionListener
{
	private enum EditMode {
	    SELECT_TILES, TILE_PATTERN_SELECTION, TILE_PATTERN_EDIT, TILE_PATTERN_COLORING, TILE_COLLISION_EDIT, LAYER_HEIGHT_EDIT, TILE_COVER_EDIT 
	}
	private static final long serialVersionUID = 1L;
	private int imageWidth;
	private int imageHeight;
	private EditorState editorState;
	private boolean mouseDownLeft = false, mouseDownRight = false, mouseDownMid = false;
	private Int2d mouseStart = null;
    private EditMode editMode;
	public TilePattern selectedTilePattern = null;
	TileSet currentTileSet;
	private JFrame jFrame;
	private static int drawCounter = 0;
	private boolean redrawNeeded = true;
	private TileIndexChoosingLabel insideChoosingLabel;
	private TileIndexChoosingLabel outsideChoosingLabel;

    public TileSelectorFrame(String name, EditorState editorState)
	{
		this.editorState = editorState;
		editMode = EditMode.SELECT_TILES;
		
		jFrame=new JFrame(name);
		
		JMenuBar menuBar = new JMenuBar();
		String[] tileSetStrings = Resources.getTileSetNames();
		JComboBox<String> tileSetCombobox = new JComboBox<String>(tileSetStrings);
		tileSetCombobox.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent ae) {
				JComboBox<String> comboBox = (JComboBox<String>) ae.getSource();
				int index = comboBox.getSelectedIndex();
				System.out.println("Selected tileset index: " + index);
				setCurrentTileSet(index);
				redrawNeeded = true;
			}
		});
		tileSetCombobox.setMaximumRowCount(50);
		menuBar.add(tileSetCombobox);
		
		JMenu tilePatternSwitchMenu = new JMenu("Patt");
		//tilePatternSwitchMenu.setIcon(new ImageIcon("star_icon.png"));
		tilePatternSwitchMenu.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				  if(editMode != EditMode.TILE_PATTERN_SELECTION)
				  {
					  editMode = EditMode.TILE_PATTERN_SELECTION;
				  }else
				  {
					  editMode = EditMode.SELECT_TILES;
					  selectedTilePattern = null;
				  }
	        	  System.out.println("tileSelectionModeActive: " + editMode);
	        	  redrawNeeded = true;
	            } 
	          });
		menuBar.add(tilePatternSwitchMenu);
		
		JMenu tilePatternColoringSwitchMenu = new JMenu("");
		tilePatternColoringSwitchMenu.setIcon(new ImageIcon("colors_icon.png"));
		tilePatternColoringSwitchMenu.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				  if(editMode == EditMode.TILE_PATTERN_EDIT)
				  {
					  editMode = EditMode.TILE_PATTERN_COLORING;
				  }else
				  if(editMode == EditMode.TILE_PATTERN_COLORING)
				  {
					  editMode = EditMode.TILE_PATTERN_EDIT;
				  }
	        	  System.out.println("tileSelectionModeActive: " + editMode);
	        	  redrawNeeded = true;
	            } 
	          });
		menuBar.add(tilePatternColoringSwitchMenu);
		
		JMenu collisionSwitchMenu = new JMenu("Coll");
		//collisionSwitchMenu.setIcon(new ImageIcon("red_square_icon.png"));
		collisionSwitchMenu.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				  if(editMode != EditMode.TILE_COLLISION_EDIT)
				  {
					  editMode = EditMode.TILE_COLLISION_EDIT;
				  }else
				  if(editMode == EditMode.TILE_COLLISION_EDIT)
				  {
					  editMode = EditMode.SELECT_TILES;
				  }
	        	  System.out.println("tileSelectionModeActive: " + editMode);
	        	  redrawNeeded = true;
	            } 
	          });
		menuBar.add(collisionSwitchMenu);

		JMenu coverSwitchMenu = new JMenu("Cover");
		//collisionSwitchMenu.setIcon(new ImageIcon("red_square_icon.png"));
		coverSwitchMenu.addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				  if(editMode != EditMode.TILE_COVER_EDIT)
				  {
					  editMode = EditMode.TILE_COVER_EDIT;
				  }else
				  if(editMode == EditMode.TILE_COVER_EDIT)
				  {
					  editMode = EditMode.SELECT_TILES;
				  }
	        	  System.out.println("tileSelectionModeActive: " + editMode);
	        	  redrawNeeded = true;
	            } 
	          });
		menuBar.add(coverSwitchMenu);

		JMenu layerSwitchMenu = new JMenu("Layer");
		//layerSwitchMenu .setIcon(new ImageIcon("red_square_icon.png"));
		layerSwitchMenu .addMouseListener(new MouseAdapter() { 
			public void mousePressed(MouseEvent me) {
				  if(editMode != EditMode.LAYER_HEIGHT_EDIT)
				  {
					  editMode = EditMode.LAYER_HEIGHT_EDIT;
				  }else
				  if(editMode == EditMode.LAYER_HEIGHT_EDIT)
				  {
					  editMode = EditMode.SELECT_TILES;
				  }
	        	  System.out.println("tileSelectionModeActive: " + editMode);
	        	  redrawNeeded = true;
	            } 
	          });
		menuBar.add(layerSwitchMenu );		
		
		JLabel tilePatternsLabel = new JLabel("TilePattern:");
		menuBar.add(tilePatternsLabel);
		insideChoosingLabel = new TileIndexChoosingLabel("inside", jFrame, (ae)->{ if(selectedTilePattern!=null)selectedTilePattern.insideTileIndex =  ae.getID(); }); 
		menuBar.add(insideChoosingLabel);
		outsideChoosingLabel = new TileIndexChoosingLabel("outside", jFrame, (ae)->{ if(selectedTilePattern!=null)selectedTilePattern.outsideTileIndex = ae.getID(); });
		menuBar.add(outsideChoosingLabel);
		jFrame.setJMenuBar(menuBar);

		jFrame.add(this);
		setCurrentTileSet(1);
		jFrame.setVisible(true);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	private void setCurrentTileSet(int index) {
		currentTileSet = Resources.levelTileSets[index];
		imageWidth = currentTileSet.tiles.length * Constant.TILE_WIDTH;
		imageHeight = currentTileSet.tiles[0].length * Constant.TILE_HEIGHT;
		setSize(imageWidth * 2,imageHeight * 2);
		setPreferredSize(new Dimension(imageWidth * 2,imageHeight * 2));
		jFrame.pack();
	}

	@Override
	public void paint(Graphics g)
	{
		Image image=createVolatileImage(imageWidth, imageHeight);
		Graphics2D imageG=(Graphics2D) image.getGraphics();
		drawCounter = (drawCounter+1) % 100;

		int tileswidth = imageWidth/16+1;
		int tilesheight = imageHeight/16+1;
		for(int y=0;y<tilesheight;y++)
		{
			if(y >= 0 && y < currentTileSet.tiles[0].length)
			{
				for(int x=0;x<tileswidth;x++)
				{
					if(x >= 0 && x< currentTileSet.tiles.length)
					{
						imageG.drawImage(currentTileSet.tiles[x][y], x * Constant.TILE_WIDTH, y * Constant.TILE_HEIGHT, null);
						switch(editMode) {
						case LAYER_HEIGHT_EDIT:
							imageG.setColor(currentTileSet.layerHeight[x][y] ? Color.BLUE : Color.ORANGE);
							imageG.drawRect(x * Constant.TILE_WIDTH, y * Constant.TILE_HEIGHT, Constant.TILE_WIDTH - 1, Constant.TILE_HEIGHT - 1);
							break;
						case SELECT_TILES:
							break;
						case TILE_COLLISION_EDIT:
							imageG.setColor(currentTileSet.tileCollision[x][y] ? Color.RED : Color.GREEN);
							imageG.drawRect(x * Constant.TILE_WIDTH, y * Constant.TILE_HEIGHT, Constant.TILE_WIDTH - 1, Constant.TILE_HEIGHT - 1);
							break;
						case TILE_COVER_EDIT:
							int coverage = currentTileSet.getCoverageForXYUnsafe(x,y);
							if(coverage<0)coverage = 0;
							if(coverage>255)coverage = 255;
							imageG.setColor(new Color(coverage, 255-coverage,0));
							imageG.fillRect(x * Constant.TILE_WIDTH, y * Constant.TILE_HEIGHT, Constant.TILE_WIDTH - 1, Constant.TILE_HEIGHT - 1);
							break;
						case TILE_PATTERN_COLORING:
							break;
						case TILE_PATTERN_EDIT:
							break;
						case TILE_PATTERN_SELECTION:
							imageG.setColor(Color.GRAY);
							imageG.drawRect(x * Constant.TILE_WIDTH, y * Constant.TILE_HEIGHT, Constant.TILE_WIDTH - 1, Constant.TILE_HEIGHT - 1);
							break;
						default:
							break;
						}
					}
				}
			}
		}
		if(editMode == EditMode.TILE_PATTERN_SELECTION)
		{
			imageG.setColor(Color.RED);
			for(TilePattern tilePattern:currentTileSet.tilePatterns)
			{
				tilePattern.draw(imageG, currentTileSet.index);
			}
		}
		if(editMode == EditMode.TILE_PATTERN_EDIT)
		{
			imageG.setColor(Color.RED);
			selectedTilePattern.draw(imageG, currentTileSet.index);
		}
		if(editMode == EditMode.TILE_PATTERN_COLORING)
		{
			Composite backupComposite = imageG.getComposite();
			imageG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); //alpha));
			selectedTilePattern.drawColorHash(imageG);
			imageG.setComposite(backupComposite);
		}
		editorState.drawTileSelection(imageG, 0, 0);
		imageG.dispose();		
		g.drawImage(image,0,0,imageWidth * 2,imageHeight * 2,0,0,imageWidth,imageHeight,null);
	}

	public void updateFrame()
	{
		if(redrawNeeded)
		{
			redrawNeeded = false;
			repaint();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	private Int2d getMousePixelLocation(MouseEvent mouseEvent)
	{
		return new Int2d(mouseEvent.getX() / 2,
						 mouseEvent.getY() / 2);
	}
	@Override
	public void mousePressed(MouseEvent mouseEvent)
	{
		if(mouseEvent.getButton() == MouseEvent.BUTTON1)
			mouseDownLeft = true;
		if(mouseEvent.getButton() == MouseEvent.BUTTON2)
			mouseDownMid = true;
		if(mouseEvent.getButton() == MouseEvent.BUTTON3)
			mouseDownRight = true;
		mouseStart  = getMousePixelLocation(mouseEvent);
		int tileX = mouseStart.x / Constant.TILE_WIDTH;
		int tileY = mouseStart.y / Constant.TILE_HEIGHT;
		int tileIndex = currentTileSet.index*65536 + tileY*256 + tileX;


		if((mouseEvent.getModifiers() & ActionEvent.CTRL_MASK) != 0)
		{
			if(selectedTilePattern != null)
			{
				System.out.println("Cloning current tilePattern");
				// Copy selected tilepattern translated from top-left most to mouseStart
				currentTileSet.tilePatterns.add(selectedTilePattern.cloneTranslated(tileX, tileY));
			}
		}else
		{
			if(editMode == EditMode.TILE_PATTERN_SELECTION)
			{
				TilePattern tilePattern = currentTileSet.getTilePatternFromTile(tileIndex);
				if(tilePattern == null)
				{
					tilePattern = new TilePattern();
					tilePattern.addTile(new TilePatternTile(tileIndex));
					currentTileSet.tilePatterns.add(tilePattern);
				}
				selectedTilePattern  = tilePattern;
				insideChoosingLabel.setTileIndex(selectedTilePattern.insideTileIndex);
				outsideChoosingLabel.setTileIndex(selectedTilePattern.outsideTileIndex);
				for(int i=0;i<currentTileSet.tilePatterns.size();i++)
					if(currentTileSet.tilePatterns.get(i)==tilePattern)
						System.out.println("Selected tile pattern: "+i);
				editMode = EditMode.TILE_PATTERN_EDIT;
			}else
			if(editMode == EditMode.TILE_PATTERN_EDIT)
			{
				if(selectedTilePattern.isTileInTilePattern(tileIndex))
					selectedTilePattern.removeTileFromPattern(tileIndex);
				else
					selectedTilePattern.addTile(new TilePatternTile(tileIndex));
			}
			mouseDragged(mouseEvent);
		}
  	  	redrawNeeded = true;
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent)
	{
		if(mouseEvent.getButton() == MouseEvent.BUTTON1)
			mouseDownLeft = false;
		if(mouseEvent.getButton() == MouseEvent.BUTTON2)
			mouseDownMid = false;
		if(mouseEvent.getButton() == MouseEvent.BUTTON3)
			mouseDownRight = false;
	}

	@Override
	public void mouseMoved(MouseEvent mouseEvent)
	{
	}

	@Override
	public void mouseDragged(MouseEvent mouseEvent)
	{
		//System.out.println("Mouse dragged");
		if(mouseDownLeft || mouseDownRight || mouseDownMid)
		{
			Int2d mouseLocation = getMousePixelLocation(mouseEvent);
			int tileX = mouseLocation.x / Constant.TILE_WIDTH;
			int tileY = mouseLocation.y / Constant.TILE_HEIGHT;
			int tileIndex = currentTileSet.index*65536 + tileY*256 + tileX;
			if(editMode == EditMode.TILE_COLLISION_EDIT)
			{
				boolean newState = mouseDownLeft ? true : false;
				if(tileX >= 0 &&
				   tileX < currentTileSet.tileCollision.length &&
				   tileY >= 0 &&
				   tileY < currentTileSet.tileCollision[0].length)
					currentTileSet.tileCollision[tileX][tileY] = newState; 
			}else
			if(editMode == EditMode.LAYER_HEIGHT_EDIT)
			{
				boolean newState = mouseDownLeft ? true : false;
				if(tileX >= 0 &&
				   tileX < currentTileSet.layerHeight.length &&
				   tileY >= 0 &&
				   tileY < currentTileSet.layerHeight[0].length)
					currentTileSet.layerHeight[tileX][tileY] = newState; 
			}else
			if(editMode == EditMode.SELECT_TILES)
			{
				int tileX1 = mouseStart.x / Constant.TILE_WIDTH;
				int tileY1 = mouseStart.y / Constant.TILE_HEIGHT;
				
				editorState.setTileSelection(tileX, tileY, tileX1, tileY1);
				System.out.println("Topleft index: " + currentTileSet.getTileIndexFromXY(editorState.tileSelection.topleft.x, editorState.tileSelection.topleft.y));
			}else
			if(editMode == EditMode.TILE_PATTERN_COLORING)
			{
				int newState = mouseDownLeft ?  TilePatternTile.OCCUPIED : mouseDownMid ? TilePatternTile.EITHER : TilePatternTile.EMPTY;
				int tileThirdX = (mouseLocation.x % Constant.TILE_WIDTH) * 3 / Constant.TILE_WIDTH;
				int tileThirdY = (mouseLocation.y % Constant.TILE_HEIGHT) * 3 / Constant.TILE_HEIGHT;
				System.out.println("Third position: " + tileIndex + ":" + tileThirdX + "," + tileThirdY);
				
				selectedTilePattern.changeColor(tileIndex, tileThirdX, tileThirdY, newState);
			}
		}
  	  	redrawNeeded = true;
	}
}
