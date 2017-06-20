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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import pl.karol202.evolution.world.WorldLoader;

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

import static pl.karol202.evolution.simulation.SimulationManager.getIntAttribute;
import static pl.karol202.evolution.simulation.SimulationManager.setNumberAttribute;

class SimulationLoader
{
	static final int VERSION = 1;
	
	private Document document;
	
	private int version;
	private Simulation simulation;
	private WorldLoader worldLoader;
	
	void parseSimulation(File file, Simulation simulation) throws IOException, ParserConfigurationException, SAXException
	{
		this.simulation = simulation;
		this.worldLoader = new WorldLoader(simulation.getWorld());
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		document = builder.parse(file);
		parseRoot();
	}
	
	private void parseRoot()
	{
		Element root = document.getDocumentElement();
		parseVersion(root);
		simulation.setTimeStep(getIntAttribute(root, "timeStep"));
		worldLoader.parseWorld(root);
	}
	
	private void parseVersion(Element elementRoot)
	{
		version = getIntAttribute(elementRoot, "version");
		if(version != VERSION) updateBeforeParsing();
	}
	
	private void updateBeforeParsing()
	{
		
	}
	
	void saveSimulation(Simulation simulation, File file) throws ParserConfigurationException, TransformerException, IOException
	{
		this.simulation = simulation;
		this.worldLoader = new WorldLoader(simulation.getWorld());
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		document = builder.newDocument();
		document.appendChild(createRoot());
		saveToFile(file);
	}
	
	private Element createRoot()
	{
		Element rootSimulation = document.createElement("simulation");
		setNumberAttribute(rootSimulation, "version", VERSION);
		setNumberAttribute(rootSimulation, "timeStep", simulation.getTimeStep());
		rootSimulation.appendChild(worldLoader.getWorldElement(document));
		return rootSimulation;
	}
	
	private void saveToFile(File file) throws TransformerException, IOException
	{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(file);
		transformer.transform(domSource, streamResult);
	}
}