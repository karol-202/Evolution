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
package pl.karol202.evolution.ui.side;

import pl.karol202.evolution.entity.Entities;
import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.world.OnWorldUpdateListener;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SidePanel extends JPanel implements OnWorldUpdateListener, DocumentListener
{
	private World world;
	private Entities entities;
	
	private JLabel labelTitle;
	private JTabbedPane tabbedPane;
	private JTextField fieldSearch;
	
	private JScrollPane scrollPaneProperties;
	private JTable tableProperties;
	private EntityTableModel tableModelProperties;
	
	private JScrollPane scrollPaneGenotype;
	private JTable tableGenotype;
	private GenotypeTableModel tableModelGenotype;
	
	private JPanel panelGraphs;
	private JLabel labelEntityAmountGraph;
	private EntityAmountGraph graphEntityAmount;
	private JPanel panelEmpty;
	
	private JPanel panelStats;
	private JScrollPane scrollPaneStats;
	private JTable tableStats;
	private EntityStatsTableModel tableModelEntityStats;
	private JLabel labelPropertyName;
	private EntityPropertyGraph graphEntityProperty;
	
	private int lastEntityIndex;
	
	public SidePanel(World world)
	{
		this.world = world;
		this.entities = world.getEntities();
		world.addListener(this);
		
		setLayout(new GridBagLayout());
		initTitleLabel();
		initTabbedPane();
		initSearchField();
		
		updateData();
	}
	
	private void initTitleLabel()
	{
		labelTitle = new JLabel();
		labelTitle.setFont(new Font(labelTitle.getFont().getName(), Font.PLAIN, 16));
		add(labelTitle, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	private void initTabbedPane()
	{
		tabbedPane = new JTabbedPane();
		add(tabbedPane, new GridBagConstraints(0, 1, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0));
		initEntityTab();
		initGenotypeTab();
		initGraphsTab();
		initStatsTab();
	}

	private void initEntityTab()
	{
		tableModelProperties = new EntityTableModel();
		
		tableProperties = new JTable(tableModelProperties);
		tableProperties.setTableHeader(null);
		
		scrollPaneProperties = new JScrollPane(tableProperties);
		tabbedPane.addTab("Właściwości", scrollPaneProperties);
	}
	
	private void initGenotypeTab()
	{
		tableModelGenotype = new GenotypeTableModel();
		
		tableGenotype = new JTable(tableModelGenotype);
		tableGenotype.setTableHeader(null);
		
		scrollPaneGenotype = new JScrollPane(tableGenotype);
		tabbedPane.addTab("Genotyp", scrollPaneGenotype);
	}
	
	private void initGraphsTab()
	{
		panelGraphs = new JPanel(new GridBagLayout());
		panelGraphs.setBackground(Color.WHITE);
		tabbedPane.addTab("Wykresy", panelGraphs);
		
		labelEntityAmountGraph = new JLabel("Ilość istot");
		panelGraphs.add(labelEntityAmountGraph, new GridBagConstraints(0, 0, 1, 1,
				1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		
		graphEntityAmount = new EntityAmountGraph(world);
		panelGraphs.add(graphEntityAmount, new GridBagConstraints(0, 1, 1, 1,
				1, 0.2, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		panelEmpty = new JPanel();
		panelGraphs.add(panelEmpty, new GridBagConstraints(0, 2, 1, 1, 1, 0.8,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	private void initStatsTab()
	{
		panelStats = new JPanel(new GridBagLayout());
		panelStats.setBackground(Color.WHITE);
		tabbedPane.addTab("Statystyki", panelStats);
		
		tableModelEntityStats = new EntityStatsTableModel(entities);
		
		tableStats = new JTable(tableModelEntityStats);
		tableStats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableStats.getSelectionModel()
				  .addListSelectionListener(e -> {
				  	graphEntityProperty.setProperty(tableModelEntityStats.getFilteredPropertyAtRow(tableStats.getSelectedRow()));
				  	labelPropertyName.setText(graphEntityProperty.getPropertyName());
				  });
		
		scrollPaneStats = new JScrollPane(tableStats);
		panelStats.add(scrollPaneStats, new GridBagConstraints(0, 0, 1, 1, 1, 0.8,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0));
		
		labelPropertyName = new JLabel();
		panelStats.add(labelPropertyName, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
				0, 0));
		
		graphEntityProperty = new EntityPropertyGraph();
		panelStats.add(graphEntityProperty, new GridBagConstraints(0, 2, 1, 1, 1, 0.2,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	private void initSearchField()
	{
		fieldSearch = new JTextField();
		fieldSearch.getDocument().addDocumentListener(this);
		add(fieldSearch, new GridBagConstraints(0, 2, 1, 1, 1, 0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	public void updateData()
	{
		updateTitleLabel();
		updateTableModels();
	}
	
	private void updateTitleLabel()
	{
		int index = entities.getSelectedEntities().count() == 1 ?
					entities.getEntityId(entities.getSelectedEntities().findAny().orElse(null)) : -1;
		if(index == lastEntityIndex) return;
		lastEntityIndex = index;
		
		String title = index != -1 ? String.format("Istota #%d", index) : " ";
		labelTitle.setText(title);
	}
	
	private void updateTableModels()
	{
		Entity entity = entities.getSelectedEntities().count() == 1 ?
						entities.getSelectedEntities().findAny().orElse(null) : null;
		
		tableModelProperties.setEntity(entity);
		tableModelGenotype.setEntity(entity);
		tableModelEntityStats.updateEntities();
	}
	
	@Override
	public void onWorldUpdated()
	{
		updateData();
	}
	
	@Override
	public void insertUpdate(DocumentEvent e)
	{
		updateFiltering();
	}
	
	@Override
	public void removeUpdate(DocumentEvent e)
	{
		updateFiltering();
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) { }
	
	private void updateFiltering()
	{
		String filter = fieldSearch.getText();
		tableModelProperties.setFilter(filter);
		tableModelGenotype.setFilter(filter);
	}
}