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

import org.w3c.dom.*;
import pl.karol202.evolution.utils.SimulationParseException;
import pl.karol202.evolution.utils.Vector2;

public class ComponentState
{
	private Document document;
	private Element element;
	
	public ComponentState(Document document, Element element)
	{
		this.document = document;
		this.element = element;
	}
	
	public static ComponentState createForComponent(SavableComponent component, Document document)
	{
		Element element = document.createElement(component.getClass().getName());
		ComponentState state = new ComponentState(document, element);
		component.saveState(state);
		return state;
	}
	
	public String getString(String key)
	{
		Attr attr = element.getAttributeNode(key);
		if(attr == null) throw new SimulationParseException("String attribute not found: " + key);
		return attr.getValue();
	}
	
	public Vector2 getVector(String key)
	{
		Element vectorElement = getVectorElementFromListByName(key);
		if(vectorElement == null) throw new SimulationParseException("Vector element not found: " + key);
		Attr attrX = vectorElement.getAttributeNode("x");
		Attr attrY = vectorElement.getAttributeNode("y");
		if(attrX == null || attrY == null) throw new SimulationParseException("Corrupted vector node");
		return new Vector2(Float.parseFloat(attrX.getValue()), Float.parseFloat(attrY.getValue()));
	}
	
	private Element getVectorElementFromListByName(String name)
	{
		NodeList nodeList = element.getElementsByTagName("vector");
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Element element = (Element) nodeList.item(i);
			Attr attrName = element.getAttributeNode("name");
			if(attrName == null) throw new SimulationParseException("Corrupted vector node");
			if(attrName.getValue().equals(name)) return element;
		}
		return null;
	}
	
	public void putString(String key, String value)
	{
		Attr attr = document.createAttribute(key);
		attr.setValue(value);
		element.setAttributeNode(attr);
	}
	
	public void putVector(String key, Vector2 value)
	{
		if(value == null) tryToRemoveVector(key);
		else
		{
			Element elementVector = document.createElement("vector");
			createVectorNameAttribute(key, elementVector);
			createVectorXAttribute(value, elementVector);
			createVectorYAttribute(value, elementVector);
			element.appendChild(elementVector);
		}
	}
	
	private void tryToRemoveVector(String name)
	{
		Element vectorElement = getVectorElementFromListByName(name);
		if(vectorElement != null) element.removeChild(vectorElement);
	}
	
	private void createVectorNameAttribute(String name, Element vectorElement)
	{
		Attr attrVectorName = document.createAttribute("name");
		attrVectorName.setValue(name);
		vectorElement.setAttributeNode(attrVectorName);
	}
	
	private void createVectorXAttribute(Vector2 vector, Element vectorElement)
	{
		Attr attrVectorX = document.createAttribute("x");
		attrVectorX.setValue(String.valueOf(vector.getX()));
		vectorElement.setAttributeNode(attrVectorX);
	}
	
	private void createVectorYAttribute(Vector2 vector, Element vectorElement)
	{
		Attr attrVectorY = document.createAttribute("y");
		attrVectorY.setValue(String.valueOf(vector.getY()));
		vectorElement.setAttributeNode(attrVectorY);
	}
	
	public Element getElement()
	{
		return element;
	}
}