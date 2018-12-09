package com.aquarius.rpg1;

import java.util.Vector;

public class Player extends GameObject {
	private static final long serialVersionUID = 1752542798493227606L;
	Vector<CarryableItem> itemsCarried;
	private GameObject interactionGameObject = null;
	public Bag<String> inventory;
	private InteractionPossibility interactionPossibilty;
	public Player(ObjectDrawer objectDrawer, ObjectPosition position, Direction direction) {
		super("player", objectDrawer, position, direction);
		weapon = new BeamWeapon(this);
		inventory = new Bag<>();
		inventory.add("broccoli ring");
	}
	public GameObject getInteractionGameObject() {
		return interactionGameObject;
	}
	public void setInteractionGameObject(GameObject interactionGameObject) {
		this.interactionGameObject = interactionGameObject;
	}
	
	public void determineInteractionGameObject(Vector<GameObject> allGameObjects) {
		setInteractionGameObject(null);
		for(GameObject gameObject: allGameObjects)
		{
			if(hasInSight(gameObject.getPosition(), 64))
			{
				//System.out.println("hasInSight: " + gameObject.name);
				if(gameObject.getInteractionPossibilities().contains(InteractionPossibility.TALK)) {
					setInteractionGameObject(gameObject);
					setInteractionPossibility(InteractionPossibility.TALK);
					//System.out.println("Player has in sight!");
				}else
				if(gameObject.getInteractionPossibilities().contains(InteractionPossibility.OPEN)) {
					setInteractionGameObject(gameObject);
					setInteractionPossibility(InteractionPossibility.OPEN);
					//System.out.println("Player has in sight!");
				}else					
				if(gameObject.getInteractionPossibilities().contains(InteractionPossibility.UNLOCK)) {
					setInteractionGameObject(gameObject);
					setInteractionPossibility(InteractionPossibility.UNLOCK);
					//System.out.println("Player has in sight!");
				}					
				
			}
		}
	}
	private void setInteractionPossibility(InteractionPossibility interactionPossibilty) {
		this.interactionPossibilty = interactionPossibilty;
		
	}
	public void checkIfTouching(LevelState levelState) {
		for(GameObject gameObject: levelState.allGameObjects) {
			if(colliding(gameObject)) {
				System.out.println(gameObject.name + " is colliding");
				if(gameObject.getInteractionPossibilities().contains(InteractionPossibility.TOUCH)) {
					System.out.println("Touching " + gameObject.name);
					gameObject.doTouchAction(levelState, this);
				}
			}
		}
	}
	@Override
	public void doTouchAction(LevelState levelState, Player player) {
		System.out.println("Don't touch yourself!");
	}
	
	public InteractionPossibility getInteractionPossiblity() {
		return interactionPossibilty;
	}
}
