/*
  Copyright 2017 karol-202
 
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
 
        http://www.apache.org/licenses/LICENSE-2.0
 
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package pl.karol202.evolution.entity.behaviour;

import pl.karol202.evolution.entity.ComponentManager;
import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.entity.EntityMovement;
import pl.karol202.evolution.world.World;

import java.util.Random;

public class RandomMovingBehaviour extends Behaviour
{
	static final int BEHAVIOUR_ID = 0;
	
	private EntityMovement movement;
	private Random random;
	
	RandomMovingBehaviour(Entity entity, ComponentManager componentManager, BehaviourManager behaviours)
	{
		super(entity, behaviours);
		movement = (EntityMovement) componentManager.getComponent(EntityMovement.class);
		random = new Random();
	}
	
	@Override
	public void update()
	{
		if(!movement.isMoving()) newTarget();
	}
	
	@Override
	public int getId()
	{
		return BEHAVIOUR_ID;
	}
	
	private void newTarget()
	{
		float x = random.nextFloat() * (World.getWorldWidth() - 1);
		float y = random.nextFloat() * (World.getWorldHeight() - 1);
		movement.setTarget(x, y);
	}
	
	@Override
	public String getName()
	{
		return "Losowe poruszanie się";
	}
}