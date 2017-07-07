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

import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.entity.property.EntityProperties;
import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Stats
{
	public static final Stats instance = new Stats();
	
	private int initialEntitiesAmount;
	private List<Map<EntityProperties, Float>> initialPropertiesMapsList;
	
	private List<EntityStatsEvent> entityEvents;
	
	private int highestEntitiesAmount;
	private float currentTime;
	
	private Stats()
	{
		resetStats(0, new ArrayList<>());
	}
	
	public void resetStats(World world)
	{
		resetStats(world.getInitialEntitiesAmount(), createPropertiesMapsList(world.getEntities().getEntitiesStream()));
	}
	
	void resetStats(int initialEntitiesAmount, List<Map<EntityProperties, Float>> initialEntities)
	{
		this.initialEntitiesAmount = initialEntitiesAmount;
		this.initialPropertiesMapsList = initialEntities;
		
		this.entityEvents = new ArrayList<>();
		
		this.highestEntitiesAmount = initialEntitiesAmount;
		this.currentTime = 0;
	}
	
	private List<Map<EntityProperties, Float>> createPropertiesMapsList(Stream<Entity> initialEntities)
	{
		return initialEntities.map(this::getMapOfEntityProperties).collect(Collectors.toList());
	}
	
	private Map<EntityProperties, Float> getMapOfEntityProperties(Entity entity)
	{
		return Stream.of(EntityProperties.values())
					 .filter(EntityProperties::isFloatProperty)
					 .filter(EntityProperties::isRegistered)
					 .collect(Collectors.toMap(ep -> ep, ep -> ep.getFloatValueForEntity(entity)));
	}
	
	public void update()
	{
		currentTime += Simulation.deltaTime;
	}
	
	public void registerEntityBorn(Entity entity)
	{
		entityEvents.add(new BornEntityStatsEvent(currentTime, getMapOfEntityProperties(entity)));
		highestEntitiesAmount = Math.max(highestEntitiesAmount, getEntitiesAmount(currentTime));
	}
	
	public void registerEntityDeath(Entity entity)
	{
		entityEvents.add(new DeathEntityStatsEvent(currentTime, getMapOfEntityProperties(entity)));
	}
	
	public int getEntitiesAmount(float time)
	{
		return initialEntitiesAmount + entityEvents.stream()
												   .filter(e -> e.getTime() <= time)
												   .mapToInt(EntityStatsEvent::getInfluenceOnAmount)
												   .sum();
	}
	
	public Stream<Map<EntityProperties, Float>> getEntitiesProperties(float time)
	{
		List<Map<EntityProperties, Float>> mapsList = initialPropertiesMapsList.stream().map(HashMap::new)
																						.collect(Collectors.toList());
		entityEvents.stream().filter(e -> e.getTime() <= time).forEach(e -> {
			Map<EntityProperties, Float> map = e.getEntityPropertiesMap();
			if(e.getInfluenceOnAmount() > 0) mapsList.add(map);
			else if(e.getInfluenceOnAmount() < 0) mapsList.remove(map);
		});
		return mapsList.stream();
	}
	
	int getInitialEntitiesAmount()
	{
		return initialEntitiesAmount;
	}
	
	Stream<Map<EntityProperties, Float>> getInitialPropertiesMapsStream()
	{
		return initialPropertiesMapsList.stream();
	}
	
	Stream<EntityStatsEvent> getEntityEventsStream()
	{
		return entityEvents.stream();
	}
	
	void clearAllEvents()
	{
		entityEvents.clear();
	}
	
	void addEntityEvent(EntityStatsEvent event)
	{
		entityEvents.add(event);
	}
	
	public int getHighestEntitiesAmount()
	{
		return highestEntitiesAmount;
	}
	
	void setHighestEntitiesAmount(int highestEntitiesAmount)
	{
		this.highestEntitiesAmount = highestEntitiesAmount;
	}
	
	public float getCurrentTime()
	{
		return currentTime;
	}
	
	void setCurrentTime(float currentTime)
	{
		this.currentTime = currentTime;
	}
}