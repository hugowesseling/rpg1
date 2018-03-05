package com.aquarius.rpg1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DialogueBlock implements Serializable
{
	private static final long serialVersionUID = 6276869414867442109L;
	String text;
	DialogueBlock nextNode = null;
	String jumpTo = null;
	private HashMap<String, DialogueBlock> answers = new HashMap<String, DialogueBlock>();
	private HashMap<String, DialogueBlock> jumpPoints = new HashMap<String, DialogueBlock>();
	
	public DialogueBlock(String text)
	{
		this.text = text;
	}

	public DialogueBlock add(DialogueBlock dialogueBlock)
	{
		nextNode = dialogueBlock;
		return dialogueBlock;
	}

	public DialogueBlock addAnswer(String answer, DialogueBlock dialogueBlock)
	{
		answers.put(answer, dialogueBlock);
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
			for(Map.Entry<String, DialogueBlock> entry : answers.entrySet())
			{
				String answer = entry.getKey();
				DialogueBlock dialogueBlock = entry.getValue();
				System.out.println("If answering " + answer + ":");
				dialogueBlock.run(jumpPoints);
				System.out.println("End of answering " + answer);
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
}
