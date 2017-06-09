package pl.karol202.evolution.ui.main;

import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.ui.entity.EntityPanel;
import pl.karol202.evolution.ui.time.TimeSettingsFrame;
import pl.karol202.evolution.utils.ButtonHovering;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import java.awt.*;

public class EvolutionFrame extends JFrame implements EvolutionPanel.OnViewParametersChangeListener, Simulation.OnSimulationStateChangeListener
{
	private World world;
	private Simulation simulation;
	
	private EvolutionPanel evolutionPanel;
	
	private JMenuBar menuBar;
	
	private JMenu menuSimulation;
	private JMenuItem itemNew;
	private JMenuItem itemStart;
	private JMenuItem itemPause;
	private JMenuItem itemStep;
	private JMenuItem itemTimeSettings;
	
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
	
	private EntityPanel entityPanel;
	
	public EvolutionFrame(World world, Simulation simulation)
	{
		super("Evolution");
		this.world = world;
		this.simulation = simulation;
		simulation.addStateListener(this);
		
		setFrameParams();
		initEvolutionPanel();
		initMenu();
		initBottomPanel();
		initEntityPanel();
		
		updateMenu();
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
		menuSimulation.add(new JSeparator());
		initStartSimulationItem();
		initPauseSimulationItem();
		initStepSimulationItem();
		initTimeSettingsItem();
	}
	
	private void initNewSimulationItem()
	{
		itemNew = new JMenuItem("Nowa symulacja");
		itemNew.addActionListener(e -> world.generateWorld());
		menuSimulation.add(itemNew);
	}
	
	private void initStartSimulationItem()
	{
		itemStart = new JMenuItem("Start symulacji");
		itemStart.addActionListener(e -> simulation.start());
		menuSimulation.add(itemStart);
	}
	
	private void initPauseSimulationItem()
	{
		itemPause = new JMenuItem("Zatrzymaj symulację");
		itemPause.addActionListener(e -> simulation.stop());
		menuSimulation.add(itemPause);
	}
	
	private void initStepSimulationItem()
	{
		itemStep = new JMenuItem("Krok symulacji");
		itemStep.addActionListener(e -> simulation.step());
		menuSimulation.add(itemStep);
	}
	
	private void initTimeSettingsItem()
	{
		itemTimeSettings = new JMenuItem("Ustawienia czasu");
		itemTimeSettings.addActionListener(e -> SwingUtilities.invokeLater(() -> new TimeSettingsFrame(simulation)));
		menuSimulation.add(itemTimeSettings);
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
	
	private void initEntityPanel()
	{
		entityPanel = new EntityPanel(world);
		entityPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.DARK_GRAY));
		add(entityPanel, BorderLayout.EAST);
	}
	
	private void updateMenu()
	{
		itemStart.setEnabled(!simulation.isRunning());
		itemPause.setEnabled(simulation.isRunning());
		itemStep.setEnabled(!simulation.isRunning());
		itemTimeSettings.setEnabled(!simulation.isRunning());
	}
	
	@Override
	public void onViewParametersChanged()
	{
		labelScale.setText(getScaleString());
	}
	
	@Override
	public void onEntitySelectionChanged()
	{
		entityPanel.updateData();
	}
	
	@Override
	public void onSimulationStateChanged()
	{
		updateMenu();
	}
	
	private String getScaleString()
	{
		return String.format("%.1f%%", evolutionPanel.getScale() * 100);
	}
}