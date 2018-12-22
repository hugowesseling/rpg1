package com.aquarius.rpg1.objects;

import com.aquarius.rpg1.ObjectColor;

public class RingObjectType extends StorableObjectType {
	// Rings are used to open doors with specific colors
	private static final long serialVersionUID = -1191999549213203871L;
	private ObjectColor color;

	public RingObjectType(String name, int tileIndex, ObjectColor color) {
		super(name, tileIndex);
		this.color = color;
	}
}
