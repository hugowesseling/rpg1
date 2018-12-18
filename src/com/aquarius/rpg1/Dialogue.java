package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.awt.Image;

import com.aquarius.rpg1.AudioSystemPlayer.RandomSound;

public class Dialogue {
	DialogueBlock startDialogueBlock, currentDialogueBlock;
	DialogStyle dialogStyle;
	int currentAnswerIndex = 0;
	
	public Dialogue(DialogueBlock startDialogueBlock, DialogStyle dialogStyle) {
		this.startDialogueBlock = startDialogueBlock;
		this.currentDialogueBlock = startDialogueBlock;
		if(dialogStyle == null) {
			dialogStyle = Resources.dialogStyles.get(0);
			System.out.println("Setting dialogstyle to:" + dialogStyle);
		}
		this.dialogStyle = dialogStyle;
		checkChoice();
	}

	private void checkChoice() {
		if(currentDialogueBlock.answers.size()>0) {
			currentAnswerIndex = 0;
		}
	}

	public boolean confirm(LevelState levelState, Player player) {
		System.out.println("Dialog.confirm");
		if(currentDialogueBlock.answers.size()>0)
			currentDialogueBlock = currentDialogueBlock.answers.get(currentAnswerIndex).db;
		else
			currentDialogueBlock = currentDialogueBlock.nextNode;
		if(currentDialogueBlock == null) {
			currentDialogueBlock = startDialogueBlock;
			return false;
		}else {
			currentDialogueBlock.doAction(levelState, player);
			AudioSystemPlayer.playRandom(RandomSound.EXPRESSION);
			checkChoice();
			return true;
		}
		
	}

	public void left() {
		currentAnswerIndex --;
		if(currentAnswerIndex<0)currentAnswerIndex = currentDialogueBlock.answers.size()-1;
	}

	public void right() {
		currentAnswerIndex ++;
		if(currentAnswerIndex>currentDialogueBlock.answers.size()-1)currentAnswerIndex = 0;
	}

	public void draw(Graphics2D graphics, int x, int y, int w, int h) {
		dialogStyle.draw(graphics, x, y, w, h);
		int estimatedTextWidth = 5 * currentDialogueBlock.text.length();
		//System.out.println("Showing dialogue: " + currentDialogueBlock.text);
		int answerCount = currentDialogueBlock.answers.size();
		int lineY, lineX;
		if(answerCount>0) {
			lineX = x + w/2 - estimatedTextWidth/2;
			lineY = y + h/2 + 4;
		}else {
			lineX = x+ 20;
			lineY = y + h/2 + 10;
		}
		graphics.drawString(currentDialogueBlock.text, lineX, lineY);
		Image carot = Resources.getTileImageFromIndex(196867);
		if(answerCount>0) {
			int answerWidth = (w-40)/answerCount;
			for(int i=0; i<answerCount ; i++) {
				graphics.drawString(currentDialogueBlock.answers.get(i).answer, lineX + answerWidth*i, y + h/2 + 17);
				if(i==currentAnswerIndex)
					graphics.drawImage(carot, lineX + answerWidth*i - 16, lineY, null);
			}
		}

	}
	
}
