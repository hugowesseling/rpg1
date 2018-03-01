package com.aquarius.testdialogue2;

import com.aquarius.rpg1.DialogueBlock;

public class TestDialogue2 {

	public TestDialogue2() {
		/*
		Dialog flow: 
		    Hello -> Want some candy?
				- No -> Well suit yourself -> <endpart>
				- Yes -> Here is some candy -> <endpart>
			<endpart> -> Okay then -> See you later! -> <end>
		*/

		DialogueBlock begin = new DialogueBlock("Hello");
		begin.add(new DialogueBlock("Want some candy?"))
				.addAnswer("No", new DialogueBlock("Well suit yourself").jumpTo("endpart"))
				.addAnswer("Yes", new DialogueBlock("Here is some candy").jumpTo("endpart"))
			.addJumpPoint("endpart", new DialogueBlock("Okay then")).add(new DialogueBlock("See you later!"));

		begin.run();
	}
	
	public static void main(String[] args)
	{
		System.out.println("Start");
		new TestDialogue2();
		System.out.println("End");
	}

}
