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
import pl.karol202.evolution.ui.main.ViewInfo;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class BehaviourManager
{
	private Entity entity;
	private ComponentManager componentManager;
	
	private Map<Integer, Behaviour> behaviours;
	private Behaviour currentBehaviour;
	
	public BehaviourManager(Entity entity, ComponentManager componentManager)
	{
		this.entity = entity;
		this.componentManager = componentManager;
		
		behaviours = new HashMap<>();
	}
	
	public void addBehaviours()
	{
		addBehaviour(new RandomMovingBehaviour(entity, componentManager, this));
		addBehaviour(new FoodSeekBehaviour(entity, componentManager, this));
		addBehaviour(new ReproduceBehaviour(entity, componentManager, this));
		
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
		if(entity.shouldEat()) currentBehaviour = findBehaviour(FoodSeekBehaviour.BEHAVIOUR_ID);
		else if(entity.isReadyToReproduce()) currentBehaviour = findBehaviour(ReproduceBehaviour.BEHAVIOUR_ID);
		else currentBehaviour = findBehaviour(RandomMovingBehaviour.BEHAVIOUR_ID);
	}
	
	private void chooseBehaviourWhenBusy()
	{
		if(isEating() || isActivelyReproducing()) return;
		if(entity.shouldEat()) currentBehaviour = findBehaviour(FoodSeekBehaviour.BEHAVIOUR_ID);
		else if(entity.isReadyToReproduce()) currentBehaviour = findBehaviour(ReproduceBehaviour.BEHAVIOUR_ID);
	}
	
	void abandonCurrentBehaviour()
	{
		currentBehaviour = null;
	}
	
	public ReproduceBehaviour getReproduceBehaviour()
	{
		return (ReproduceBehaviour) findBehaviour(ReproduceBehaviour.BEHAVIOUR_ID);
	}
	
	public ReproduceBehaviour reproduce()
	{
		if(!(currentBehaviour instanceof ReproduceBehaviour))
			currentBehaviour = getReproduceBehaviour();
		return (ReproduceBehaviour) currentBehaviour;
	}
	
	private boolean isEating()
	{
		return currentBehaviour instanceof FoodSeekBehaviour;
	}
	
	public boolean isReproducing()
	{
		return currentBehaviour instanceof ReproduceBehaviour;
	}
	
	public boolean isActivelyReproducing()
	{
		return isReproducing() && ((ReproduceBehaviour) currentBehaviour).isBusy();
	}
	
	public void drawCurrentBehaviour(Graphics2D g, ViewInfo viewInfo)
	{
		if(currentBehaviour != null) currentBehaviour.drawBehaviour(g, viewInfo);
	}
	
	public String getCurrentBehaviourName()
	{
		if(currentBehaviour == null) return "";
		return currentBehaviour.getName();
	}
	
	public int getCurrentBehaviourId()
	{
		if(currentBehaviour == null) return -1;
		return currentBehaviour.getId();
	}
	
	public void setCurrentBehaviourId(int id)
	{
		currentBehaviour = behaviours.get(id);
	}
	
	public Stream<SavableBehaviour> getSavableBehavioursStream()
	{
		return behaviours.values().stream().filter(b -> b instanceof SavableBehaviour).map(b -> (SavableBehaviour) b);
	}
}