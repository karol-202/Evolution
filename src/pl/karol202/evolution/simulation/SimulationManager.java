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

import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SimulationManager
{
	private Simulation simulation;
	private World world;
	private SimulationLoader loader;
	
	public SimulationManager(Simulation simulation)
	{
		this.simulation = simulation;
		this.world = simulation.getWorld();
		this.loader = new SimulationLoader();
	}
	
	public void newSimulation()
	{
		world.generateRandomWorld(world.getWidth(), world.getHeight());
		simulation.reset();
	}
	
	public void openSimulation(Component parentForDialog)
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new SimulationFileFilter());
		int result = fileChooser.showOpenDialog(parentForDialog);
		if(result == JFileChooser.APPROVE_OPTION) openFile(fileChooser.getSelectedFile(), parentForDialog);
	}
	
	public void openFile(File file, Component parentForDialog)
	{
		try
		{
			if(!hasProperExtension(file)) JOptionPane.showMessageDialog(parentForDialog, "Nieobsługiwany format pliku.",
																		"Błąd otwierania", JOptionPane.ERROR_MESSAGE);
			loader.parseSimulation(file, simulation);
		}
		catch(IOException | SAXException | ParserConfigurationException e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveSimulation(Component parentForDialog)
	{
		
	}
	
	public void saveSimulationAs(Component parentForDialog)
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new SimulationFileFilter());
		int result = fileChooser.showSaveDialog(parentForDialog);
		if(result == JFileChooser.APPROVE_OPTION) saveFile(fileChooser.getSelectedFile(), parentForDialog);
	}
	
	@SuppressWarnings("ResultOfMethodCallIgnored")
	private void saveFile(File file, Component parentForDialog)
	{
		try
		{
			if(!hasProperExtension(file)) file = fixExtension(file);
			file.createNewFile();
			loader.saveSimulation(simulation, file);
		}
		catch(ParserConfigurationException | TransformerException | IOException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(parentForDialog, "Nie można zapisać pliku.", "Błąd zapisu", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private boolean hasProperExtension(File file)
	{
		return SimulationFileFilter.getExtension(file.getName()).equals(SimulationFileFilter.EVOLUTION_FILE);
	}
	
	private File fixExtension(File file)
	{
		return new File(file.getAbsolutePath() + ".evo");
	}
	
	public static Element getElement(Element parent, String name)
	{
		return (Element) parent.getElementsByTagName(name).item(0);
	}
	
	public static int getIntAttribute(Element element, String name)
	{
		return Integer.parseInt(element.getAttribute(name));
	}
	
	public static float getFloatAttribute(Element element, String name)
	{
		return Float.parseFloat(element.getAttribute(name));
	}
	
	public static void setNumberAttribute(Element element, String attribute, Number value)
	{
		element.setAttribute(attribute, value.toString());
	}
}