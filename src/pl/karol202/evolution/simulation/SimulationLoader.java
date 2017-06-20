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
import pl.karol202.evolution.entity.Entities;
import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.genes.Allele;
import pl.karol202.evolution.genes.Gene;
import pl.karol202.evolution.genes.GeneType;
import pl.karol202.evolution.genes.Genotype;
import pl.karol202.evolution.world.Plant;
import pl.karol202.evolution.world.Plants;
import pl.karol202.evolution.world.World;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class SimulationLoader
{
	private Document document;
	private Element root;
	
	private int version;
	private Simulation simulation;
	private World world;
	private Plants plants;
	private Entities entities;
	
	public void parseSimulation(File file, Simulation simulation) throws IOException, ParserConfigurationException, SAXException
	{
		this.simulation = simulation;
		this.world = simulation.getWorld();
		this.plants = world.getPlants();
		this.entities = world.getEntities();
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		document = builder.parse(file);
		parseRoot();
	}
	
	private void parseRoot()
	{
		root = document.getDocumentElement();
		parseVersion();
		simulation.setTimeStep(getIntAttribute(root, "timeStep"));
		parseWorld();
	}
	
	private void parseVersion()
	{
		version = getIntAttribute(root, "version");
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
		parseEntities(elementWorld);
	}
	
	private void parsePlants(Element worldElement)
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
		float x = getFloatAttribute(plantElement, "x");
		float y = getFloatAttribute(plantElement, "y");
		float health = getFloatAttribute(plantElement, "health");
		return new Plant(x, y, health);
	}
	
	private void parseEntities(Element worldElement)
	{
		Element elementEntities = getElement(worldElement, "entities");
		
		entities.setSelectedEntityIndex(getIntAttribute(elementEntities, "selectedEntity"));
		
		entities.removeAllEntities();
		NodeList entitiesNodes = elementEntities.getChildNodes();
		for(int i = 0; i < entitiesNodes.getLength(); i++)
		{
			Element elementEntity = (Element) entitiesNodes.item(i);
			entities.addEntity(parseEntity(elementEntity));
		}
	}
	
	private Entity parseEntity(Element entityElement)
	{
		float x = getFloatAttribute(entityElement, "x");
		float y = getFloatAttribute(entityElement, "y");
		Genotype genotype = parseGenotype(entityElement);
		//Components and behaviours
		Entity entity = new Entity(entities, x, y, genotype);
		
		return entity;
	}
	
	private Genotype parseGenotype(Element entityElement)
	{
		Element elementGenotype = getElement(entityElement, "genotype");
		
		Genotype genotype = new Genotype(new Random());
		
		NodeList genesNodes = elementGenotype.getChildNodes();
		for(int i = 0; i < genesNodes.getLength(); i++)
		{
			Element elementGene = (Element) genesNodes.item(i);
			genotype.setGene(parseGene(elementGene));
		}
		return genotype;
	}
	
	private Gene parseGene(Element geneElement)
	{
		GeneType type = GeneType.getTypeByName(geneElement.getAttribute("type"));
		int level = getIntAttribute(geneElement, "level");
		Allele alleleA = Allele.getAlleleByName(geneElement.getAttribute("alleleA"));
		Allele alleleB = Allele.getAlleleByName(geneElement.getAttribute("alleleB"));
		return new Gene(type, level, alleleA, alleleB);
	}
	
	private Element getElement(Element parent, String name)
	{
		return (Element) parent.getElementsByTagName(name).item(0);
	}

	private int getIntAttribute(Element element, String name)
	{
		return Integer.parseInt(element.getAttribute(name));
	}
	
	private float getFloatAttribute(Element element, String name)
	{
		return Float.parseFloat(element.getAttribute(name));
	}
}