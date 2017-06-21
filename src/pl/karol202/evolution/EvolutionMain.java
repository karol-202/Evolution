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
package pl.karol202.evolution;

import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.simulation.SimulationManager;
import pl.karol202.evolution.ui.main.EvolutionFrame;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import java.io.File;
import java.util.Random;

public class EvolutionMain
{
	private World world;
	private Simulation simulation;
	private SimulationManager manager;
	private EvolutionFrame frame;
	
	private File fileToOpen;
	
	EvolutionMain(File fileToOpen)
	{
		world = new World(new Random());
		world.generateRandomWorld(1024, 1024);
		simulation = new Simulation(world, 3);
		manager = new SimulationManager(simulation);
		
		this.fileToOpen = fileToOpen;
		
		setLookAndFeel();
		runMainFrame();
		while(frame == null || frame.isVisible())
		{
			SwingUtilities.invokeLater(() -> simulation.mainLoop());
			waitAMillisecond();
		}
	}
	
	private void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void runMainFrame()
	{
		SwingUtilities.invokeLater(() -> {
			frame = new EvolutionFrame(manager, simulation);
			if(fileToOpen != null) manager.openFile(fileToOpen, frame);
		});
	}
	
	private void waitAMillisecond()
	{
		try
		{
			Thread.sleep(1);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		if(args.length == 0) new EvolutionMain(null);
		else new EvolutionMain(new File(args[0]));
	}
}