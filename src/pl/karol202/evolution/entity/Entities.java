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

import java.util.ArrayList;
import java.util.Random;

public class Entities
{
	private Random random;
	private ArrayList<Entity> entities;
	private int selectedEntity;
	
	public Entities(Random random)
	{
		this.random = random;
		entities = new ArrayList<>();
		selectedEntity = -1;
	}
	
	public void generateEntities()
	{
		entities.clear();
		entities.add(Entity.createRandomEntity(100, 100, random));
		selectedEntity = -1;
	}
	
	public void update()
	{
		entities.forEach(Entity::update);
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
	
	public Entity[] getEntities()
	{
		return entities.toArray(new Entity[entities.size()]);
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
}