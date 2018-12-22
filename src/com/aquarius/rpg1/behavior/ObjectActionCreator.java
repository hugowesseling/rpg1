package com.aquarius.rpg1.behavior;

import com.aquarius.rpg1.behavior.hateno.SingleActionDoer;
import com.aquarius.rpg1.objects.GameObject;

public interface ObjectActionCreator {

	ObjectAction create(GameObject gameObject);

}
