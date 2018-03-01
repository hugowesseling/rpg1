package com.aquarius.rpg1;

public interface CarryableItem {
	public String getName();
	public int getIconIndex();
	public void useBy(Character character);	// Implement the effect on character if he/she uses/eats this item	
	public void hitBy(Character character);	// Implement what happens if the item is thrown
	
}
