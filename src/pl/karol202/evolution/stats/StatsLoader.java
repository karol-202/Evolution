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

import static pl.karol202.evolution.simulation.SimulationManager.*;

public class StatsLoader
{
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
		
		stats.resetStats(getIntAttribute(elementStats, "initialEntitiesAmount"));
		stats.setHighestEntitiesAmount(getIntAttribute(elementStats, "highestAmount"));
		stats.setCurrentTime(getFloatAttribute(elementStats, "currentTime"));
		
		factory = new EntityStatsEventFactory();
		stats.clearAllEvents();
		NodeList eventsNodes = elementStats.getChildNodes();
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
		
		return factory.createEvent(className, time);
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
		stats.getEntityEventsStream().map(this::createEventElement).forEach(elementStats::appendChild);
		return elementStats;
	}
	
	private Element createEventElement(EntityStatsEvent event)
	{
		Element elementEvent = document.createElement(event.getClass().getName());
		setNumberAttribute(elementEvent, "time", event.getTime());
		return elementEvent;
	}
}