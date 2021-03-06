package com.aquarius.rpg1;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JDialog;

public class CharacterTileSetSelector extends JDialog implements MouseListener {

	private final class ComponentExtension extends Component {
		private static final long serialVersionUID = -9113490174840163895L;

		@Override
		public void paint(Graphics g)
		{
			Graphics2D g2d = (Graphics2D) g;
			int frame = 0;
			
			int x=0;
			int y=0;
			int screenWidth = (int) this.getSize().getWidth();
			System.out.println("CharacterTileSetSelector: screenWidth: "+screenWidth);

			for(CharacterTileSet characterTileSet:Resources.characterTileSets) {
				characterTileSet.drawTopLeft(g2d, x*TILE_WIDTH,  y*TILE_HEIGHT, Direction.SOUTH, frame);
				Image image = characterTileSet.getImage(Direction.SOUTH, frame);
				g2d.drawRect(x*TILE_WIDTH,  y*TILE_HEIGHT, image.getWidth(null),  image.getHeight(null));
				x++;
				if(x*TILE_WIDTH+TILE_WIDTH >= screenWidth) {
					x=0;
					y++;
				}
			}
		}
	}
	private static final int TILE_WIDTH = 64;
	private static final int TILE_HEIGHT = 64;
	private static final long serialVersionUID = -7539539217604009302L;
	private int characterTileSetIndex;
	private ComponentExtension drawingComponent;

	public CharacterTileSetSelector(Frame owner) {
		super(owner, "CharacterTileSet Selector", true);
		setCharacterTileSetIndex(0);

		drawingComponent = new ComponentExtension();
		this.add(drawingComponent);
		drawingComponent.setPreferredSize(new Dimension(710,710));
		this.pack();
		drawingComponent.addMouseListener(this);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	private Int2d getMousePixelLocation(MouseEvent mouseEvent)
	{
		return new Int2d(mouseEvent.getX(),
						 mouseEvent.getY());
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		int screenWidth = (int) drawingComponent.getSize().getWidth();
		System.out.println("CharacterTileSetSelector: screenWidth: "+screenWidth);

		Int2d mouseStart = getMousePixelLocation(mouseEvent);
		int tileX = mouseStart.x / TILE_WIDTH;
		int tileY = mouseStart.y / TILE_HEIGHT;
		int index = tileY * (screenWidth / TILE_WIDTH ) + tileX;
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
	}
}
