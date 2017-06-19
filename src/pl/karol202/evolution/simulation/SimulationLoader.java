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
package pl.karol202.evolution.simulation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pl.karol202.evolution.world.Plant;
import pl.karol202.evolution.world.Plants;
import pl.karol202.evolution.world.World;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class SimulationLoader
{
	private Document document;
	private Element root;
	
	private int version;
	private Simulation simulation;
	private World world;
	private Plants plants;
	
	public void parseSimulation(File file, Simulation simulation) throws IOException, ParserConfigurationException, SAXException
	{
		this.simulation = simulation;
		this.world = simulation.getWorld();
		this.plants = world.getPlants();
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		document = builder.parse(file);
		parseRoot();
	}
	
	private void parseRoot()
	{
		root = document.getDocumentElement();
		parseVersion();
		simulation.setTimeStep(Integer.parseInt(root.getAttribute("timeStep")));
		parseWorld();
	}
	
	private void parseVersion()
	{
		version = Integer.parseInt(root.getAttribute("version"));
		if(version != SimulationSaver.VERSION) updateBeforeParsing();
	}
	
	private void updateBeforeParsing()
	{
		
	}
	
	private void parseWorld()
	{
		Element elementWorld = getElement(root, "world");
		
		int width = Integer.parseInt(elementWorld.getAttribute("width"));
		int height = Integer.parseInt(elementWorld.getAttribute("height"));
		world.generateEmptyWorld(width, height);
		
		world.setTemperatureFrequency(Integer.parseInt(elementWorld.getAttribute("temperatureFrequency")));
		world.setHumidityFrequency(Integer.parseInt(elementWorld.getAttribute("humidityFrequency")));
		world.setMinTemperature(Float.parseFloat(elementWorld.getAttribute("minTemperature")));
		world.setMaxTemperature(Float.parseFloat(elementWorld.getAttribute("maxTemperature")));
		world.setMinHumidity(Float.parseFloat(elementWorld.getAttribute("minHumidity")));
		world.setMaxHumidity(Float.parseFloat(elementWorld.getAttribute("maxHumidity")));
		world.setEntitiesAmount(Integer.parseInt(elementWorld.getAttribute("entitiesAmount")));
		
		int temperatureXOffset = Integer.parseInt(elementWorld.getAttribute("temperatureXOffset"));
		int temperatureYOffset = Integer.parseInt(elementWorld.getAttribute("temperatureYOffset"));
		int humidityXOffset = Integer.parseInt(elementWorld.getAttribute("humidityXOffset"));
		int humidityYOffset = Integer.parseInt(elementWorld.getAttribute("humidityYOffset"));
		world.generateTemperature(temperatureXOffset, temperatureYOffset);
		world.generateHumidity(humidityXOffset, humidityYOffset);
		
		parsePlants(world.getPlants(), elementWorld);
	}
	
	private void parsePlants(Plants plants, Element worldElement)
	{
		Element elementPlants = getElement(worldElement, "plants");
		
		plants.setNoiseFrequency(Integer.parseInt(elementPlants.getAttribute("noiseFrequency")));
		plants.setLeastMinDistance(Float.parseFloat(elementPlants.getAttribute("leastMinDistance")));
		plants.setGreatestMinDistance(Float.parseFloat(elementPlants.getAttribute("greatestMinDistance")));
		
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
		float x = Float.parseFloat(plantElement.getAttribute("x"));
		float y = Float.parseFloat(plantElement.getAttribute("y"));
		float health = Float.parseFloat(plantElement.getAttribute("health"));
		return new Plant(x, y, health);
	}
	
	private Element getElement(Element parent, String name)
	{
		return (Element) parent.getElementsByTagName(name).item(0);
	}
}