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
package pl.karol202.evolution.entity;

import pl.karol202.evolution.stats.Stats;
import pl.karol202.evolution.world.Plants;
import pl.karol202.evolution.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

public class Entities
{
	private Random random;
	private World world;
	private Plants plants;
	private EntitiesList entities;
	
	private ArrayList<Entity> entitiesToAdd;
	private ArrayList<Entity> entitiesToRemove;
	
	public Entities(Random random, World world)
	{
		this.random = random;
		this.world = world;
		this.plants = world.getPlants();
		entities = new EntitiesList();
		
		entitiesToAdd = new ArrayList<>();
		entitiesToRemove = new ArrayList<>();
	}
	
	public void generateEntities(int amount)
	{
		entities.clear();
		for(int i = 0; i < amount; i++) generateRandomEntity();
	}
	
	private void generateRandomEntity()
	{
		float x = random.nextFloat() * World.getWorldWidth();
		float y = random.nextFloat() * World.getWorldHeight();
		entities.addEntity(Entity.createRandomEntity(this, x, y, random), false);
	}
	
	void removeAllEntities()
	{
		entities.clear();
	}
	
	public void addNewEntity(Entity entity)
	{
		entitiesToAdd.add(entity);
		Stats.instance.registerEntityBorn();
	}
	
	void addEntityInstantly(Entity entity, boolean selected)
	{
		entities.addEntity(entity, selected);
	}
	
	void removeDeadEntity(Entity entity)
	{
		entitiesToRemove.add(entity);
		Stats.instance.registerEntityDeath();
	}
	
	float getTemperature(float x, float y)
	{
		return world.getTemperature(x, y);
	}
	
	float getHumidity(float x, float y)
	{
		return world.getHumidity(x, y);
	}
	
	public void update()
	{
		entities.updateAll();
		addEntities();
		removeEntities();
	}
	
	private void addEntities()
	{
		if(entitiesToAdd.isEmpty()) return;
		entitiesToAdd.forEach(e -> entities.addEntity(e, false));
		entitiesToAdd.clear();
	}
	
	private void removeEntities()
	{
		if(entitiesToRemove.isEmpty()) return;
		entitiesToRemove.forEach(entities::removeEntity);
		entitiesToRemove.clear();
	}
	
	public void selectOnlyEntity(Entity entity)
	{
		entities.selectOnlyEntity(entity);
	}
	
	public void selectEntity(Entity entity)
	{
		entities.selectEntity(entity);
	}
	
	public void deselectEntity(Entity entity)
	{
		entities.deselectEntity(entity);
	}
	
	public void selectEntitiesInRect(Rectangle rect)
	{
		entities.selectEntitiesInRect(rect);
	}
	
	public void deselectEntitiesInRect(Rectangle rect)
	{
		entities.deselectEntitiesInRect(rect);
	}
	
	public void selectNothing()
	{
		entities.selectNothing();
	}
	
	Plants getPlants()
	{
		return plants;
	}
	
	public int getEntitiesAmount()
	{
		return entities.size();
	}
	
	public Stream<Entity> getEntitiesStream()
	{
		return entities.entitiesStream();
	}
	
	public Stream<Entity> getSelectedEntities()
	{
		return entities.selectedEntitiesStream();
	}
	
	public boolean isEntitySelected(Entity entity)
	{
		return entities.isEntitySelected(entity);
	}
	
	public int getEntityId(Entity entity)
	{
		return entities.indexOfEntity(entity);
	}
	
	public Entity getEntityById(int id)
	{
		return entities.getEntityById(id);
	}
}