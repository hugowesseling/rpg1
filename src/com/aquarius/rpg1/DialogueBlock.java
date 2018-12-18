package com.aquarius.rpg1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DialogueBlock implements Serializable
{
	private static final long serialVersionUID = 6276869414867442109L;
	String text;
	DialogueBlock nextNode = null;
	DialogueAction action = null;
	String jumpTo = null;
	public class AnswerPair { 
		  public final String answer;
		  public final DialogueBlock db;
		  public AnswerPair(String answer, DialogueBlock db) {
		    this.answer = answer;
		    this.db = db;
		  }
	}
	public ArrayList<AnswerPair> answers = new ArrayList<>();
	public HashMap<String, DialogueBlock> jumpPoints = new HashMap<String, DialogueBlock>();
	
	public DialogueBlock(String text)
	{
		this.text = text;
	}

	public DialogueBlock(String text, DialogueAction action)
	{
		this(text);
		this.action = action;
	}

	public DialogueBlock add(DialogueBlock dialogueBlock)
	{
		nextNode = dialogueBlock;
		return dialogueBlock;
	}

	public DialogueBlock addAnswer(String answer, DialogueBlock dialogueBlock)
	{
		answers.add(new AnswerPair(answer, dialogueBlock));
		return this;
	}
	
	public DialogueBlock jumpTo(String reference)
	{
		jumpTo = reference;
		return this;
	}

	public DialogueBlock addJumpPoint(String reference, DialogueBlock dialogueBlock)
	{
		jumpPoints.put(reference, dialogueBlock);
		return dialogueBlock;
	}

	public void run()
	{
		run(new HashMap<String, DialogueBlock>());
	}
	
	public void run(HashMap<String, DialogueBlock> jumpPointsFromTop)
	{
		jumpPoints.putAll(jumpPointsFromTop);
		
		System.out.println("Text: \"" + text + "\"");
		
		if(answers.size() > 0)
		{
			for(AnswerPair answerPair : answers)
			{
				System.out.println("If answering " + answerPair.answer + ":");
				answerPair.db.run(jumpPoints);
				System.out.println("End of answering " + answerPair.answer);
			}
		}
		
		if(nextNode != null )
		{
			nextNode.run(jumpPoints);
		}else if(jumpTo != null)
		{
			DialogueBlock dialogueBlock = jumpPoints.get(jumpTo);
			if(dialogueBlock != null)
			{
				dialogueBlock.run(jumpPoints);
			}else
			{
				System.out.println("Error: Could not find jump point \"" + jumpTo + "\"");
			}
		}else
		{
			System.out.println("Done");
		}
		
	}

	public void doAction(LevelState levelState, Player player) {
		if(action != null){
			action.doAction(levelState, player);
		}
	}
}
