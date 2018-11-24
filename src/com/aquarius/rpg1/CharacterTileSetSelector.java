package com.aquarius.rpg1;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JMenuBar;

public class CharacterTileSetSelector extends JDialog implements MouseListener {

	private final class ComponentExtension extends Component {
		private static final long serialVersionUID = -9113490174840163895L;

		@Override
		public void paint(Graphics g)
		{
			Direction direction = Direction.SOUTH;
			int frame = 0;
			
			int x=0;
			int y=0;
			for(CharacterTileSet characterTileSet:Resources.characterTileSets) {
				characterTileSet.drawTopLeft((Graphics2D) g, x*TILE_WIDTH,  y*TILE_HEIGHT, direction, frame);
				x++;
				if(x>=this.getSize().getWidth() / TILE_WIDTH) {
					x=0;
					y++;
				}
			}
		}
	}
	private static final int TILE_WIDTH = 64;
	private static final int TILE_HEIGHT = 64;
	private static final long serialVersionUID = -7539539217604009302L;
	private CharacterTileSetSelector self;
	private int characterTileSetIndex;
	private ComponentExtension drawingComponent;

	public CharacterTileSetSelector(Frame owner) {
		super(owner, "CharacterTileSet Selector", true);
		self = this;
		setCharacterTileSetIndex(0);

		drawingComponent = new ComponentExtension();
		this.add(drawingComponent);
		drawingComponent.setPreferredSize(new Dimension(300,300));
		this.pack();
		drawingComponent.addMouseListener(this);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

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
		return new Int2d(mouseEvent.getX(),
						 mouseEvent.getY());
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		Int2d mouseStart = getMousePixelLocation(mouseEvent);
		int tileX = mouseStart.x / TILE_WIDTH;
		int tileY = mouseStart.y / TILE_HEIGHT;
		int index = tileY * ((int)(this.getSize().getWidth()) / TILE_WIDTH + 1) + tileX;
		System.out.println("CharacterTileSetSelector: tile:" + tileX + "," + tileY + ", index:" + index);
		setCharacterTileSetIndex(index);
		setVisible(false);
	}

	private void setCharacterTileSetIndex(int index) {
		if(index >= Resources.characterTileSets.size())
			index = Resources.characterTileSets.size()-1;
		if(index < 0)
			index = 0;
		characterTileSetIndex = index;
	}
	

	public int getCharacterTileSetIndex() {
		return characterTileSetIndex;
	}
	
	public CharacterTileSet getCharacterTileSet() {
		return Resources.characterTileSets.get(characterTileSetIndex);
	}
	

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
