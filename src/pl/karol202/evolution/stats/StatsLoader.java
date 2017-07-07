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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pl.karol202.evolution.entity.property.EntityProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.karol202.evolution.simulation.SimulationManager.*;

public class StatsLoader
{
	private class PropertyPair
	{
		private EntityProperties property;
		private float value;
		
		public PropertyPair(EntityProperties property, float value)
		{
			this.property = property;
			this.value = value;
		}
		
		public EntityProperties getProperty()
		{
			return property;
		}
		
		public float getValue()
		{
			return value;
		}
	}
	
	private Stats stats;
	private EntityStatsEventFactory factory;
	
	private Document document;
	
	public StatsLoader()
	{
		stats = Stats.instance;
		factory = new EntityStatsEventFactory();
	}
	
	public void parseStats(Element elementSimulation)
	{
		Element elementStats = getElement(elementSimulation, "stats");
		
		int initialEntitiesAmount = getIntAttribute(elementStats, "initialEntitiesAmount");
		List<Map<EntityProperties, Float>> initialEntitiesProperties = parseInitialEntitiesProperties(elementStats);
		stats.resetStats(initialEntitiesAmount, initialEntitiesProperties);
		stats.setHighestEntitiesAmount(getIntAttribute(elementStats, "highestAmount"));
		stats.setCurrentTime(getFloatAttribute(elementStats, "currentTime"));
		parseEntityEvents(elementStats);
	}
	
	private List<Map<EntityProperties, Float>> parseInitialEntitiesProperties(Element elementStats)
	{
		Element elementInitialEntitiesProperties = getElement(elementStats, "initialEntitiesProperties");
		NodeList mapsNodes = elementInitialEntitiesProperties.getChildNodes();
		
		List<Map<EntityProperties, Float>> mapsList = new ArrayList<>();
		for(int i = 0; i < mapsNodes.getLength(); i++)
		{
			Element elementMap = (Element) mapsNodes.item(i);
			mapsList.add(getEntityProperties(elementMap));
		}
		return mapsList;
	}
	
	private Map<EntityProperties, Float> getEntityProperties(Element elementMap)
	{
		NodeList propertiesNodes = elementMap.getChildNodes();
		return IntStream.range(0, propertiesNodes.getLength())
						.mapToObj(i -> (Element) propertiesNodes.item(i))
						.map(this::getEntityProperty)
						.collect(Collectors.toMap(PropertyPair::getProperty, PropertyPair::getValue));
	}
	
	private PropertyPair getEntityProperty(Element elementProperty)
	{
		EntityProperties property = EntityProperties.values()[getIntAttribute(elementProperty, "property")];
		float value = getFloatAttribute(elementProperty, "value");
		return new PropertyPair(property, value);
	}
	
	private void parseEntityEvents(Element elementStats)
	{
		factory = new EntityStatsEventFactory();
		stats.clearAllEvents();
		Element elementEvents = getElement(elementStats, "events");
		NodeList eventsNodes = elementEvents.getChildNodes();
		for(int i = 0; i < eventsNodes.getLength(); i++)
		{
			Element elementEvent = (Element) eventsNodes.item(i);
			stats.addEntityEvent(parseEntityEvent(elementEvent));
		}
	}
	
	private EntityStatsEvent parseEntityEvent(Element elementEvent)
	{
		String className = elementEvent.getTagName();
		float time = getFloatAttribute(elementEvent, "time");
		Map<EntityProperties, Float> properties = getEntityProperties(elementEvent);
		
		return factory.createEvent(className, time, properties);
	}
	
	public Element getStatsElement(Document document)
	{
		this.document = document;
		return createStatsElement();
	}
	
	private Element createStatsElement()
	{
		Element elementStats = document.createElement("stats");
		setNumberAttribute(elementStats, "initialEntitiesAmount", stats.getInitialEntitiesAmount());
		setNumberAttribute(elementStats, "highestAmount", stats.getHighestEntitiesAmount());
		setNumberAttribute(elementStats, "currentTime", stats.getCurrentTime());
		elementStats.appendChild(createInitialEntitiesPropertiesElement());
		elementStats.appendChild(createEventsElement());
		return elementStats;
	}
	
	private Element createInitialEntitiesPropertiesElement()
	{
		Element elementInitialEntitiesProperties = document.createElement("initialEntitiesProperties");
		stats.getInitialPropertiesMapsStream().map(this::createEntityPropertiesElement).forEach(elementInitialEntitiesProperties::appendChild);
		return elementInitialEntitiesProperties;
	}
	
	private Element createEntityPropertiesElement(Map<EntityProperties, Float> properties)
	{
		Element elementProperties = document.createElement("properties");
		properties.entrySet()
				  .stream()
				  .map(e -> createEntityPropertyElement(new PropertyPair(e.getKey(), e.getValue())))
				  .forEach(elementProperties::appendChild);
		return elementProperties;
	}
	
	private Element createEntityPropertyElement(PropertyPair pair)
	{
		Element elementProperty = document.createElement("property");
		setNumberAttribute(elementProperty, "property", pair.getProperty().ordinal());
		setNumberAttribute(elementProperty, "value", pair.getValue());
		return elementProperty;
	}
	
	private Element createEventsElement()
	{
		Element elementEvents = document.createElement("events");
		stats.getEntityEventsStream().map(this::createEventElement).forEach(elementEvents::appendChild);
		return elementEvents;
	}
	
	private Element createEventElement(EntityStatsEvent event)
	{
		Element elementEvent = document.createElement(event.getClass().getName());
		setNumberAttribute(elementEvent, "time", event.getTime());
		elementEvent.appendChild(createEntityPropertiesElement(event.getEntityPropertiesMap()));
		return elementEvent;
	}
}