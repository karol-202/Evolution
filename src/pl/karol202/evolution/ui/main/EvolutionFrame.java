/*
  Copyright 2017 karol-202
 
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
 
        http://www.apache.org/licenses/LICENSE-2.0
 
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package pl.karol202.evolution.ui.main;

import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.simulation.SimulationManager;
import pl.karol202.evolution.ui.entity.EntityPanel;
import pl.karol202.evolution.ui.settings.SimulationSettingsFrame;
import pl.karol202.evolution.utils.ButtonHovering;
import pl.karol202.evolution.utils.ImageLoader;
import pl.karol202.evolution.utils.Vector2;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import java.awt.*;

public class EvolutionFrame extends JFrame implements EvolutionPanel.OnViewChangeListener, Simulation.OnSimulationUpdateListener
{
	private static final int REPAINT_TIME = 10;
	
	private SimulationManager manager;
	private Simulation simulation;
	private World world;
	
	private EvolutionPanel panelEvolution;
	
	private JMenuBar menuBar;
	
	private JMenu menuSimulation;
	private JMenuItem itemNew;
	private JMenuItem itemOpen;
	private JMenuItem itemSave;
	private JMenuItem itemSaveAs;
	private JMenuItem itemStart;
	private JMenuItem itemPause;
	private JMenuItem itemStep;
	private JMenuItem itemSettings;
	
	private JMenu menuView;
	private ButtonGroup groupView;
	private JRadioButtonMenuItem itemViewStandard;
	private JRadioButtonMenuItem itemViewTemperature;
	private JRadioButtonMenuItem itemViewHumidity;
	private JMenuItem itemCenterView;
	
	private JToolBar toolbar;
	private JButton buttonStart;
	private JButton buttonPause;
	private JButton buttonStep;
	
	private JPanel panelBottom;
	private JLabel labelEntities;
	private JLabel labelTemperature;
	private JLabel labelHumidity;
	private JLabel labelScale;
	private ButtonHovering buttonMinus;
	private ButtonHovering buttonPlus;
	
	private EntityPanel panelEntity;
	
	private long lastRepaintTime;
	
	public EvolutionFrame(SimulationManager manager, Simulation simulation)
	{
		super("Evolution");
		
		this.manager = manager;
		this.simulation = simulation;
		this.world = simulation.getWorld();
		simulation.addListener(this);
		
		setFrameParams();
		initEvolutionPanel();
		initMenu();
		initToolbar();
		initBottomPanel();
		initEntityPanel();
		
		updateMenu();
		updateEntitiesLabel();
		updateWorldInfo(0, 0);
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
		panelEvolution = new EvolutionPanel(world, this);
		add(panelEvolution, BorderLayout.CENTER);
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
		initOpenSimulationItem();
		initSaveSimulationItem();
		initSaveAsSimulationItem();
		menuSimulation.add(new JSeparator());
		initStartSimulationItem();
		initPauseSimulationItem();
		initStepSimulationItem();
		initSettingsItem();
	}
	
	private void initNewSimulationItem()
	{
		itemNew = new JMenuItem("Nowa symulacja");
		itemNew.addActionListener(e -> manager.newSimulation());
		menuSimulation.add(itemNew);
	}
	
	private void initOpenSimulationItem()
	{
		itemOpen = new JMenuItem("Otwórz symulację");
		itemOpen.addActionListener(e -> manager.openSimulation(this));
		menuSimulation.add(itemOpen);
	}
	
	private void initSaveSimulationItem()
	{
		itemSave = new JMenuItem("Zapisz symulację");
		itemSave.addActionListener(e -> manager.saveSimulation(this));
		menuSimulation.add(itemSave);
	}
	
	private void initSaveAsSimulationItem()
	{
		itemSaveAs = new JMenuItem("Zapisz symulację jako");
		itemSaveAs.addActionListener(e -> manager.saveSimulationAs(this));
		menuSimulation.add(itemSaveAs);
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
	
	private void initSettingsItem()
	{
		itemSettings = new JMenuItem("Ustawienia symulacji");
		itemSettings.addActionListener(e -> SwingUtilities.invokeLater(() -> new SimulationSettingsFrame(world, simulation)));
		menuSimulation.add(itemSettings);
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
		itemViewStandard.addActionListener(e -> panelEvolution.setViewMode(ViewMode.STANDARD));
		itemViewStandard.setSelected(true);
		groupView.add(itemViewStandard);
		menuView.add(itemViewStandard);
	}
	
	private void initTemperatureModeItem()
	{
		itemViewTemperature = new JRadioButtonMenuItem("Temperatura");
		itemViewTemperature.addActionListener(e -> panelEvolution.setViewMode(ViewMode.TEMPERATURE));
		groupView.add(itemViewTemperature);
		menuView.add(itemViewTemperature);
	}
	
	private void initHumidityModeItem()
	{
		itemViewHumidity = new JRadioButtonMenuItem("Wilgotność");
		itemViewHumidity.addActionListener(e -> panelEvolution.setViewMode(ViewMode.HUMIDITY));
		groupView.add(itemViewHumidity);
		menuView.add(itemViewHumidity);
	}
	
	private void initCenterViewItem()
	{
		itemCenterView = new JMenuItem("Wyśrodkuj");
		itemCenterView.addActionListener(e -> panelEvolution.centerView());
		menuView.add(itemCenterView);
	}
	
	private void initToolbar()
	{
		toolbar = new JToolBar("Pasek narzędzi");
		toolbar.setFloatable(false);
		toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
		add(toolbar, BorderLayout.PAGE_START);
		initStartButton();
		initPauseButton();
		initStepButton();
	}
	
	private void initStartButton()
	{
		buttonStart = new JButton(new ImageIcon(ImageLoader.loadImage("/res/start.png")));
		buttonStart.setFocusable(false);
		buttonStart.addActionListener(e -> simulation.start());
		toolbar.add(buttonStart);
	}
	
	private void initPauseButton()
	{
		buttonPause = new JButton(new ImageIcon(ImageLoader.loadImage("/res/stop.png")));
		buttonPause.setFocusable(false);
		buttonPause.addActionListener(e -> simulation.stop());
		toolbar.add(buttonPause);
	}
	
	private void initStepButton()
	{
		buttonStep = new JButton(new ImageIcon(ImageLoader.loadImage("/res/step.png")));
		buttonStep.setFocusable(false);
		buttonStep.addActionListener(e -> simulation.step());
		toolbar.add(buttonStep);
	}
	
	private void initBottomPanel()
	{
		panelBottom = new JPanel(new GridBagLayout());
		panelBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
		add(panelBottom, BorderLayout.SOUTH);
		initEntitiesLabel();
		initTemperatureLabel();
		initHumidityLabel();
		initScaleDownButton();
		initScaleLabel();
		initScaleUpButton();
	}
	
	private void initEntitiesLabel()
	{
		labelEntities = new JLabel();
		panelBottom.add(labelEntities, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0, 8, 0, 7),
				0, 0));
	}
	
	private void initTemperatureLabel()
	{
		labelTemperature = new JLabel();
		panelBottom.add(labelTemperature, new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0, 8, 0, 7),
				0, 0));
	}
	
	private void initHumidityLabel()
	{
		labelHumidity = new JLabel();
		panelBottom.add(labelHumidity, new GridBagConstraints(2, 0, 1, 1, 1, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets(0, 8, 0, 0),
				0, 0));
	}
	
	private void initScaleDownButton()
	{
		buttonMinus = new ButtonHovering(ImageLoader.loadImage("/res/minus.png"));
		buttonMinus.setListener(panelEvolution::scaleDown);
		panelBottom.add(buttonMinus, new GridBagConstraints(3, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	private void initScaleLabel()
	{
		labelScale = new JLabel(getScaleString());
		panelBottom.add(labelScale, new GridBagConstraints(4, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	private void initScaleUpButton()
	{
		buttonPlus = new ButtonHovering(ImageLoader.loadImage("/res/plus.png"));
		buttonPlus.setListener(panelEvolution::scaleUp);
		panelBottom.add(buttonPlus, new GridBagConstraints(5, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	private void initEntityPanel()
	{
		panelEntity = new EntityPanel(world);
		panelEntity.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.DARK_GRAY));
		add(panelEntity, BorderLayout.EAST);
	}
	
	private void updateMenu()
	{
		itemStart.setEnabled(!simulation.isRunning());
		itemPause.setEnabled(simulation.isRunning());
		itemStep.setEnabled(!simulation.isRunning());
		itemSettings.setEnabled(!simulation.isRunning());
		
		buttonStart.setEnabled(!simulation.isRunning());
		buttonPause.setEnabled(simulation.isRunning());
		buttonStep.setEnabled(!simulation.isRunning());
	}
	
	private void updateEntitiesLabel()
	{
		labelEntities.setText(getEntitiesString());
	}
	
	private void updateWorldInfo(int x, int y)
	{
		Vector2 worldPos = mouseToWorld(x, y);
		
		float temperature = world.getTemperature(worldPos.getX(), worldPos.getY());
		labelTemperature.setText(String.format("Temperatura: %.1f°", temperature));
		
		float humidity = world.getHumidity(worldPos.getX(), worldPos.getY());
		labelHumidity.setText(String.format("Wilgotność: %.1f%%", humidity));
	}
	
	private Vector2 mouseToWorld(int x, int y)
	{
		float worldX = (float) ((x - panelEvolution.getXPosition()) / panelEvolution.getScale());
		float worldY = (float) ((y - panelEvolution.getYPosition()) / panelEvolution.getScale());
		return new Vector2(worldX, worldY);
	}
	
	@Override
	public void onViewParametersChanged()
	{
		labelScale.setText(getScaleString());
	}
	
	@Override
	public void onEntitySelectionChanged()
	{
		panelEntity.updateData();
	}
	
	@Override
	public void onMousePositionChanged(int x, int y)
	{
		updateWorldInfo(x, y);
	}
	
	@Override
	public void onSimulationUpdated()
	{
		panelEntity.updateData();
		updateEntitiesLabel();
		repaintPanel();
	}
	
	private void repaintPanel()
	{
		if(lastRepaintTime + REPAINT_TIME > System.currentTimeMillis()) return;
		panelEvolution.repaint();
		lastRepaintTime = System.currentTimeMillis();
	}
	
	@Override
	public void onSimulationStateChanged()
	{
		updateMenu();
	}
	
	private String getEntitiesString()
	{
		return String.format("Istoty: %d/%d", world.getEntities().getEntitiesAmount(), world.getInitialEntitiesAmount());
	}
	
	private String getScaleString()
	{
		return String.format("%.1f%%", panelEvolution.getScale() * 100);
	}
}