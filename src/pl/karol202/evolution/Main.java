package pl.karol202.evolution;

import pl.karol202.evolution.ui.MainFrame;
import pl.karol202.evolution.world.World;

import javax.swing.*;

public class Main
{
	private World world;
	
	public Main()
	{
		world = new World(null, 512, 512);
		runMainFrame();
	}
	
	private void runMainFrame()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(() -> new MainFrame(world));
	}
	
	public static void main(String[] args)
	{
		new Main();
	}
}