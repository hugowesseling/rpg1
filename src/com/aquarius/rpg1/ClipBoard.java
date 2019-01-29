package com.aquarius.rpg1;

public class ClipBoard
{
	public static ClipBoard instance = null;
	public Layer bottom_layer, top_layer;

	public ClipBoard(Selection selection, Layer bottom_layer, Layer top_layer)
	{
		this.bottom_layer = selection.copyFromLayer(bottom_layer);
		this.top_layer = selection.copyFromLayer(top_layer);
		instance = this;
	}

	public void copyToLayer(Layer bottom_layer, Layer top_layer, int tileX, int tileY) {
		for(int x = 0; x < this.bottom_layer.getWidth(); x++)
		{
			for(int y = 0; y < this.bottom_layer.getHeight(); y++)
			{
				top_layer.setTileIndexForCheckedXY(x + tileX, y + tileY, this.top_layer.getTile(x, y));
				bottom_layer.setTileIndexForCheckedXY(x + tileX, y + tileY, this.bottom_layer.getTile(x, y));
			}
		}
		
	}
}
