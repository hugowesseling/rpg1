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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class TileSelectorFrame extends Component implements MouseListener, MouseMotionListener
{
	private enum EditMode {
	    SELECT_TILES, TILE_PATTERN_SELECTION, TILE_PATTERN_EDIT, TILE_PATTERN_COLORING, TILE_COLLISION_EDIT 
	}
	private static final long serialVersionUID = 1L;
	private int imageWidth;
	private int imageHeight;
	private EditorState editorState;
	public TileSet tileSet;
	private boolean mouseDownLeft;
	private Int2d mouseStart = null;
    public Vector<TilePattern> tilePatterns;
    private boolean[][] tileCollision;
    private EditMode editMode;
	public TilePattern selectedTilePattern = null;
	private static int drawCounter = 0;

    public TileSelectorFrame(String name, TileSet tileSet, EditorState editorState)
	{
		this.tileSet = tileSet;
		tileCollision = new boolean[tileSet.tiles.length][tileSet.tiles[0].length];
		this.editorState = editorState;
		imageWidth = tileSet.tiles.length * Constant.TILE_WIDTH;
		imageHeight = tileSet.tiles[0].length * Constant.TILE_HEIGHT;
		tilePatterns = new Vector<TilePattern>();
		editMode = EditMode.SELECT_TILES;
		
		setSize(imageWidth * 2,imageHeight * 2);
		setPreferredSize(new Dimension(imageWidth * 2,imageHeight * 2));
		JFrame jFrame=new JFrame(name);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu optionsMenu = new JMenu("Options");
		JMenuItem editTilePatterngMenuItem = new JMenuItem("Edit Tile Patterns");
		optionsMenu.add(editTilePatterngMenuItem);
		menuBar.add(optionsMenu);

		JMenu tilePatternSwitchMenu = new JMenu("");
		tilePatternSwitchMenu.setIcon(new ImageIcon("star_icon.png"));
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
	            } 
	          });
		menuBar.add(tilePatternColoringSwitchMenu);
		
		JMenu collisionSwitchMenu = new JMenu("");
		collisionSwitchMenu.setIcon(new ImageIcon("red_square_icon.png"));
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
	            } 
	          });
		menuBar.add(collisionSwitchMenu);

		
		jFrame.setJMenuBar(menuBar);
		
		
		jFrame.add(this);
		jFrame.pack();
		jFrame.setVisible(true);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void paint(Graphics g)
	{
		Image image=createVolatileImage(imageWidth, imageHeight);
		Graphics2D imageG=(Graphics2D) image.getGraphics();
		float alpha = 1f;
		drawCounter = (drawCounter+1) % 100;
		alpha = (float)(Math.sin(Math.PI*drawCounter/50)) * 0.25f + 0.75f;

		int tileswidth = imageWidth/16+1;
		int tilesheight = imageHeight/16+1;
		for(int y=0;y<tilesheight;y++)
		{
			if(y >= 0 && y < tileSet.tiles[0].length)
			{
				for(int x=0;x<tileswidth;x++)
				{
					if(x >= 0 && x< tileSet.tiles.length)
					{
						imageG.drawImage(tileSet.tiles[x][y], x * Constant.TILE_WIDTH, y * Constant.TILE_HEIGHT, null);
						if(editMode == EditMode.TILE_PATTERN_SELECTION)
						{
							imageG.setColor(Color.GRAY);
							imageG.drawRect(x * Constant.TILE_WIDTH, y * Constant.TILE_HEIGHT, Constant.TILE_WIDTH - 1, Constant.TILE_HEIGHT - 1);
						}else
						if(editMode == EditMode.TILE_COLLISION_EDIT)
						{
							imageG.setColor(tileCollision[x][y] ? Color.RED : Color.GREEN);
							imageG.drawRect(x * Constant.TILE_WIDTH, y * Constant.TILE_HEIGHT, Constant.TILE_WIDTH - 1, Constant.TILE_HEIGHT - 1);
						}
					}
				}
			}
		}
		if(editMode == EditMode.TILE_PATTERN_SELECTION)
		{
			imageG.setColor(Color.RED);
			for(TilePattern tilePattern:tilePatterns)
			{
				tilePattern.draw(imageG);
			}
		}
		if(editMode == EditMode.TILE_PATTERN_EDIT)
		{
			imageG.setColor(Color.RED);
			selectedTilePattern.draw(imageG);
		}
		if(editMode == EditMode.TILE_PATTERN_COLORING)
		{
			Composite backupComposite = imageG.getComposite();
			imageG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			selectedTilePattern.drawColorHash(imageG);
			imageG.setComposite(backupComposite);
		}
		editorState.drawTileSelection(imageG, 0, 0);
		imageG.dispose();		
		g.drawImage(image,0,0,imageWidth * 2,imageHeight * 2,0,0,imageWidth,imageHeight,null);
	}

	public void updateFrame()
	{
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	private Int2d getMousePixelLocation(MouseEvent mouseEvent)
	{
		return new Int2d(mouseEvent.getX() / 2,
						 mouseEvent.getY() / 2);
	}
	@Override
	public void mousePressed(MouseEvent mouseEvent)
	{
		mouseDownLeft = true;
		mouseStart  = getMousePixelLocation(mouseEvent);
		int tileX = mouseStart.x / Constant.TILE_WIDTH;
		int tileY = mouseStart.y / Constant.TILE_HEIGHT;


		if((mouseEvent.getModifiers() & ActionEvent.CTRL_MASK) != 0)
		{
			if(selectedTilePattern != null)
			{
				System.out.println("Cloning current tilePattern");
				// Copy selected tilepattern translated from top-left most to mouseStart
				tilePatterns.add(selectedTilePattern.cloneTranslated(tileX, tileY));
			}
		}else
		{
			if(editMode == EditMode.TILE_PATTERN_SELECTION)
			{
				TilePattern tilePattern = getTilePatternFromTile(tileX, tileY);
				if(tilePattern == null)
				{
					tilePattern = new TilePattern();
					tilePattern.addTile(new TilePatternTile(tileX, tileY));
					tilePatterns.add(tilePattern);
				}
				selectedTilePattern  = tilePattern; 
				editMode = EditMode.TILE_PATTERN_EDIT;
			}else
			if(editMode == EditMode.TILE_PATTERN_EDIT)
			{
				if(selectedTilePattern.isTileInTilePattern(tileX, tileY))
					selectedTilePattern.removeTileFromPattern(tileX,tileY);
				else
					selectedTilePattern.addTile(new TilePatternTile(tileX, tileY));
			}else
			if(editMode == EditMode.TILE_PATTERN_COLORING)
			{
				int tileThirdX = (mouseStart.x % Constant.TILE_WIDTH) * 3 / Constant.TILE_WIDTH;
				int tileThirdY = (mouseStart.y % Constant.TILE_HEIGHT) * 3 / Constant.TILE_HEIGHT;
				System.out.println("Third position: " + tileThirdX + "," + tileThirdY);
				selectedTilePattern.changeColor(tileX, tileY, tileThirdX, tileThirdY);
			}
			mouseDragged(mouseEvent);
		}
	}

	private TilePattern getTilePatternFromTile(int tileX, int tileY)
	{
		for(TilePattern tilePattern:tilePatterns)
		{
			if(tilePattern.isTileInTilePattern(tileX,tileY))
			{
				return tilePattern;
			}
		}
		return null;
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent)
	{
		mouseDownLeft = false;
	}

	@Override
	public void mouseMoved(MouseEvent mouseEvent)
	{
	}

	@Override
	public void mouseDragged(MouseEvent mouseEvent)
	{
		//System.out.println("Mouse dragged");
		if(mouseDownLeft)
		{
			Int2d mouseLocation = getMousePixelLocation(mouseEvent);
			int tileX = mouseLocation.x / Constant.TILE_WIDTH;
			int tileY = mouseLocation.y / Constant.TILE_HEIGHT;
			if(editMode == EditMode.TILE_COLLISION_EDIT)
			{
				if(tileX >= 0 && tileX < tileCollision.length && tileY >= 0 && tileY < tileCollision[0].length)
					tileCollision[tileX][tileY] = !tileCollision[tileX][tileY]; 
			}else		
			if(editMode == EditMode.SELECT_TILES)
			{
				int tileX1 = mouseStart.x / Constant.TILE_WIDTH;
				int tileY1 = mouseStart.y / Constant.TILE_HEIGHT;
				
				editorState.setTileSelection(tileX, tileY, tileX1, tileY1);
				System.out.println("Topleft index: " + tileSet.getTileIndexFromXY(editorState.tileSelection.topleft.x, editorState.tileSelection.topleft.y));
			}
		}
	}

	public void writeTilePatternsToFileOutputStream(FileOutputStream fileOutputStream)
	{
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new  ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(tilePatterns);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void readTilePatternsFromOutputStream(FileInputStream fileInputStream)
	{
		try
		{
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			tilePatterns = (Vector<TilePattern>) objectInputStream.readObject();
		}catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}