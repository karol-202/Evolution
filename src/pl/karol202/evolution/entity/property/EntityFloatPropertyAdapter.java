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

import java.text.DecimalFormat;
import java.util.function.Function;
 
class EntityFloatPropertyAdapter implements EntityPropertyAdapter<Float>
{
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.0#");
	private static final Function<Float, String> DEFAULT_DISPLAY = DECIMAL_FORMAT::format;
	
	private Function<Entity, Float> valueFunction;
	private Function<Float, String> displayFunction;
	private boolean registered;
	private float min;
	private float max;
	
	EntityFloatPropertyAdapter(Function<Entity, Float> valueFunction, Function<Float, String> displayFunction, boolean registered, float min, float max)
	{
		this.valueFunction = valueFunction;
		this.displayFunction = displayFunction != null ? displayFunction : DEFAULT_DISPLAY;
		this.registered = registered;
		this.min = min;
		this.max = max;
	}
	
	@Override
	public boolean isStatsCapable()
	{
		return true;
	}
	
	@Override
	public Float getValueForEntity(Entity entity)
	{
		return valueFunction.apply(entity);
	}
	
	@Override
	public String transformToString(Float value)
	{
		return displayFunction.apply(value);
	}
	
	@Override
	public boolean isRegistered()
	{
		return registered;
	}
	
	public float getMin()
	{
		return min;
	}
	
	public float getMax()
	{
		return max;
	}
}