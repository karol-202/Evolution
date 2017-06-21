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

public class BehaviourState
{
	private Element element;
	
	public BehaviourState(Element element)
	{
		this.element = element;
	}
	
	public static BehaviourState createForBehaviour(SavableBehaviour behaviour, Document document)
	{
		Element element = document.createElement("behaviour");
		element.setAttribute("id", String.valueOf(behaviour.getId()));
		
		BehaviourState state = new BehaviourState(element);
		behaviour.saveState(state);
		return state;
	}
	
	int getInt(String key)
	{
		return Integer.parseInt(element.getAttribute(key));
	}
	
	void putInt(String key, int value)
	{
		element.setAttribute(key, String.valueOf(value));
	}
	
	public Element getElement()
	{
		return element;
	}
}