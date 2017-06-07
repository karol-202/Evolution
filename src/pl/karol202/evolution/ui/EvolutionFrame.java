package pl.karol202.evolution.ui;

import pl.karol202.evolution.world.World;

import javax.swing.*;

public class EvolutionFrame extends JFrame
{
	private World world;
	
	private JMenuBar menuBar;
	private JMenu menuSimulation;
	private JMenuItem itemNew;
	
	public EvolutionFrame(World world)
	{
		super("Evolution");
		this.world = world;
		
		setSize(1024, 768);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);

		EvolutionPanel panel = new EvolutionPanel(world);
		add(panel);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuSimulation = new JMenu("Symulacja");
		menuBar.add(menuSimulation);
		
		itemNew = new JMenuItem("Nowa symulacja");
		itemNew.addActionListener(e -> world.generateWorld());
		menuSimulation.add(itemNew);
	}
}