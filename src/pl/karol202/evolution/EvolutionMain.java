package pl.karol202.evolution;

import pl.karol202.evolution.ui.main.EvolutionFrame;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import java.util.Random;

public class EvolutionMain
{
	private Random random;
	private World world;
	
	public EvolutionMain()
	{
		random = new Random();
		world = new World(random, 1024, 1024);
		setLookAndFeel();
		runMainFrame();
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
		SwingUtilities.invokeLater(() -> new EvolutionFrame(world));
	}
	
	public static void main(String[] args)
	{
		new EvolutionMain();
	}
}