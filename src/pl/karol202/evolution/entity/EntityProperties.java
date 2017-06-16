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

import java.util.function.Function;

public enum EntityProperties
{
	X("X", e -> toString(e.getX())),
	Y("Y", e -> toString(e.getY())),
	BEHAVIOUR("Zachowanie", Entity::getCurrentBehaviourName),
	SIZE("Wielkość", e -> toString(e.getSize())),
	SPEED("Szybkość", e -> toString(e.getSpeed()));
	
	private String name;
	private Function<Entity, String> function;
	
	EntityProperties(String name, Function<Entity, String> function)
	{
		this.name = name;
		this.function = function;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getValueForEntity(Entity entity)
	{
		return function.apply(entity);
	}
	
	private static String toString(float f)
	{
		return Float.toString(f);
	}
}