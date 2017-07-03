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
import java.util.stream.Stream;

public class ComponentManager
{
	private Entity entity;
	private Entities entities;
	
	private ArrayList<Component> components;
	
	ComponentManager(Entity entity, Entities entities)
	{
		this.entity = entity;
		this.entities = entities;
		
		components = new ArrayList<>();
	}
	
	void addComponents()
	{
		components.add(new EntityMovement(entity));
		components.add(new EntitySight(entity, entities.getPlants(), entities, entity.getSightRange()));
		components.add(new EntitySmell(entity, entities, entity.getSmellRange()));
		components.add(new EntityNutrition(entity));
	}
	
	void update()
	{
		components.forEach(Component::update);
	}
	
	public Component getComponent(Class<? extends Component> type)
	{
		return components.stream().filter(c -> c.getClass() == type).findAny().orElse(null);
	}
	
	Stream<SavableComponent> getSavableComponentsStream()
	{
		return components.stream().filter(c -> c instanceof SavableComponent).map(c -> (SavableComponent) c);
	}
}