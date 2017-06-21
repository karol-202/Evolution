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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pl.karol202.evolution.utils.Vector2;

public class ComponentState
{
	private Document document;
	private Element element;
	
	ComponentState(Document document, Element element)
	{
		this.document = document;
		this.element = element;
	}
	
	static ComponentState createForComponent(SavableComponent component, Document document)
	{
		Element element = document.createElement(component.getClass().getName());
		ComponentState state = new ComponentState(document, element);
		component.saveState(state);
		return state;
	}
	
	String getString(String key)
	{
		return element.getAttribute(key);
	}
	
	Vector2 getVector(String key)
	{
		Element vectorElement = getVectorElementFromListByName(key);
		if(vectorElement == null) return null;
		return new Vector2(Float.parseFloat(vectorElement.getAttribute("x")), Float.parseFloat(vectorElement.getAttribute("y")));
	}
	
	private Element getVectorElementFromListByName(String name)
	{
		NodeList nodeList = element.getElementsByTagName("vector");
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Element element = (Element) nodeList.item(i);
			String vectorName = element.getAttribute("name");
			if(vectorName.equals(name)) return element;
		}
		return null;
	}
	
	void putString(String key, String value)
	{
		Attr attr = document.createAttribute(key);
		attr.setValue(value);
		element.setAttributeNode(attr);
	}
	
	void putVector(String key, Vector2 value)
	{
		if(value == null) tryToRemoveVector(key);
		else createVectorElement(key, value);
	}
	
	private void tryToRemoveVector(String name)
	{
		Element vectorElement = getVectorElementFromListByName(name);
		if(vectorElement != null) element.removeChild(vectorElement);
	}
	
	private void createVectorElement(String key, Vector2 value)
	{
		Element elementVector = document.createElement("vector");
		elementVector.setAttribute("name", key);
		elementVector.setAttribute("x", String.valueOf(value.getX()));
		elementVector.setAttribute("y", String.valueOf(value.getY()));
		element.appendChild(elementVector);
	}
	
	Element getElement()
	{
		return element;
	}
}