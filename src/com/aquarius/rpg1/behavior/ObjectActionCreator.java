package com.aquarius.rpg1.behavior;

import java.io.Serializable;

import com.aquarius.rpg1.objects.GameObject;

public interface ObjectActionCreator extends Serializable{

	ObjectAction create(GameObject gameObject);

}
