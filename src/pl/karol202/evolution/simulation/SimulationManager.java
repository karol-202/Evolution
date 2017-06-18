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

import pl.karol202.evolution.world.World;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SimulationManager
{
	public interface OnSimulationReplaceListener
	{
		void onSimulationReplaced(Simulation simulation);
	}
	
	private Simulation simulation;
	private World world;
	private OnSimulationReplaceListener listener;
	private SimulationLoader loader;
	private SimulationSaver saver;
	
	public SimulationManager(OnSimulationReplaceListener listener)
	{
		this.listener = listener;
		this.loader = new SimulationLoader();
		this.saver = new SimulationSaver();
	}
	
	public void newSimulation()
	{
		world.generateWorld(world.getWidth(), world.getHeight());
		simulation.reset();
	}
	
	public void openSimulation(Component parentForDialog)
	{
		
	}
	
	public void saveSimulation(Component parentForDialog)
	{
		
	}
	
	public void saveSimulationAs(Component parentForDialog)
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new SimulationFileFilter());
		int result = fileChooser.showSaveDialog(parentForDialog);
		if(result == JFileChooser.APPROVE_OPTION) saveAs(fileChooser.getSelectedFile());
	}
	
	private void saveAs(File file)
	{
		try
		{
			if(!hasProperExtension(file)) file = fixExtension(file);
			file.createNewFile();
			saver.saveSimulation(simulation, file);
		}
		catch(ParserConfigurationException | TransformerException | IOException e)
		{
			e.printStackTrace();
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
	
	public void setSimulation(Simulation simulation)
	{
		this.simulation = simulation;
		this.world = simulation.getWorld();
	}
}