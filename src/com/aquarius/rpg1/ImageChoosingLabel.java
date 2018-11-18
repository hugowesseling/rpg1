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
	protected int tileIndex = -1;

	public ImageChoosingLabel(String text, Frame frame, ActionListener al) {
		super(text);
		ImageChoosingLabel self = this;
		self.setIcon(new ImageIcon(Resources.getTileImageFromIndex(0)));
		this.addMouseListener(new MouseAdapter() {
		    //@Override
		    public void mousePressed(MouseEvent e) {
		    	TileIndexSelector tileIndexSelector = new TileIndexSelector(frame);
		    	tileIndexSelector.setVisible(true);
		    	tileIndex = tileIndexSelector.tileIndex;
		    	self.setIcon(new ImageIcon(Resources.getTileImageFromIndex(tileIndex)));
		    	if(al != null)
		    		al.actionPerformed(new ActionEvent(self, tileIndex, "set tileIndex"));
		    }
		});
	}
	
}
