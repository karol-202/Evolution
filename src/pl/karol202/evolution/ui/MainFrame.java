package pl.karol202.evolution.ui;

import pl.karol202.evolution.world.World;

import javax.swing.*;

public class MainFrame extends JFrame
{
	private World world;
	
	public MainFrame(World world)
	{
		super("Evolution");
		setSize(1024, 1024);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		this.world = world;
		
		EvolutionPanel panel = new EvolutionPanel(world);
		add(panel);
	}
}