package pl.karol202.evolution;

import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.ui.main.EvolutionFrame;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import java.util.Random;

public class EvolutionMain
{
	private Random random;
	private World world;
	private Simulation simulation;
	
	public EvolutionMain()
	{
		random = new Random();
		world = new World(random, 1024, 1024);
		simulation = new Simulation(1000);
		setLookAndFeel();
		runMainFrame();
		while(true)
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
		SwingUtilities.invokeLater(() -> new EvolutionFrame(world, simulation));
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
		new EvolutionMain();
	}
}