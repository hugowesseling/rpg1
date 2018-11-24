package com.aquarius.rpg1;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageChoosingLabel extends JLabel {
	private static final long serialVersionUID = 5210950489506926249L;
	private int tileIndex = -1;

	public ImageChoosingLabel(String text, Frame frame, ActionListener actionListener) {
		super(text);
		setIcon(new ImageIcon(Resources.getTileImageFromIndex(0)));
		this.addMouseListener(new MouseAdapter() {
		    //@Override
		    public void mousePressed(MouseEvent e) {
		    	TileIndexSelector tileIndexSelector = new TileIndexSelector(frame);
		    	tileIndexSelector.setVisible(true);
		    	setTileIndex(tileIndexSelector.tileIndex);
		    	if(actionListener != null)
		    		actionListener.actionPerformed(new ActionEvent(this, tileIndex, "set tileIndex"));
		    }
		});
	}

	public int getTileIndex() {
		return tileIndex;
	}

	public void setTileIndex(int tileIndex) {
    	this.tileIndex = tileIndex;
    	setIcon(new ImageIcon(Resources.getTileImageFromIndex(tileIndex)));
	}
	
}
