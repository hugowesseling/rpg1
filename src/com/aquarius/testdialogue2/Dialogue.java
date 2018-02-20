package com.aquarius.testdialogue2;

import java.util.HashMap;
import java.util.Map;

public class Dialogue
{
	String text;
	Dialogue nextNode = null;
	String jumpTo = null;
	private HashMap<String, Dialogue> answers = new HashMap<String, Dialogue>();
	private HashMap<String, Dialogue> jumpPoints = new HashMap<String, Dialogue>();
	
	public Dialogue(String text)
	{
		this.text = text;
	}

	public Dialogue add(Dialogue dialogue)
	{
		nextNode = dialogue;
		return dialogue;
	}

	public Dialogue addAnswer(String answer, Dialogue dialogue)
	{
		answers.put(answer, dialogue);
		return this;
	}
	
	public Dialogue jumpTo(String reference)
	{
		jumpTo = reference;
		return this;
	}

	public Dialogue addJumpPoint(String reference, Dialogue dialogue)
	{
		jumpPoints.put(reference, dialogue);
		return dialogue;
	}

	public void run()
	{
		run(new HashMap<String, Dialogue>());
	}
	
	public void run(HashMap<String, Dialogue> jumpPointsFromTop)
	{
		jumpPoints.putAll(jumpPointsFromTop);
		
		System.out.println("Text: \"" + text + "\"");
		
		if(answers.size() > 0)
		{
			for(Map.Entry<String, Dialogue> entry : answers.entrySet())
			{
				String answer = entry.getKey();
				Dialogue dialogue = entry.getValue();
				System.out.println("If answering " + answer + ":");
				dialogue.run(jumpPoints);
				System.out.println("End of answering " + answer);
			}
		}
		
		if(nextNode != null )
		{
			nextNode.run(jumpPoints);
		}else if(jumpTo != null)
		{
			Dialogue dialogue = jumpPoints.get(jumpTo);
			if(dialogue != null)
			{
				dialogue.run(jumpPoints);
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
