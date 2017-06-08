package pl.karol202.evolution.ui;

import pl.karol202.evolution.utils.ButtonHovering;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import java.awt.*;

public class EvolutionFrame extends JFrame implements EvolutionPanel.OnViewParametersChangeListener
{
	private World world;
	
	private EvolutionPanel evolutionPanel;
	
	private JMenuBar menuBar;
	
	private JMenu menuSimulation;
	private JMenuItem itemNew;
	
	private JMenu menuView;
	private ButtonGroup groupView;
	private JRadioButtonMenuItem itemViewStandard;
	private JRadioButtonMenuItem itemViewTemperature;
	private JRadioButtonMenuItem itemViewHumidity;
	private JMenuItem itemCenterView;
	
	private JPanel panelBottom;
	private JLabel labelScale;
	private ButtonHovering buttonMinus;
	private ButtonHovering buttonPlus;
	
	public EvolutionFrame(World world)
	{
		super("Evolution");
		this.world = world;
		
		setFrameParams();
		initEvolutionPanel();
		initMenu();
		initBottomPanel();
	}
	
	private void setFrameParams()
	{
		setSize(1024, 768);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setVisible(true);
	}
	
	private void initEvolutionPanel()
	{
		evolutionPanel = new EvolutionPanel(world, this);
		add(evolutionPanel, BorderLayout.CENTER);
	}
	
	private void initMenu()
	{
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		initSimulationMenu();
		initViewMenu();
	}
	
	private void initSimulationMenu()
	{
		menuSimulation = new JMenu("Symulacja");
		menuBar.add(menuSimulation);
		initNewSimulationItem();
	}
	
	private void initNewSimulationItem()
	{
		itemNew = new JMenuItem("Nowa symulacja");
		itemNew.addActionListener(e -> world.generateWorld());
		menuSimulation.add(itemNew);
	}
	
	private void initViewMenu()
	{
		menuView = new JMenu("Widok");
		menuBar.add(menuView);
		
		groupView = new ButtonGroup();
		initStandardModeItem();
		initTemperatureModeItem();
		initHumidityModeItem();
		menuView.add(new JSeparator());
		initCenterViewItem();
	}
	
	private void initStandardModeItem()
	{
		itemViewStandard = new JRadioButtonMenuItem("Standardowy");
		itemViewStandard.addActionListener(e -> evolutionPanel.setViewMode(ViewMode.STANDARD));
		itemViewStandard.setSelected(true);
		groupView.add(itemViewStandard);
		menuView.add(itemViewStandard);
	}
	
	private void initTemperatureModeItem()
	{
		itemViewTemperature = new JRadioButtonMenuItem("Temperatura");
		itemViewTemperature.addActionListener(e -> evolutionPanel.setViewMode(ViewMode.TEMPERATURE));
		groupView.add(itemViewTemperature);
		menuView.add(itemViewTemperature);
	}
	
	private void initHumidityModeItem()
	{
		itemViewHumidity = new JRadioButtonMenuItem("Wilgotność");
		itemViewHumidity.addActionListener(e -> evolutionPanel.setViewMode(ViewMode.HUMIDITY));
		groupView.add(itemViewHumidity);
		menuView.add(itemViewHumidity);
	}
	
	private void initCenterViewItem()
	{
		itemCenterView = new JMenuItem("Wyśrodkuj");
		itemCenterView.addActionListener(e -> evolutionPanel.centerView());
		menuView.add(itemCenterView);
	}
	
	private void initBottomPanel()
	{
		panelBottom = new JPanel(new GridBagLayout());
		panelBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
		add(panelBottom, BorderLayout.SOUTH);
		initScaleLabel();
		initScaleDownButton();
		initScaleUpButton();
	}
	
	private void initScaleLabel()
	{
		labelScale = new JLabel(getScaleString());
		panelBottom.add(labelScale, new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	private void initScaleDownButton()
	{
		buttonMinus = new ButtonHovering("res/minus.png");
		buttonMinus.setListener(evolutionPanel::scaleDown);
		panelBottom.add(buttonMinus, new GridBagConstraints(0, 0, 1, 1, 1, 0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	private void initScaleUpButton()
	{
		buttonPlus = new ButtonHovering("res/plus.png");
		buttonPlus.setListener(evolutionPanel::scaleUp);
		panelBottom.add(buttonPlus, new GridBagConstraints(2, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	@Override
	public void onViewParametersChanged()
	{
		labelScale.setText(getScaleString());
	}
	
	private String getScaleString()
	{
		return String.format("%.1f%%", evolutionPanel.getScale() * 100);
	}
}