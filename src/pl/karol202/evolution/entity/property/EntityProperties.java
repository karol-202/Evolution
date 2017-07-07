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

import com.sun.istack.internal.Nullable;
import pl.karol202.evolution.entity.Entity;

import java.util.function.Function;

public enum EntityProperties
{
	X("X", Entity::getX, null, false),
	Y("Y", Entity::getY, null, false),
	ENERGY("Energia", Entity::getEnergy, null, false),
	TIME_OF_LIFE("Czas życia", Entity::getTimeOfLife, null, false),
	REPRODUCE_COOLDOWN("Czas do rozmnażania", Entity::getReproduceCooldown, null, false),
	BEHAVIOUR("Zachowanie", Entity::getCurrentBehaviourName),
	SEX("Płeć", e -> e.getSex().getName()),
	SIZE("Wielkość", Entity::getSize, null, true),
	SPEED("Szybkość", Entity::getSpeed, null, true),
	OPTIMAL_TEMPERATURE("Temperatura optymalna", Entity::getOptimalTemperature, null, true),
	OPTIMAL_HUMIDITY("Wilgotność optymalna", Entity::getOptimalHumidity, null, true),
	MAX_TIME_OF_LIFE("Docelowy czas życia", Entity::getMaxTimeOfLife, null, true),
	ADOLESCENCE_TIME("Czas dojrzewania", Entity::getAdolescenceTime, null, true),
	MAX_ENERGY("Maksymalna energia", Entity::getMaxEnergy, null, true),
	ENERGY_PER_SECOND("Użycie energii / s", Entity::getEnergyPerSecond, null, true),
	TEMPERATURE_ENERGY_LOSS("Spadek energii (temperatura)", Entity::getTemperatureEnergyLoss, f -> String.format("%f / 20°", f), true),
	HUMIDITY_ENERGY_LOSS("Spadek energii (wilgotność)", Entity::getHumidityEnergyLoss, f -> String.format("%f / 33%%", f), true),
	EATING_SPEED("Szybkość jedzenia", Entity::getEatingSpeed, null, true),
	SIGHT_RANGE("Zakres widzenia", Entity::getSightRange, null, true),
	SMELL("Obecność węchu", e -> e.hasSmell() ? "Tak" : "Nie"),
	SMELL_RANGE("Zakres węchu", Entity::getSmellRange, f -> f != 0f ? toString(f) : "-", true),
	EAT_START_ENERGY_THRESHOLD("Energia rozpoczęcia jedzenia", Entity::getEatStartEnergyThreshold, f -> String.format("< %f", f), true),
	REPRODUCE_READY_ENERGY_THRESHOLD("Energia rozmnażania", Entity::getReproduceReadyEnergyThreshold, f -> String.format(">= %f", f), true),
	MIN_REPRODUCE_COOLDOWN("Min. czas do rozmnażania", Entity::getMinReproduceCooldown, null, true),
	MAX_REPRODUCE_COOLDOWN("Maks. czas do rozmnażania", Entity::getMaxReproduceCooldown, null, true);
	
	private String name;
	private EntityPropertyAdapter adapter;
	
	EntityProperties(String name, Function<Entity, String> stringFunction)
	{
		this.name = name;
		this.adapter = new EntityStringPropertyAdapter(stringFunction);
	}
	
	EntityProperties(String name, Function<Entity, Float> floatFunction, @Nullable Function<Float, String> displayFunction, boolean registered)
	{
		this.name = name;
		this.adapter = new EntityFloatPropertyAdapter(floatFunction, displayFunction, registered);
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isStatsCapable()
	{
		return adapter.isStatsCapable();
	}
	
	public boolean isFloatProperty()
	{
		return adapter instanceof EntityFloatPropertyAdapter;
	}
	
	public String getStringValueForEntity(Entity entity)
	{
		return adapter.getStringValueForEntity(entity);
	}
	
	public float getFloatValueForEntity(Entity entity)
	{
		if(!isFloatProperty()) throw new RuntimeException("Cannot return float value for non-float property.");
		EntityFloatPropertyAdapter floatAdapter = (EntityFloatPropertyAdapter) adapter;
		return floatAdapter.getValueForEntity(entity);
	}
	
	public String transformFloatToString(float value)
	{
		if(!isFloatProperty()) throw new RuntimeException("Cannot return float value for non-float property.");
		EntityFloatPropertyAdapter floatAdapter = (EntityFloatPropertyAdapter) adapter;
		return floatAdapter.transformToString(value);
	}
	
	public boolean isRegistered()
	{
		return adapter.isRegistered();
	}
	
	private static String toString(float f)
	{
		return Float.toString(f);
	}
}