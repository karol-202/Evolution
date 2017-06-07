package pl.karol202.evolution.ui;

import pl.karol202.evolution.world.World;

import javax.swing.*;

public class EvolutionFrame extends JFrame
{
	private World world;
	
	public EvolutionFrame(World world)
	{
		super("Evolution");
		this.world = world;
		
		setSize(1024, 768);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);

		EvolutionPanel panel = new EvolutionPanel(world);
		add(panel);
	}
}