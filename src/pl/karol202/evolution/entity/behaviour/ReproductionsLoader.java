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
package pl.karol202.evolution.entity.behaviour;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pl.karol202.evolution.entity.Entities;
import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.utils.SimulationParseException;

import java.util.ArrayList;
import java.util.HashMap;

import static pl.karol202.evolution.simulation.SimulationManager.*;

public class ReproductionsLoader
{
	static ReproductionsLoader instance;
	
	private Entities entities;
	private ArrayList<Reproduction> reproductions;
	
	private Document document;
	
	public ReproductionsLoader(Entities entities)
	{
		ReproductionsLoader.instance = this;
		
		this.entities = entities;
		this.reproductions = new ArrayList<>();
	}
	
	public void parseReproductions(Element elementReproductions)
	{
		HashMap<Integer, Reproduction> reproductionsMap = new HashMap<>();
		NodeList reproductionsNodes = elementReproductions.getChildNodes();
		for(int i = 0; i < reproductionsNodes.getLength(); i++)
		{
			Element elementReproduction = (Element) reproductionsNodes.item(i);
			int id = getIntAttribute(elementReproduction, "id");
			Reproduction reproduction = parseReproduction(elementReproduction);
			
			if(reproductionsMap.containsKey(id)) throw new SimulationParseException("Double reproduction object.");
			reproductionsMap.put(id, reproduction);
		}
		reproductionsMap.forEach((i, r) -> reproductions.add(i, r));
	}
	
	private Reproduction parseReproduction(Element elementReproduction)
	{
		int entityAId = getIntAttribute(elementReproduction, "entityA");
		int entityBId = getIntAttribute(elementReproduction, "entityB");
		Entity entityA = entities.getEntityById(entityAId);
		Entity entityB = entities.getEntityById(entityBId);
		float reamingTime = getFloatAttribute(elementReproduction, "reamingTime");
		
		return new Reproduction(entities, entityA, entityB, reamingTime);
	}
	
	public Element getReproductionsElement(Document document)
	{
		this.document = document;
		return createReproductionsElement();
	}
	
	private Element createReproductionsElement()
	{
		Element elementReproductions = document.createElement("reproductions");
		
		for(int i = 0; i < reproductions.size(); i++)
		{
			Reproduction reproduction = reproductions.get(i);
			elementReproductions.appendChild(createReproductionElement(reproduction, i));
		}
		return elementReproductions;
	}
	
	private Element createReproductionElement(Reproduction reproduction, int id)
	{
		Element elementReproduction = document.createElement("reproduction");
		setNumberAttribute(elementReproduction, "id", id);
		setNumberAttribute(elementReproduction, "entityA", entities.getEntityId(reproduction.getEntityA()));
		setNumberAttribute(elementReproduction, "entityB", entities.getEntityId(reproduction.getEntityB()));
		setNumberAttribute(elementReproduction, "reamingTime", reproduction.getReamingTime());
		return elementReproduction;
	}
	
	Reproduction loadReproduction(int id)
	{
		if(id == -1) return null;
		return reproductions.get(id);
	}
	
	int saveReproduction(Reproduction reproduction)
	{
		if(reproduction == null) return -1;
		if(!reproductions.contains(reproduction)) reproductions.add(reproduction);
		return reproductions.indexOf(reproduction);
	}
}