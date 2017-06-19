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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.karol202.evolution.entity.ComponentState;
import pl.karol202.evolution.entity.Entities;
import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.entity.SavableComponent;
import pl.karol202.evolution.entity.behaviour.BehaviourState;
import pl.karol202.evolution.entity.behaviour.SavableBehaviour;
import pl.karol202.evolution.genes.Gene;
import pl.karol202.evolution.genes.GeneType;
import pl.karol202.evolution.genes.Genotype;
import pl.karol202.evolution.world.Plant;
import pl.karol202.evolution.world.Plants;
import pl.karol202.evolution.world.World;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class SimulationSaver
{
	public static final int VERSION = 1;
	
	private Simulation simulation;
	private World world;
	private Plants plants;
	private Entities entities;
	private File file;
	
	private Document document;
	private Element rootSimulation;
	private Element elementWorld;
	private Element elementPlants;
	private Element elementEntities;
	
	public void saveSimulation(Simulation simulation, File file) throws ParserConfigurationException, TransformerException, IOException
	{
		this.simulation = simulation;
		this.world = simulation.getWorld();
		this.plants = world.getPlants();
		this.entities = world.getEntities();
		this.file = file;
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		document = builder.newDocument();
		
		createRoot();
		saveToFile();
	}
	
	private void createRoot()
	{
		rootSimulation = document.createElement("simulation");
		saveVersion();
		createTimeStepAttribute();
		createWorldElement();
		document.appendChild(rootSimulation);
	}
	
	private void saveVersion()
	{
		Attr attrVersion = document.createAttribute("version");
		attrVersion.setValue(String.valueOf(VERSION));
		rootSimulation.setAttributeNode(attrVersion);
	}
	
	private void createTimeStepAttribute()
	{
		Attr attrTimeStep = document.createAttribute("timeStep");
		attrTimeStep.setValue(String.valueOf(simulation.getTimeStep()));
		rootSimulation.setAttributeNode(attrTimeStep);
	}
	
	private void createWorldElement()
	{
		elementWorld = document.createElement("world");
		createWidthAttribute();
		createHeightAttribute();
		createTemperatureFrequencyAttribute();
		createHumidityFrequencyAttribute();
		createMinTemperatureAttribute();
		createMaxTemperatureAttribute();
		createMinHumidityAttribute();
		createMaxHumidityAttribute();
		createEntitiesAmountAttribute();
		createTemperatureXOffsetAttribute();
		createTemperatureYOffsetAttribute();
		createHumidityXOffsetAttribute();
		createHumidityYOffsetAttribute();
		createPlantsElement();
		createEntitiesElement();
		rootSimulation.appendChild(elementWorld);
	}
	
	private void createWidthAttribute()
	{
		Attr attrWidth = document.createAttribute("width");
		attrWidth.setValue(String.valueOf(world.getWidth()));
		elementWorld.setAttributeNode(attrWidth);
	}
	
	private void createHeightAttribute()
	{
		Attr attrHeight = document.createAttribute("height");
		attrHeight.setValue(String.valueOf(world.getHeight()));
		elementWorld.setAttributeNode(attrHeight);
	}
	
	private void createTemperatureFrequencyAttribute()
	{
		Attr attrTemperatureFrequency = document.createAttribute("temperatureFrequency");
		attrTemperatureFrequency.setValue(String.valueOf(world.getTemperatureFrequency()));
		elementWorld.setAttributeNode(attrTemperatureFrequency);
	}
	
	private void createHumidityFrequencyAttribute()
	{
		Attr attrHumidityFrequency = document.createAttribute("humidityFrequency");
		attrHumidityFrequency.setValue(String.valueOf(world.getHumidityFrequency()));
		elementWorld.setAttributeNode(attrHumidityFrequency);
	}
	
	private void createMinTemperatureAttribute()
	{
		Attr attrMinTemperature = document.createAttribute("minTemperature");
		attrMinTemperature.setValue(String.valueOf(world.getMinTemperature()));
		elementWorld.setAttributeNode(attrMinTemperature);
	}
	
	private void createMaxTemperatureAttribute()
	{
		Attr attrMaxTemperature = document.createAttribute("maxTemperature");
		attrMaxTemperature.setValue(String.valueOf(world.getMaxTemperature()));
		elementWorld.setAttributeNode(attrMaxTemperature);
	}
	
	private void createMinHumidityAttribute()
	{
		Attr attrMinHumidity = document.createAttribute("minHumidity");
		attrMinHumidity.setValue(String.valueOf(world.getMinHumidity()));
		elementWorld.setAttributeNode(attrMinHumidity);
	}
	
	private void createMaxHumidityAttribute()
	{
		Attr attrMaxHumidity = document.createAttribute("maxHumidity");
		attrMaxHumidity.setValue(String.valueOf(world.getMaxHumidity()));
		elementWorld.setAttributeNode(attrMaxHumidity);
	}
	
	private void createEntitiesAmountAttribute()
	{
		Attr attrEntitiesAmount = document.createAttribute("entitiesAmount");
		attrEntitiesAmount.setValue(String.valueOf(world.getInitialEntitiesAmount()));
		elementWorld.setAttributeNode(attrEntitiesAmount);
	}
	
	private void createTemperatureXOffsetAttribute()
	{
		Attr attrTemperatureXOffset = document.createAttribute("temperatureXOffset");
		attrTemperatureXOffset.setValue(String.valueOf(world.getTemperatureXOffset()));
		elementWorld.setAttributeNode(attrTemperatureXOffset);
	}
	
	private void createTemperatureYOffsetAttribute()
	{
		Attr attrTemperatureYOffset = document.createAttribute("temperatureYOffset");
		attrTemperatureYOffset.setValue(String.valueOf(world.getTemperatureYOffset()));
		elementWorld.setAttributeNode(attrTemperatureYOffset);
	}
	
	private void createHumidityXOffsetAttribute()
	{
		Attr attrHumidityXOffset = document.createAttribute("humidityXOffset");
		attrHumidityXOffset.setValue(String.valueOf(world.getHumidityXOffset()));
		elementWorld.setAttributeNode(attrHumidityXOffset);
	}
	
	private void createHumidityYOffsetAttribute()
	{
		Attr attrHumidityYOffset = document.createAttribute("humidityYOffset");
		attrHumidityYOffset.setValue(String.valueOf(world.getHumidityYOffset()));
		elementWorld.setAttributeNode(attrHumidityYOffset);
	}
	
	private void createPlantsElement()
	{
		elementPlants = document.createElement("plants");
		createPlantsNoiseFrequencyAttribute();
		createLeastMinPlantsDistanceAttribute();
		createGreatestMinPlantsDistanceAttribute();
		plants.getPlantsStream().forEach(this::createPlantElement);
		elementWorld.appendChild(elementPlants);
	}
	
	private void createPlantsNoiseFrequencyAttribute()
	{
		Attr attrPlantsNoiseFrequency = document.createAttribute("noiseFrequency");
		attrPlantsNoiseFrequency.setValue(String.valueOf(plants.getNoiseFrequency()));
		elementPlants.setAttributeNode(attrPlantsNoiseFrequency);
	}
	
	private void createLeastMinPlantsDistanceAttribute()
	{
		Attr attrLeastMinPlantsDistance = document.createAttribute("leastMinDistance");
		attrLeastMinPlantsDistance.setValue(String.valueOf(plants.getLeastMinDistance()));
		elementPlants.setAttributeNode(attrLeastMinPlantsDistance);
	}
	
	private void createGreatestMinPlantsDistanceAttribute()
	{
		Attr attrGreatestMinPlantsDistance = document.createAttribute("greatestMinDistance");
		attrGreatestMinPlantsDistance.setValue(String.valueOf(plants.getGreatestMinDistance()));
		elementPlants.setAttributeNode(attrGreatestMinPlantsDistance);
	}
	
	private void createPlantElement(Plant plant)
	{
		Element elementPlant = document.createElement("plant");
		createPlantXAttribute(plant, elementPlant);
		createPlantYAttribute(plant, elementPlant);
		createPlantHealthAttribute(plant, elementPlant);
		elementPlants.appendChild(elementPlant);
	}
	
	private void createPlantXAttribute(Plant plant, Element plantElement)
	{
		Attr attrPlantX = document.createAttribute("x");
		attrPlantX.setValue(String.valueOf(plant.getX()));
		plantElement.setAttributeNode(attrPlantX);
	}
	
	private void createPlantYAttribute(Plant plant, Element plantElement)
	{
		Attr attrPlantY = document.createAttribute("y");
		attrPlantY.setValue(String.valueOf(plant.getY()));
		plantElement.setAttributeNode(attrPlantY);
	}
	
	private void createPlantHealthAttribute(Plant plant, Element plantElement)
	{
		Attr attrPlantHealth = document.createAttribute("health");
		attrPlantHealth.setValue(String.valueOf(plant.getHealth()));
		plantElement.setAttributeNode(attrPlantHealth);
	}
	
	private void createEntitiesElement()
	{
		elementEntities = document.createElement("entites");
		createSelectedEntityAttribute();
		entities.getEntitiesStream().forEach(this::createEntityElement);
		elementWorld.appendChild(elementEntities);
	}
	
	private void createSelectedEntityAttribute()
	{
		Attr attrSelectedEntity = document.createAttribute("selectedEntity");
		attrSelectedEntity.setValue(String.valueOf(entities.getSelectedEntityIndex()));
		elementEntities.setAttributeNode(attrSelectedEntity);
	}
	
	private void createEntityElement(Entity entity)
	{
		Element elementEntity = document.createElement("entity");
		createEntityXAttribute(entity, elementEntity);
		createEntityYAttribute(entity, elementEntity);
		createEntityEnergyAttribute(entity, elementEntity);
		createGenotypeElement(entity.getGenotype(), elementEntity);
		createEntityComponentsElement(entity, elementEntity);
		createEntityBehavioursElement(entity, elementEntity);
		elementEntities.appendChild(elementEntity);
	}
	
	private void createEntityXAttribute(Entity entity, Element entityElement)
	{
		Attr attrEntityX = document.createAttribute("x");
		attrEntityX.setValue(String.valueOf(entity.getX()));
		entityElement.setAttributeNode(attrEntityX);
	}
	
	private void createEntityYAttribute(Entity entity, Element entityElement)
	{
		Attr attrEntityY = document.createAttribute("y");
		attrEntityY.setValue(String.valueOf(entity.getY()));
		entityElement.setAttributeNode(attrEntityY);
	}
	
	private void createEntityEnergyAttribute(Entity entity, Element entityElement)
	{
		Attr attrEntityEnergy = document.createAttribute("energy");
		attrEntityEnergy.setValue(String.valueOf(entity.getEnergy()));
		entityElement.setAttributeNode(attrEntityEnergy);
	}
	
	private void createGenotypeElement(Genotype genotype, Element entityElement)
	{
		Element elementGenotype = document.createElement("genotype");
		processAllGenesInGenotype(genotype, elementGenotype);
		entityElement.appendChild(elementGenotype);
	}
	
	private void processAllGenesInGenotype(Genotype genotype, Element genotypeElement)
	{
		for(GeneType type : GeneType.values())
			for(int level = 0; level < type.getLevels(); level++)
			{
				Gene gene = genotype.getGeneOfTypeAndLevel(type, level);
				createGeneElement(gene, genotypeElement);
			}
	}
	
	private void createGeneElement(Gene gene, Element genotypeElement)
	{
		Element elementGene = document.createElement("gene");
		createGeneTypeAttribute(gene, elementGene);
		createGeneLevelAttribute(gene, elementGene);
		createGeneAlleleAAttribute(gene, elementGene);
		createGeneAlleleBAttribute(gene, elementGene);
		genotypeElement.appendChild(elementGene);
	}
	
	private void createGeneTypeAttribute(Gene gene, Element geneElement)
	{
		Attr attrGeneType = document.createAttribute("type");
		attrGeneType.setValue(gene.getType().name());
		geneElement.setAttributeNode(attrGeneType);
	}
	
	private void createGeneLevelAttribute(Gene gene, Element geneElement)
	{
		Attr attrGeneLevel = document.createAttribute("level");
		attrGeneLevel.setValue(String.valueOf(gene.getLevel()));
		geneElement.setAttributeNode(attrGeneLevel);
	}
	
	private void createGeneAlleleAAttribute(Gene gene, Element geneElement)
	{
		Attr attrGeneAlleleA = document.createAttribute("alleleA");
		attrGeneAlleleA.setValue(gene.getAlleleA().name());
		geneElement.setAttributeNode(attrGeneAlleleA);
	}
	
	private void createGeneAlleleBAttribute(Gene gene, Element geneElement)
	{
		Attr attrGeneAlleleB = document.createAttribute("alleleB");
		attrGeneAlleleB.setValue(gene.getAlleleB().name());
		geneElement.setAttributeNode(attrGeneAlleleB);
	}
	
	private void createEntityComponentsElement(Entity entity, Element entityElement)
	{
		Element elementComponents = document.createElement("components");
		entity.getSavableComponentsStream().forEach(sc -> createComponentElement(sc, elementComponents));
		entityElement.appendChild(elementComponents);
	}
	
	private void createComponentElement(SavableComponent component, Element componentsElement)
	{
		ComponentState state = ComponentState.createForComponent(component, document);
		componentsElement.appendChild(state.getElement());
	}
	
	private void createEntityBehavioursElement(Entity entity, Element entityElement)
	{
		Element elementBehaviours = document.createElement("behaviours");
		createCurrentBehaviourAttribute(entity, elementBehaviours);
		entity.getSavableBehavioursStream().forEach(sb -> createBehaviourElement(sb, elementBehaviours));
		entityElement.appendChild(elementBehaviours);
	}
	
	private void createCurrentBehaviourAttribute(Entity entity, Element behavioursElement)
	{
		Attr attrCurrentBehaviour = document.createAttribute("currentBehaviour");
		attrCurrentBehaviour.setValue(String.valueOf(entity.getCurrentBehaviourId()));
		behavioursElement.setAttributeNode(attrCurrentBehaviour);
	}
	
	private void createBehaviourElement(SavableBehaviour behaviour, Element entityElement)
	{
		BehaviourState state = BehaviourState.createForBehaviour(behaviour, document);
		entityElement.appendChild(state.getElement());
	}
	
	private void saveToFile() throws TransformerException, IOException
	{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(file);
		transformer.transform(domSource, streamResult);
	}
}