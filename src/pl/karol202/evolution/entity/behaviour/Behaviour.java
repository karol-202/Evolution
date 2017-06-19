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

import pl.karol202.evolution.entity.Entity;

public abstract class Behaviour
{
	protected Entity entity;
	protected BehaviourManager behaviours;
	
	public Behaviour(Entity entity, BehaviourManager behaviours)
	{
		this.entity = entity;
		this.behaviours = behaviours;
	}
	
	public abstract void update();
	
	public abstract int getId();
	
	public abstract String getName();
}