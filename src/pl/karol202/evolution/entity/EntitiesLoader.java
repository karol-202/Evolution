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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pl.karol202.evolution.entity.behaviour.BehaviourState;
import pl.karol202.evolution.entity.behaviour.ReproductionsLoader;
import pl.karol202.evolution.entity.behaviour.SavableBehaviour;
import pl.karol202.evolution.genes.GenesLoader;
import pl.karol202.evolution.genes.Genotype;
import pl.karol202.evolution.utils.Vector2;

import java.util.HashMap;
import java.util.Map;

import static pl.karol202.evolution.simulation.SimulationManager.*;

public class EntitiesLoader
{
	private Entities entities;
	private GenesLoader genesLoader;
	private ReproductionsLoader reproductionsLoader;
	
	private Document document;
	
	public EntitiesLoader(Entities entities)
	{
		this.entities = entities;
		this.genesLoader = new GenesLoader();
		this.reproductionsLoader = new ReproductionsLoader(entities);
	}
	
	public void parseEntities(Element elementWorld)
	{
		Element elementEntities = getElement(elementWorld, "entities");
		
		Element elementReproductions = getElement(elementEntities, "reproductions");
		
		entities.removeAllEntities();
		Map<Element, Entity> entitiesMap = new HashMap<>();
		NodeList entitiesNodes = elementEntities.getChildNodes();
		for(int i = 0; i < entitiesNodes.getLength(); i++)
		{
			Element element = (Element) entitiesNodes.item(i);
			if(element == elementReproductions) continue;
			
			Entity entity = parseEntity(element);
			boolean selected = getBooleanAttribute(element, "selected");
			
			entities.addEntityInstantly(entity, selected);
			entitiesMap.put(element, entity);
		}

		reproductionsLoader.parseReproductions(elementReproductions);
		
		for(Map.Entry<Element, Entity> entry : entitiesMap.entrySet())
		{
			Element element = entry.getKey();
			Entity entity = entry.getValue();
			
			parseEntityComponents(entity, element);
			parseEntityBehaviours(entity, element);
		}
	}
	
	private Entity parseEntity(Element elementEntity)
	{
		float x = getFloatAttribute(elementEntity, "x");
		float y = getFloatAttribute(elementEntity, "y");
		Genotype genotype = genesLoader.parseGenotype(elementEntity);
		Vector2 bornPosition = parseEntityBornPosition(elementEntity);
		
		Entity entity = new Entity(entities, x, y, genotype, bornPosition);
		entity.setEnergy(getFloatAttribute(elementEntity, "energy"));
		entity.setTimeOfLife(getFloatAttribute(elementEntity, "timeOfLife"));
		entity.setReproduceCooldown(getFloatAttribute(elementEntity, "reproduceCooldown"));
		
		return entity;
	}
	
	private Vector2 parseEntityBornPosition(Element elementEntity)
	{
		Element elementVector = getElement(elementEntity, "bornPosition");
		
		float x = getFloatAttribute(elementVector, "x");
		float y = getFloatAttribute(elementVector, "y");
		
		return new Vector2(x, y);
	}
	
	private void parseEntityComponents(Entity entity, Element elementEntity)
	{
		Element elementComponents = getElement(elementEntity, "components");
		
		NodeList componentsNodes = elementComponents.getChildNodes();
		for(int i = 0; i < componentsNodes.getLength(); i++)
		{
			Element elementComponent = (Element) componentsNodes.item(i);
			parseEntityComponent(entity, elementComponent);
		}
	}
	
	private void parseEntityComponent(Entity entity, Element elementComponent)
	{
		ComponentState state = new ComponentState(document, elementComponent);
		
		String componentName = elementComponent.getTagName();
		entity.getSavableComponentsStream().filter(sc -> sc.getClass().getName().equals(componentName))
										   .forEach(sc -> sc.loadState(state));
	}
	
	private void parseEntityBehaviours(Entity entity, Element elementEntity)
	{
		Element elementBehaviours = getElement(elementEntity, "behaviours");
		entity.setCurrentBehaviourId(getIntAttribute(elementBehaviours, "currentBehaviour"));
		
		NodeList behavioursList = elementBehaviours.getChildNodes();
		for(int i = 0; i < behavioursList.getLength(); i++)
		{
			Element elementBehaviour = (Element) behavioursList.item(i);
			parseEntityBehaviour(entity, elementBehaviour);
		}
	}
	
	private void parseEntityBehaviour(Entity entity, Element elementBehaviour)
	{
		BehaviourState state = new BehaviourState(elementBehaviour);
		
		int behaviourId = getIntAttribute(elementBehaviour, "id");
		entity.getSavableBehavioursStream().filter(sb -> sb.getId() == behaviourId).forEach(sb -> sb.loadState(state));
	}
	
	public Element getEntitiesElement(Document document)
	{
		this.document = document;
		return createEntitiesElement();
	}
	
	private Element createEntitiesElement()
	{
		Element elementEntities = document.createElement("entities");
		entities.getEntitiesStream().map(this::createEntityElement).forEach(elementEntities::appendChild);
		elementEntities.appendChild(reproductionsLoader.getReproductionsElement(document));
		
		return elementEntities;
	}
	
	private Element createEntityElement(Entity entity)
	{
		Element elementEntity = document.createElement("entity");
		setBooleanAttribute(elementEntity, "selected", entities.isEntitySelected(entity));
		setNumberAttribute(elementEntity, "x", entity.getX());
		setNumberAttribute(elementEntity, "y", entity.getY());
		setNumberAttribute(elementEntity, "energy", entity.getEnergy());
		setNumberAttribute(elementEntity, "timeOfLife", entity.getTimeOfLife());
		setNumberAttribute(elementEntity, "reproduceCooldown", entity.getReproduceCooldown());
		elementEntity.appendChild(genesLoader.getGenotypeElement(document, entity.getGenotype()));
		elementEntity.appendChild(createEntityBornPosition(entity.getBornPosition()));
		elementEntity.appendChild(createEntityComponentsElement(entity));
		elementEntity.appendChild(createEntityBehavioursElement(entity));
		return elementEntity;
	}
	
	private Element createEntityBornPosition(Vector2 bornPosition)
	{
		Element elementBornPosition = document.createElement("bornPosition");
		setNumberAttribute(elementBornPosition, "x", bornPosition.getX());
		setNumberAttribute(elementBornPosition, "y", bornPosition.getY());
		return elementBornPosition;
	}
	
	private Element createEntityComponentsElement(Entity entity)
	{
		Element elementComponents = document.createElement("components");
		entity.getSavableComponentsStream().map(this::createComponentElement).forEach(elementComponents::appendChild);
		return elementComponents;
	}
	
	private Element createComponentElement(SavableComponent component)
	{
		ComponentState state = ComponentState.createForComponent(component, document);
		return state.getElement();
	}
	
	private Element createEntityBehavioursElement(Entity entity)
	{
		Element elementBehaviours = document.createElement("behaviours");
		setNumberAttribute(elementBehaviours, "currentBehaviour", entity.getCurrentBehaviourId());
		entity.getSavableBehavioursStream().map(this::createBehaviourElement).forEach(elementBehaviours::appendChild);
		return elementBehaviours;
	}
	
	private Element createBehaviourElement(SavableBehaviour behaviour)
	{
		BehaviourState state = BehaviourState.createForBehaviour(behaviour, document);
		return state.getElement();
	}
}