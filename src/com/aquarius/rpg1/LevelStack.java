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
	LevelState levelState;
	Stack<StackLayer> stack = new Stack<StackLayer>();
	
	public LevelStack(Layer layer1, Layer layer2) 
	{
		super();
		this.layer1 = layer1;
		this.layer2 = layer2;
	}

	public void pushLayers()
	{
		StackLayer stackLayer = new StackLayer(clone2dArray(layer1.tiles), clone2dArray(layer2.tiles));
		System.out.println("Pushing");
		stack.push(stackLayer);
	}
	private int[][] clone2dArray(int[][] array2d)
	{
		int [][] theClone = new int[array2d.length][];
		for(int i = 0; i < array2d.length; i++)
			theClone[i] = array2d[i].clone();
		return theClone;
	}
	
	public void popLayersIfNoChange()
	{
		// Compare layer1 and layer2 to the top of the stack, if equal, pop it off
		StackLayer stackLayer = stack.peek();
		if(layerEqual(stackLayer.tiles1, layer1.tiles) && layerEqual(stackLayer.tiles2, layer2.tiles))
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
			layer1.tiles = stackLayer.tiles1;
			layer2.tiles = stackLayer.tiles2;
		}else
		{
			System.out.println("Nothing to pop");
		}
	}

	private boolean layerEqual(int[][] tiles1, int[][] tiles2) 
	{
		// returns true if all tiles are equal
		if(tiles1.length != tiles2.length || tiles1[0].length != tiles2[0].length)
			return false;
		for(int x=0;x<tiles1.length;x++)
			for(int y=0;y<tiles1[0].length;y++)
				if(tiles1[x][y] != tiles2[x][y])return false;
		/*System.out.println("equal layers: ");
		printLayer(tiles1);
		System.out.println("==");
		printLayer(tiles2);*/
		return true;
	}

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
