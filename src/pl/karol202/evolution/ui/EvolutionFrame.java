package pl.karol202.evolution.ui;

import pl.karol202.evolution.utils.ButtonHovering;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import java.awt.*;

public class EvolutionFrame extends JFrame
{
	private World world;
	
	private EvolutionPanel evolutionPanel;
	
	private JMenuBar menuBar;
	
	private JMenu menuSimulation;
	private JMenuItem itemNew;
	
	private JMenu menuView;
	private ButtonGroup groupView;
	private JRadioButtonMenuItem itemViewTemperature;
	private JRadioButtonMenuItem itemViewHumidity;
	
	private JPanel panelBottom;
	private JLabel labelScale;
	private ButtonHovering buttonMinus;
	private ButtonHovering buttonPlus;
	
	public EvolutionFrame(World world)
	{
		super("Evolution");
		this.world = world;
		
		setSize(1024, 768);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setVisible(true);

		evolutionPanel = new EvolutionPanel(world);
		add(evolutionPanel, BorderLayout.CENTER);
		
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
		itemViewTemperature.addActionListener(e -> evolutionPanel.setViewMode(ViewMode.TEMPERATURE));
		itemViewTemperature.setSelected(true);
		groupView.add(itemViewTemperature);
		menuView.add(itemViewTemperature);
		
		itemViewHumidity = new JRadioButtonMenuItem("Wilgotność");
		itemViewHumidity.addActionListener(e -> evolutionPanel.setViewMode(ViewMode.HUMIDITY));
		groupView.add(itemViewHumidity);
		menuView.add(itemViewHumidity);
		
		panelBottom = new JPanel(new GridBagLayout());
		add(panelBottom, BorderLayout.SOUTH);
		
		labelScale = new JLabel("100%");
		panelBottom.add(labelScale, new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0));
		
		buttonMinus = new ButtonHovering("res/minus.png");
		panelBottom.add(buttonMinus, new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0));
		
		buttonPlus = new ButtonHovering("res/plus.png");
		panelBottom.add(buttonPlus, new GridBagConstraints(2, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0));
	}
}