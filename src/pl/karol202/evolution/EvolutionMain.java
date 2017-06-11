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
	
	private EvolutionFrame frame;
	
	public EvolutionMain()
	{
		random = new Random();
		world = new World(random);
		world.generateWorld(1024, 1024);
		simulation = new Simulation(world, 3);
		
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
		SwingUtilities.invokeLater(() -> frame = new EvolutionFrame(world, simulation));
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