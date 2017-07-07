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

import pl.karol202.evolution.entity.property.EntityProperties;

import java.util.Map;

public class EntityStatsEventFactory
{
	public EntityStatsEvent createEvent(String className, float time, Map<EntityProperties, Float> entityPropertiesMap)
	{
		if(className.equals(BornEntityStatsEvent.class.getName()))
			return new BornEntityStatsEvent(time, entityPropertiesMap);
		else if(className.equals(DeathEntityStatsEvent.class.getName()))
			return new DeathEntityStatsEvent(time, entityPropertiesMap);
		else return null;
	}
}