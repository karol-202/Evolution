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
package pl.karol202.evolution.entity.property;

import pl.karol202.evolution.entity.Entity;

import java.util.function.Function;

class EntityStringPropertyAdapter implements EntityPropertyAdapter<String>
{
	private Function<Entity, String> stringFunction;
	
	EntityStringPropertyAdapter(Function<Entity, String> stringFunction)
	{
		this.stringFunction = stringFunction;
	}
	
	@Override
	public boolean isStatsCapable()
	{
		return false;
	}
	
	@Override
	public String getValueForEntity(Entity entity)
	{
		return stringFunction.apply(entity);
	}
	
	@Override
	public String transformToString(String value)
	{
		return value;
	}
	
	@Override
	public boolean isRegistered()
	{
		return false;
	}
}