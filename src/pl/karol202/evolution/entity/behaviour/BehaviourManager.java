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

import pl.karol202.evolution.entity.Component;
import pl.karol202.evolution.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BehaviourManager
{
	private Entity entity;
	private ArrayList<Component> components;
	
	private Map<Integer, Behaviour> behaviours;
	private Behaviour currentBehaviour;
	
	public BehaviourManager(Entity entity, ArrayList<Component> components)
	{
		this.entity = entity;
		this.components = components;
		
		behaviours = new HashMap<>();
	}
	
	public void addBehaviours()
	{
		addBehaviour(new RandomMovingBehaviour(entity, components, this));
		addBehaviour(new FoodSeekBehaviour(entity, components, this));
		
		currentBehaviour = findBehaviour(RandomMovingBehaviour.BEHAVIOUR_ID);
	}
	
	private void addBehaviour(Behaviour behaviour)
	{
		behaviours.put(behaviour.getId(), behaviour);
	}
	
	private Behaviour findBehaviour(int id)
	{
		return behaviours.get(id);
	}
	
	public void update()
	{
		if(currentBehaviour == null) chooseBehaviourWhenNotBusy();
		else chooseBehaviourWhenBusy();
		currentBehaviour.update();
	}
	
	private void chooseBehaviourWhenNotBusy()
	{
		if(shouldEat()) currentBehaviour = findBehaviour(FoodSeekBehaviour.BEHAVIOUR_ID);
		else currentBehaviour = findBehaviour(RandomMovingBehaviour.BEHAVIOUR_ID);
	}
	
	private void chooseBehaviourWhenBusy()
	{
		if(shouldEat()) currentBehaviour = findBehaviour(FoodSeekBehaviour.BEHAVIOUR_ID);
	}
	
	private boolean shouldEat()
	{
		return entity.getEnergy() < entity.getEatStartEnergyThreshold() * entity.getMaxEnergy();
	}
	
	void abandonCurrentBehaviour()
	{
		currentBehaviour = null;
	}
	
	public String getCurrentBehaviourName()
	{
		if(currentBehaviour == null) return "";
		return currentBehaviour.getName();
	}
}