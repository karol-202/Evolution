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
package pl.karol202.evolution.world;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pl.karol202.evolution.entity.EntitiesLoader;

import static pl.karol202.evolution.simulation.SimulationManager.*;

public class WorldLoader
{
	private World world;
	private Plants plants;
	private EntitiesLoader entitiesLoader;
	
	private Document document;
	
	public WorldLoader(World world)
	{
		this.world = world;
		this.plants = world.getPlants();
		this.entitiesLoader = new EntitiesLoader(world.getEntities());
	}
	
	public void parseWorld(Element elementSimulation)
	{
		Element elementWorld = getElement(elementSimulation, "world");
		
		int width = getIntAttribute(elementWorld, "width");
		int height = getIntAttribute(elementWorld, "height");
		world.generateEmptyWorld(width, height);
		
		world.setTemperatureFrequency(getIntAttribute(elementWorld, "temperatureFrequency"));
		world.setHumidityFrequency(getIntAttribute(elementWorld, "humidityFrequency"));
		world.setMinTemperature(getFloatAttribute(elementWorld, "minTemperature"));
		world.setMaxTemperature(getFloatAttribute(elementWorld, "maxTemperature"));
		world.setMinHumidity(getFloatAttribute(elementWorld, "minHumidity"));
		world.setMaxHumidity(getFloatAttribute(elementWorld, "maxHumidity"));
		world.setEntitiesAmount(getIntAttribute(elementWorld, "entitiesAmount"));
		
		int temperatureXOffset = getIntAttribute(elementWorld, "temperatureXOffset");
		int temperatureYOffset = getIntAttribute(elementWorld, "temperatureYOffset");
		int humidityXOffset = getIntAttribute(elementWorld, "humidityXOffset");
		int humidityYOffset = getIntAttribute(elementWorld, "humidityYOffset");
		world.generateTemperature(temperatureXOffset, temperatureYOffset);
		world.generateHumidity(humidityXOffset, humidityYOffset);
		
		parsePlants(elementWorld);
		entitiesLoader.parseEntities(elementWorld);
	}
	
	private void parsePlants(Element worldElement)
	{
		Element elementPlants = getElement(worldElement, "plants");
		
		plants.setNoiseFrequency(getIntAttribute(elementPlants, "noiseFrequency"));
		plants.setLeastMinDistance(getFloatAttribute(elementPlants, "leastMinDistance"));
		plants.setGreatestMinDistance(getFloatAttribute(elementPlants, "greatestMinDistance"));
		
		plants.removePlants();
		NodeList plantsNodes = elementPlants.getChildNodes();
		for(int i = 0; i < plantsNodes.getLength(); i++)
		{
			Element elementPlant = (Element) plantsNodes.item(i);
			plants.addPlant(parsePlant(elementPlant));
		}
	}
	
	private Plant parsePlant(Element plantElement)
	{
		float x = getFloatAttribute(plantElement, "x");
		float y = getFloatAttribute(plantElement, "y");
		float health = getFloatAttribute(plantElement, "health");
		return new Plant(x, y, health);
	}
	
	public Element getWorldElement(Document document)
	{
		this.document = document;
		return createWorldElement();
	}
	
	private Element createWorldElement()
	{
		Element elementWorld = document.createElement("world");
		setNumberAttribute(elementWorld, "width", world.getWidth());
		setNumberAttribute(elementWorld, "height", world.getHeight());
		setNumberAttribute(elementWorld, "temperatureFrequency", world.getTemperatureFrequency());
		setNumberAttribute(elementWorld, "humidityFrequency", world.getHumidityFrequency());
		setNumberAttribute(elementWorld, "minTemperature", world.getMinTemperature());
		setNumberAttribute(elementWorld, "maxTemperature", world.getMaxTemperature());
		setNumberAttribute(elementWorld, "minHumidity", world.getMinHumidity());
		setNumberAttribute(elementWorld, "maxHumidity", world.getMaxHumidity());
		setNumberAttribute(elementWorld, "entitiesAmount", world.getInitialEntitiesAmount());
		setNumberAttribute(elementWorld, "temperatureXOffset", world.getTemperatureXOffset());
		setNumberAttribute(elementWorld, "temperatureYOffset", world.getTemperatureYOffset());
		setNumberAttribute(elementWorld, "humidityXOffset", world.getHumidityXOffset());
		setNumberAttribute(elementWorld, "humidityYOffset", world.getHumidityYOffset());
		elementWorld.appendChild(createPlantsElement());
		elementWorld.appendChild(entitiesLoader.getEntitiesElement(document));
		return elementWorld;
	}
	
	private Element createPlantsElement()
	{
		Element elementPlants = document.createElement("plants");
		setNumberAttribute(elementPlants, "noiseFrequency", plants.getNoiseFrequency());
		setNumberAttribute(elementPlants, "leastMinDistance", plants.getLeastMinDistance());
		setNumberAttribute(elementPlants, "greatestMinDistance", plants.getGreatestMinDistance());
		plants.getPlantsStream().map(this::createPlantElement).forEach(elementPlants::appendChild);
		return elementPlants;
	}
	
	private Element createPlantElement(Plant plant)
	{
		Element elementPlant = document.createElement("plant");
		setNumberAttribute(elementPlant, "x", plant.getX());
		setNumberAttribute(elementPlant, "y", plant.getY());
		setNumberAttribute(elementPlant, "health", plant.getHealth());
		return elementPlant;
	}
}