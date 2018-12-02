package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.Direction;
import com.aquarius.rpg1.GameObject;
import com.aquarius.rpg1.Int2d;
import com.aquarius.rpg1.LevelState;
import com.aquarius.rpg1.WorldState;

public class WalkToCharacterAction implements ObjectAction, Serializable{

	private static final long serialVersionUID = -4328959808077331996L;
	private GameObject character;
	private GameObject toCharacter;
	private int mindistance;
	private int maxdistance;

	public WalkToCharacterAction(GameObject character, GameObject toCharacter, int mindistance, int maxdistance) {
		this.character = character;
		this.toCharacter = toCharacter;
		this.mindistance = mindistance;
		this.maxdistance = maxdistance;
	}

	@Override
	public boolean doActionAndCheckIfDone(WorldState worldState, LevelState levelState) {
		Direction direction = Direction.getDirectionFromTo(character.getPosition(), toCharacter.getPosition());
		character.setDirection(direction);
		character.setMovement(direction.movement);
		
		int distance = toCharacter.getPosition().distanceTo(character.getPosition());
		boolean isDone = (distance < mindistance) || (distance > maxdistance);
		if(isDone) {
			character.setMovement(new Int2d(0,0));
		}
		return isDone;
	}


}
