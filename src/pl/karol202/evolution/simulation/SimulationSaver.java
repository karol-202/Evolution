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
	private File file;
	
	private Document document;
	private Element rootSimulation;
	
	public void saveSimulation(Simulation simulation, File file) throws ParserConfigurationException, TransformerException, IOException
	{
		this.simulation = simulation;
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
		createTimeStepElement();
		document.appendChild(rootSimulation);
	}
	
	private void saveVersion()
	{
		Attr attrVersion = document.createAttribute("version");
		attrVersion.setValue(String.valueOf(VERSION));
		rootSimulation.setAttributeNode(attrVersion);
	}
	
	private void createTimeStepElement()
	{
		Element elementTimeStep = document.createElement("timeStep");
		elementTimeStep.setTextContent(String.valueOf(simulation.getTimeStep()));
		rootSimulation.appendChild(elementTimeStep);
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