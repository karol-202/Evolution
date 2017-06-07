package pl.karol202.evolution.ui;

import pl.karol202.evolution.world.World;

import javax.swing.*;

public class EvolutionFrame extends JFrame
{
	private World world;
	
	private JMenuBar menuBar;
	
	private JMenu menuSimulation;
	private JMenuItem itemNew;
	
	private JMenu menuView;
	private ButtonGroup groupView;
	private JRadioButtonMenuItem itemViewTemperature;
	private JRadioButtonMenuItem itemViewHumidity;
	
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
		
		menuView = new JMenu("Widok");
		menuBar.add(menuView);
		
		groupView = new ButtonGroup();
		
		itemViewTemperature = new JRadioButtonMenuItem("Temperatura");
		itemViewTemperature.addActionListener(e -> panel.setViewMode(ViewMode.TEMPERATURE));
		itemViewTemperature.setSelected(true);
		groupView.add(itemViewTemperature);
		menuView.add(itemViewTemperature);
		
		itemViewHumidity = new JRadioButtonMenuItem("Wilgotność");
		itemViewHumidity.addActionListener(e -> panel.setViewMode(ViewMode.HUMIDITY));
		groupView.add(itemViewHumidity);
		menuView.add(itemViewHumidity);
	}
}