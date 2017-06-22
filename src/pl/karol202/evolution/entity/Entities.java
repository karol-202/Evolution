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

import pl.karol202.evolution.world.Plants;
import pl.karol202.evolution.world.World;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

public class Entities
{
	private Random random;
	private World world;
	private Plants plants;
	private ArrayList<Entity> entities;
	private int selectedEntity;
	
	private ArrayList<Entity> entitiesToRemove;
	
	public Entities(Random random, World world)
	{
		this.random = random;
		this.world = world;
		this.plants = world.getPlants();
		entities = new ArrayList<>();
		selectedEntity = -1;
		
		entitiesToRemove = new ArrayList<>();
	}
	
	public void generateEntities(int amount)
	{
		entities.clear();
		for(int i = 0; i < amount; i++) generateRandomEntity();
		selectedEntity = -1;
	}
	
	private void generateRandomEntity()
	{
		float x = random.nextFloat() * World.getWorldWidth();
		float y = random.nextFloat() * World.getWorldHeight();
		entities.add(Entity.createRandomEntity(this, x, y, random));
	}
	
	void removeAllEntities()
	{
		entities.clear();
		selectedEntity = -1;
	}
	
	void addEntity(Entity entity)
	{
		entities.add(entity);
	}
	
	void removeEntity(Entity entity)
	{
		if(getSelectedEntity() == entity) selectNothing();
		else if(selectedEntity > entities.indexOf(entity)) selectedEntity--;
		if(entities.contains(entity)) entitiesToRemove.add(entity);
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
		entities.forEach(Entity::update);
		
		if(entitiesToRemove.isEmpty()) return;
		entitiesToRemove.forEach(entities::remove);
		entitiesToRemove.clear();
	}
	
	public void selectEntity(Entity entity)
	{
		if(!entities.contains(entity)) throw new RuntimeException("Unknown entity: " + entity);
		selectedEntity = entities.indexOf(entity);
	}
	
	public void selectNothing()
	{
		selectedEntity = -1;
	}
	
	Plants getPlants()
	{
		return plants;
	}
	
	public Stream<Entity> getEntitiesStream()
	{
		return entities.stream();
	}
	
	public int getEntitiesAmount()
	{
		return entities.size();
	}
	
	public Entity getSelectedEntity()
	{
		if(selectedEntity == -1) return null;
		return entities.get(selectedEntity);
	}
	
	public int getSelectedEntityIndex()
	{
		return selectedEntity;
	}
	
	void setSelectedEntityIndex(int selectedEntity)
	{
		this.selectedEntity = selectedEntity;
	}
}