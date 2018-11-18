package com.aquarius.rpg1;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuBar;

public class TileIndexSelector extends JDialog implements MouseListener {

	private final class ComponentExtension extends Component {
		@Override
		public void paint(Graphics g)
		{
			//Image image=createVolatileImage(imageWidth, imageHeight);
			//Graphics2D imageG=(Graphics2D) image.getGraphics();

			for(int y=0;y<currentTileSet.tiles[0].length;y++)
			{
				for(int x=0;x<currentTileSet.tiles.length;x++)
				{
					g.drawImage(currentTileSet.tiles[x][y], x * Constant.TILE_WIDTH, y * Constant.TILE_HEIGHT, null);
				}
			}
			//imageG.dispose();		
			//g.drawImage(image,0,0,imageWidth,imageHeight,0,0,imageWidth,imageHeight,null);
		}
	}
	private static final long serialVersionUID = -7539539217604009302L;
	private TileSet currentTileSet;
	private int imageWidth = 0;
	private int imageHeight = 0;
	private TileIndexSelector self;
	public int tileIndex;
	private ComponentExtension drawingComponent;

	public TileIndexSelector(Frame owner) {
		super(owner, "Tile Selector", true);
		self = this;

		JMenuBar menuBar = new JMenuBar();
		String[] tileSetStrings = Resources.getTileSetNames();
		JComboBox<String> tileSetCombobox = new JComboBox<String>(tileSetStrings);
		tileSetCombobox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				JComboBox<String> comboBox = (JComboBox<String>) ae.getSource();
				int index = comboBox.getSelectedIndex();
				System.out.println("Selected tileset index: " + index);
				setCurrentTileSet(index);
				self.repaint();
			}
		});
		menuBar.add(tileSetCombobox);
		this.setJMenuBar(menuBar);
		drawingComponent = new ComponentExtension();
		this.add(drawingComponent);
		setCurrentTileSet(0);
		drawingComponent.addMouseListener(this);
	}
	private void setCurrentTileSet(int index) {
		currentTileSet = Resources.levelTileSets[index];
		imageWidth = currentTileSet.tiles.length * Constant.TILE_WIDTH;
		imageHeight = currentTileSet.tiles[0].length * Constant.TILE_HEIGHT;
		drawingComponent.setPreferredSize(new Dimension(imageWidth,imageHeight));
		this.pack();
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
		int tileX = mouseStart.x / Constant.TILE_WIDTH;
		int tileY = mouseStart.y / Constant.TILE_HEIGHT;
		tileIndex = currentTileSet.index*65536 + tileY*256 + tileX;
		setVisible(false);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
