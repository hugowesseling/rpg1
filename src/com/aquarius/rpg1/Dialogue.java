package com.aquarius.rpg1;

import java.awt.Graphics2D;

public class Dialogue {
	DialogueBlock startDialogueBlock, currentDialogueBlock;
	DialogStyle dialogStyle;
	
	public Dialogue(DialogueBlock startDialogueBlock, DialogStyle dialogStyle) {
		this.startDialogueBlock = startDialogueBlock;
		this.currentDialogueBlock = startDialogueBlock;
		if(dialogStyle == null) {
			dialogStyle = Resources.dialogStyles.get(0);
			System.out.println("Setting dialogstyle to:" + dialogStyle);
		}
		this.dialogStyle = dialogStyle;
	}

	public boolean confirm(LevelState levelState, Player player) {
		System.out.println("Dialog.confirm");
		currentDialogueBlock = currentDialogueBlock.nextNode;
		if(currentDialogueBlock == null) {
			currentDialogueBlock = startDialogueBlock;
			return false;
		}else {
			currentDialogueBlock.doAction(levelState, player);
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
		int estimatedTextWidth = 5 * currentDialogueBlock.text.length();
		//System.out.println("Showing dialogue: " + currentDialogueBlock.text);
		graphics.drawString(currentDialogueBlock.text, x + w/2 - estimatedTextWidth/2, y + h/2 + 10);

	}
	
}
