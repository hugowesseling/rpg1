package com.aquarius.rpg1;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class CharacterTileSetChoosingLabel extends JLabel {
	private static final long serialVersionUID = 5210950489506926249L;
	private int characterTileSetIndex = 0;

	public CharacterTileSetChoosingLabel(String text, Frame frame, ActionListener actionListener) {
		super(text);
		setIcon(new ImageIcon(Resources.characterTileSets.get(0).getImage()));
		this.addMouseListener(new MouseAdapter() {
		    //@Override
		    public void mousePressed(MouseEvent e) {
		    	CharacterTileSetSelector characterTileSetSelector = new CharacterTileSetSelector(frame);
		    	characterTileSetSelector.setVisible(true);
		    	setCharacterTileSetIndex(characterTileSetSelector.getCharacterTileSetIndex());
		    	if(actionListener != null)
		    		actionListener.actionPerformed(new ActionEvent(this, characterTileSetSelector.getCharacterTileSetIndex(), "set characterTileSet"));
		    }
		});
	}

	public int getCharacterTileSetIndex() {
		return characterTileSetIndex;
	}

	public void setCharacterTileSetIndex(int characterTileSetIndex) {
		this.characterTileSetIndex = characterTileSetIndex;
		CharacterTileSet characterTileSet = Resources.characterTileSets.get(characterTileSetIndex);
    	setIcon(new ImageIcon(characterTileSet.getImage()));
	}

	
}
