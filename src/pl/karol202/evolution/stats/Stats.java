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
package pl.karol202.evolution.stats;

import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.world.World;

import java.util.ArrayList;
import java.util.List;

public class Stats
{
	public static Stats instance;
	
	private int initialEntitiesAmount;
	private List<EntityStatsEvent> entityEvents;
	private int highestEntitiesAmount;
	private float currentTime;
	
	private Stats(int initialEntitiesAmount)
	{
		this.initialEntitiesAmount = initialEntitiesAmount;
		this.entityEvents = new ArrayList<>();
		this.highestEntitiesAmount = initialEntitiesAmount;
		this.currentTime = 0;
	}
	
	public static void resetStats(World world)
	{
		Stats.instance = new Stats(world.getInitialEntitiesAmount());
	}
	
	public void update()
	{
		currentTime += Simulation.deltaTime;
	}
	
	public void registerEntityBorn()
	{
		entityEvents.add(new BornEntityStatsEvent(currentTime));
		highestEntitiesAmount++;
	}
	
	public void registerEntityDeath()
	{
		entityEvents.add(new DeathEntityStatsEvent(currentTime));
	}
	
	public int getEntitiesAmount(float time)
	{
		return initialEntitiesAmount + entityEvents.stream()
												   .filter(e -> e.getTime() < time)
												   .mapToInt(EntityStatsEvent::getInfluenceOnAmount)
												   .sum();
	}
	
	public int getHighestEntitiesAmount()
	{
		return highestEntitiesAmount;
	}
	
	public float getCurrentTime()
	{
		return currentTime;
	}
}