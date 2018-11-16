package com.aquarius.rpg1;

public class ValueObjectType extends StorableObjectType {

	private int value;

	public ValueObjectType(String name, int tileIndex, int value) {
		super(name, tileIndex);
		this.value = value;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8980815866183943714L;

}
