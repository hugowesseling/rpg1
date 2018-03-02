package com.aquarius.rpg1;

import java.awt.Graphics2D;

public class Dialogue {
	DialogueBlock startDialogueBlock, currentDialogueBlock;
	DialogStyle dialogStyle;
	
	public Dialogue(DialogueBlock startDialogueBlock, DialogStyle dialogStyle) {
		super();
		this.startDialogueBlock = startDialogueBlock;
		this.currentDialogueBlock = startDialogueBlock;
		this.dialogStyle = dialogStyle;
	}

	public boolean confirm() {
		System.out.println("Dialog.confirm");
		currentDialogueBlock = currentDialogueBlock.nextNode;
		if(currentDialogueBlock == null) {
			currentDialogueBlock = startDialogueBlock;
			return false;
		}else {
			return true;
		}
		
	}

	public void up() {
		// TODO Auto-generated method stub
		
	}

	public void down() {
		// TODO Auto-generated method stub
		
	}

	public void draw(Graphics2D graphics, int x, int y, int w, int h) {
		dialogStyle.draw(graphics, x, y, w, h);
		int estimatedTextWidth = 10 * currentDialogueBlock.text.length();
		System.out.println("Showing dialogue: " + currentDialogueBlock.text);
		graphics.drawString(currentDialogueBlock.text, x + w/2 - estimatedTextWidth/2, y + h/2 + 10);

	}
	
}
