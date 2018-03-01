package com.aquarius.rpg1;

import java.awt.Graphics2D;

public class Dialogue {
	DialogueBlock dialogueBlock;
	DialogStyle dialogStyle;
	
	public Dialogue(DialogueBlock dialogueBlock, DialogStyle dialogStyle) {
		super();
		this.dialogueBlock = dialogueBlock;
		this.dialogStyle = dialogStyle;
	}

	public void confirm() {
		System.out.println("Dialog.confirm");
		
	}

	public void up() {
		// TODO Auto-generated method stub
		
	}

	public void down() {
		// TODO Auto-generated method stub
		
	}

	public void draw(Graphics2D graphics, int x, int y, int w, int h) {
		dialogStyle.draw(graphics, x, y, w, h);
		
	}
	
}
