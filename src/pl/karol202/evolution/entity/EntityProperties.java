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

import pl.karol202.evolution.utils.ToFloatFunction;

import java.util.function.Function;

public enum EntityProperties
{
	X("X", Entity::getX, null),
	Y("Y", Entity::getY, null),
	ENERGY("Energia", Entity::getEnergy, null),
	TIME_OF_LIFE("Czas życia", Entity::getTimeOfLife, null),
	REPRODUCE_COOLDOWN("Czas do rozmnażania", Entity::getReproduceCooldown, null),
	BEHAVIOUR("Zachowanie", Entity::getCurrentBehaviourName),
	SEX("Płeć", e -> e.getSex().getName()),
	SIZE("Wielkość", Entity::getSize, null),
	SPEED("Szybkość", Entity::getSpeed, null),
	OPTIMAL_TEMPERATURE("Temperatura optymalna", Entity::getOptimalTemperature, null),
	OPTIMAL_HUMIDITY("Wilgotność optymalna", Entity::getOptimalHumidity, null),
	MAX_TIME_OF_LIFE("Docelowy czas życia", Entity::getMaxTimeOfLife, null),
	ADOLESCENCE_TIME("Czas dojrzewania", Entity::getAdolescenceTime, null),
	MAX_ENERGY("Maksymalna energia", Entity::getMaxEnergy, null),
	ENERGY_PER_SECOND("Użycie energii / s", Entity::getEnergyPerSecond, null),
	TEMPERATURE_ENERGY_LOSS("Spadek energii (temperatura)", e -> String.format("%f / 20°", e.getTemperatureEnergyLoss())),
	HUMIDITY_ENERGY_LOSS("Spadek energii (wilgotność)", e -> String.format("%f / 33%%", e.getHumidityEnergyLoss())),
	EATING_SPEED("Szybkość jedzenia", Entity::getEatingSpeed, null),
	SIGHT_RANGE("Zakres widzenia", Entity::getSightRange, null),
	SMELL("Obecność węchu", e -> e.hasSmell() ? "Tak" : "Nie"),
	SMELL_RANGE("Zakres węchu", e -> e.hasSmell() ? toString(e.getSmellRange()) : "-"),
	EAT_START_ENERGY_THRESHOLD("Energia rozpoczęcia jedzenia", e -> String.format("< %f", e.getEatStartEnergyThreshold())),
	REPRODUCE_READY_ENERGY_THRESHOLD("Energia rozmnażania", e -> String.format(">= %f", e.getReproduceReadyEnergyThreshold())),
	MIN_REPRODUCE_COOLDOWN("Min. czas do rozmnażania", Entity::getMinReproduceCooldown, null),
	MAX_REPRODUCE_COOLDOWN("Maks. czas do rozmnażania", Entity::getMaxReproduceCooldown, null);
	
	private String name;
	private Function<Entity, String> stringFunction;
	private ToFloatFunction<Entity> floatFunction;
	private boolean floatProperty;
	
	EntityProperties(String name, Function<Entity, String> stringFunction)
	{
		this.name = name;
		this.stringFunction = stringFunction;
		this.floatProperty = false;
	}
	
	EntityProperties(String name, ToFloatFunction<Entity> floatFunction, Void v)
	{
		this.name = name;
		this.floatFunction = floatFunction;
		this.floatProperty = true;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getStringValueForEntity(Entity entity)
	{
		if(!floatProperty) return stringFunction.apply(entity);
		else return toString(floatFunction.apply(entity));
	}
	
	public float getFloatValueForEntity(Entity entity)
	{
		if(!floatProperty) return -1f;
		return floatFunction.apply(entity);
	}
	
	public boolean isFloatProperty()
	{
		return floatProperty;
	}
	
	private static String toString(float f)
	{
		return Float.toString(f);
	}
}