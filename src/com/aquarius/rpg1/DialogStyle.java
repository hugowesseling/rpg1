package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class DialogStyle  {
	private TilePattern tilePattern;
	private TileSet tileSet;
	
	private Layer layer = null;
	private int prev_w=0, prev_h=0;
	private boolean fillCenter;

	public DialogStyle(int tilePatternIndex, TileSet tileSet, boolean fillCenter) {
		this.tilePattern = tileSet.tilePatterns.get(tilePatternIndex);
		this.tileSet = tileSet;
		this.fillCenter = fillCenter;
	}

	public void draw(Graphics2D graphics, int x, int y, int w, int h) {
		if((layer == null) || (prev_w != w) || (prev_h != h))
		{
			prev_w = w;
			prev_h = h;
			// create new layer
			System.out.println("DialogStyle dimensions changed, recreating");
			layer = new Layer(20, 20);
			if(fillCenter) {
				for(int yt=0; yt <= h/Constant.TILE_HEIGHT  ; yt++) {
					for(int xt=0; xt <= w/Constant.TILE_WIDTH ; xt++) {
						tilePattern.place(layer, tileSet, xt, yt, true);					
					}
				}
			}else {
				for(int xt=0; xt <= w/Constant.TILE_WIDTH ; xt++) {
					tilePattern.place(layer, tileSet, xt, 0, true);					
					tilePattern.place(layer, tileSet, xt, h/Constant.TILE_HEIGHT, true);					
				}
				for(int yt=1; yt < h/Constant.TILE_HEIGHT-1 ; yt++) {
					tilePattern.place(layer, tileSet, 0, yt, true);					
					tilePattern.place(layer, tileSet, w/Constant.TILE_WIDTH, yt, true);					
				}
			}
		}
		AffineTransform transform = graphics.getTransform();
		graphics.translate(x, y);
		layer.drawLayer(graphics, (w/Constant.TILE_WIDTH -1)*Constant.TILE_WIDTH, (h/ Constant.TILE_HEIGHT-1)* Constant.TILE_HEIGHT, 0, 0, false);
		graphics.setTransform(transform);
	}
	public String toString(){
		return tileSet.fileName + ": fillCenter=" + Boolean.toString(fillCenter); 
	}
}
