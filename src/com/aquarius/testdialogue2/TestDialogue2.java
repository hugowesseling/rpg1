package com.aquarius.testdialogue2;

public class TestDialogue2 {

	public TestDialogue2() {
		/*
		Dialog flow: 
		    Hello -> Want some candy?
				- No -> Well suit yourself -> <endpart>
				- Yes -> Here is some candy -> <endpart>
			<endpart> -> Okay then -> See you later! -> <end>
		*/

		Dialogue begin = new Dialogue("Hello");
		begin.add(new Dialogue("Want some candy?"))
				.addAnswer("No", new Dialogue("Well suit yourself").jumpTo("endpart"))
				.addAnswer("Yes", new Dialogue("Here is some candy").jumpTo("endpart"))
			.addJumpPoint("endpart", new Dialogue("Okay then")).add(new Dialogue("See you later!"));

		begin.run();
	}
	
	public static void main(String[] args)
	{
		System.out.println("Start");
		new TestDialogue2();
		System.out.println("End");
	}

}
