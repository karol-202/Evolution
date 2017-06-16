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

public abstract class Behaviour
{
	protected Entity entity;
	protected ArrayList<Component> components;
	protected ArrayList<Behaviour> behaviours;
	
	public Behaviour(Entity entity, ArrayList<Component> components, ArrayList<Behaviour> behaviours)
	{
		this.entity = entity;
		this.components = components;
		this.behaviours = behaviours;
	}
	
	public abstract void update();
	
	public abstract String getName();
	
	protected Component getComponent(Class<? extends Component> type)
	{
		return components.stream().filter(c -> c.getClass() == type).findAny().orElse(null);
	}
}