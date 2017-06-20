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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.karol202.evolution.utils.SimulationParseException;

public class BehaviourState
{
	private Document document;
	private Element element;
	
	public BehaviourState(Document document, Element element)
	{
		this.document = document;
		this.element = element;
	}
	
	public static BehaviourState createForBehaviour(SavableBehaviour behaviour, Document document)
	{
		Element element = document.createElement("behaviour");
		element.setAttribute("id", String.valueOf(behaviour.getId()));
		
		BehaviourState state = new BehaviourState(document, element);
		behaviour.saveState(state);
		return state;
	}
	
	public int getInt(String key)
	{
		Attr attr = element.getAttributeNode(key);
		if(attr == null) throw new SimulationParseException("Int attribute not found: " + key);
		return Integer.parseInt(attr.getValue());
	}
	
	public void putInt(String key, int value)
	{
		Attr attr = document.createAttribute(key);
		attr.setValue(String.valueOf(value));
		element.setAttributeNode(attr);
	}
	
	public Element getElement()
	{
		return element;
	}
}