package com.aquarius.rpg1;

import java.awt.Graphics2D;
import java.util.Map;

import com.aquarius.rpg1.objects.StorableObjectType;

public class InventoryMenu {

	private DialogStyle dialogStyle;

	public InventoryMenu(DialogStyle dialogStyle) {
		this.dialogStyle = dialogStyle;
	}

	public void draw(Graphics2D graphics, int x, int y, int w, int h, Bag<String> inventory) {
		dialogStyle.draw(graphics, x, y, w, h);
		int yItem = y + 20;
		for (Map.Entry<String, Integer> entry : inventory.entrySet())
		{
			StorableObjectType sot = Resources.allStorableObjectTypesHashMap.get(entry.getKey());
			graphics.drawImage(Resources.itemTileSet.getTileImageFromIndexSafe(sot.itemTileIndex), x + 20, yItem-10, null);
			graphics.drawString(entry.getKey() + ":" + entry.getValue(), x + 40, yItem);
			yItem+=20;
		} 
	}

}
