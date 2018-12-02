package com.aquarius.rpg1;

import java.util.Stack;

public class LevelStack
{
	class StackLayer
	{
		int[][] tiles1, tiles2;

		public StackLayer(int[][] tiles1, int[][] tiles2) {
			this.tiles1 = tiles1;
			this.tiles2 = tiles2;
		}
	}
	Layer layer1, layer2;
	Stack<StackLayer> stack = new Stack<StackLayer>();
	
	public LevelStack(Layer layer1, Layer layer2) 
	{
		super();
		this.layer1 = layer1;
		this.layer2 = layer2;
	}

	public void clearStack()
	{
		stack = new Stack<StackLayer>();
	}
	
	public void pushLayers()
	{
		StackLayer stackLayer = new StackLayer(layer1.cloneTiles(), layer2.cloneTiles());
		System.out.println("Pushing");
		stack.push(stackLayer);
	}
	
	public void popLayersIfNoChange()
	{
		// Compare layer1 and layer2 to the top of the stack, if equal, pop it off
		StackLayer stackLayer = stack.peek();
		if(layer1.areTilesEqual(stackLayer.tiles1) &&
		   layer2.areTilesEqual(stackLayer.tiles2))
		{
			System.out.println("Popping due to no change");
			stack.pop();
		}
	}
	public void popIntoLayers()
	{
		if(!stack.empty()) 
		{
			StackLayer stackLayer = stack.pop();
			layer1.setTiles(stackLayer.tiles1);
			layer2.setTiles(stackLayer.tiles2);
		}else
		{
			System.out.println("Nothing to pop");
		}
	}

	@SuppressWarnings("unused")
	private void printLayer(int[][] tiles1)
	{
		for(int y=0;y<tiles1[0].length;y++)
		{
			String outText = "";
			for(int x=0;x<tiles1.length;x++)
				outText += "," + tiles1[x][y];
			System.out.println(outText);
		}
	}
}
