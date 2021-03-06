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

import pl.karol202.evolution.entity.Entities;
import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.simulation.SimulationManager;
import pl.karol202.evolution.ui.settings.SimulationSettingsFrame;
import pl.karol202.evolution.ui.side.SidePanel;
import pl.karol202.evolution.utils.ButtonHovering;
import pl.karol202.evolution.utils.ImageLoader;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import java.awt.*;

public class EvolutionFrame extends JFrame implements EvolutionPanel.OnViewChangeListener, Simulation.OnSimulationUpdateListener
{
	private static final int REPAINT_TIME = 10;
	
	private SimulationManager manager;
	private Simulation simulation;
	private World world;
	private Entities entities;
	
	private JSplitPane splitPane;
	private EvolutionPanel panelEvolution;
	private SidePanel panelSide;
	
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
	private JCheckBoxMenuItem itemLayerSightAndSmell;
	private JCheckBoxMenuItem itemLayerBehaviours;
	
	private JMenu menuSelection;
	private JCheckBoxMenuItem itemSelectingToggle;
	private JCheckBoxMenuItem itemSelectAll;
	
	private JToolBar toolbar;
	private JButton buttonStart;
	private JButton buttonPause;
	private JButton buttonStep;
	private JToggleButton buttonSelectingToggle;
	private JToggleButton buttonSelectAll;
	
	private JPanel panelBottom;
	private JLabel labelEntities;
	private JLabel labelTemperature;
	private JLabel labelHumidity;
	private JLabel labelScale;
	private ButtonHovering buttonMinus;
	private ButtonHovering buttonPlus;
	
	private long lastRepaintTime;
	
	public EvolutionFrame(SimulationManager manager, Simulation simulation)
	{
		super("Evolution");
		
		this.manager = manager;
		this.simulation = simulation;
		this.world = simulation.getWorld();
		this.entities = world.getEntities();
		simulation.addListener(this);
		
		setFrameParams();
		initSplitPane();
		initMenu();
		initToolbar();
		initBottomPanel();
		initSidePanel();
		
		updateMenu();
		updateEntitiesLabel();
		updateWorldInfo(0, 0);
	}
	
	private void setFrameParams()
	{
		setSize(1024, 768);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setIconImage(ImageLoader.loadImage("/res/icon.png"));
		setLayout(new BorderLayout());
		setVisible(true);
	}
	
	private void initSplitPane()
	{
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		splitPane.setDividerLocation(getWidth() - 307);
		initEvolutionPanel();
		initSidePanel();
		add(splitPane, BorderLayout.CENTER);
	}
	
	private void initEvolutionPanel()
	{
		panelEvolution = new EvolutionPanel(world, this);
		panelEvolution.setMinimumSize(new Dimension(100, 0));
		splitPane.setLeftComponent(panelEvolution);
	}
	
	private void initSidePanel()
	{
		panelSide = new SidePanel(world);
		panelSide.setMinimumSize(new Dimension(200, 0));
		splitPane.setRightComponent(panelSide);
	}
	
	private void initMenu()
	{
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		initSimulationMenu();
		initViewMenu();
		initSelectionMenu();
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
		menuView.add(new JSeparator());
		initSightAndSmellLayerItem();
		initBehavioursLayerItem();
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
	
	private void initSightAndSmellLayerItem()
	{
		itemLayerSightAndSmell = new JCheckBoxMenuItem("Warstwa: wzrok i węch");
		itemLayerSightAndSmell.setState(panelEvolution.isSightAndSmellLayerActive());
		itemLayerSightAndSmell.addActionListener(e -> panelEvolution.setSightAndSmellLayerActive(itemLayerSightAndSmell.getState()));
		menuView.add(itemLayerSightAndSmell);
	}
	
	private void initBehavioursLayerItem()
	{
		itemLayerBehaviours = new JCheckBoxMenuItem("Warstwa: zachowania");
		itemLayerBehaviours.setState(panelEvolution.isBehavioursLayerActive());
		itemLayerBehaviours.addActionListener(e -> panelEvolution.setBehavioursLayerActive(itemLayerBehaviours.getState()));
		menuView.add(itemLayerBehaviours);
	}
	
	private void initSelectionMenu()
	{
		menuSelection = new JMenu("Zaznaczenie");
		menuBar.add(menuSelection);
		
		initSelectingToggleItem();
		initSelectAllItem();
	}
	
	private void initSelectingToggleItem()
	{
		itemSelectingToggle = new JCheckBoxMenuItem("Włącz zaznaczanie");
		itemSelectingToggle.addActionListener(e -> setSelecting(itemSelectingToggle.getState()));
		menuSelection.add(itemSelectingToggle);
	}
	
	private void initSelectAllItem()
	{
		itemSelectAll = new JCheckBoxMenuItem("Zaznacz wszystko");
		itemSelectAll.addActionListener(e -> setSelectAll(itemSelectAll.getState()));
		menuSelection.add(itemSelectAll);
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
		toolbar.addSeparator();
		initSelectingToggleButton();
		initSelectAllButton();
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
	
	private void initSelectingToggleButton()
	{
		buttonSelectingToggle = new JToggleButton(new ImageIcon(ImageLoader.loadImage("/res/selection.png")));
		buttonSelectingToggle.setFocusable(false);
		buttonSelectingToggle.addActionListener(e -> setSelecting(buttonSelectingToggle.isSelected()));
		toolbar.add(buttonSelectingToggle);
	}
	
	private void initSelectAllButton()
	{
		buttonSelectAll = new JToggleButton(new ImageIcon(ImageLoader.loadImage("/res/select_all.png")));
		buttonSelectAll.setFocusable(false);
		buttonSelectAll.addActionListener(e -> setSelectAll(buttonSelectAll.isSelected()));
		toolbar.add(buttonSelectAll);
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
	
	private void setSelecting(boolean selecting)
	{
		panelEvolution.setSelecting(selecting);
		onViewParametersChanged();
	}
	
	private void setSelectAll(boolean selectAll)
	{
		if(selectAll) entities.selectAll();
		entities.setSelectingAll(selectAll);
		
		if(selectAll) panelEvolution.setSelecting(false);
		panelEvolution.repaint();
		
		itemSelectingToggle.setEnabled(!selectAll);
		buttonSelectingToggle.setEnabled(!selectAll);
		onEntitySelectionChanged();
		onViewParametersChanged();
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
	
	private void updateWorldInfo(float x, float y)
	{
		float temperature = world.getTemperature(x, y);
		labelTemperature.setText(String.format("Temperatura: %.1f°", temperature));
		
		float humidity = world.getHumidity(x, y);
		labelHumidity.setText(String.format("Wilgotność: %.1f%%", humidity));
	}
	
	@Override
	public void onViewParametersChanged()
	{
		labelScale.setText(getScaleString());
		
		itemSelectingToggle.setState(panelEvolution.isSelecting());
		buttonSelectingToggle.setSelected(panelEvolution.isSelecting());
		
		itemSelectAll.setState(entities.isSelectingAll());
		buttonSelectAll.setSelected(entities.isSelectingAll());
	}
	
	@Override
	public void onEntitySelectionChanged()
	{
		if(!entities.areAllEntitiesSelected())
		{
			entities.setSelectingAll(false);
			onViewParametersChanged();
		}
		panelSide.updateData();
	}
	
	@Override
	public void onMousePositionChanged(float x, float y)
	{
		updateWorldInfo(x, y);
	}
	
	@Override
	public void onSimulationUpdated()
	{
		panelSide.updateData();
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