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
package pl.karol202.evolution.ui.entity;

import pl.karol202.evolution.entity.Entities;
import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.world.OnWorldUpdateListener;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class EntityPanel extends JPanel implements OnWorldUpdateListener, DocumentListener
{
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
	
	private int lastEntityIndex;
	
	public EntityPanel(World world)
	{
		this.entities = world.getEntities();
		world.addListener(this);
		
		setLayout(new GridBagLayout());
		initTitleLabel();
		initTabbedPane();
		initSearchField();
		
		setPreferredSize(new Dimension(250, (int) getPreferredSize().getHeight()));
		
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
		initEntityTable();
		initGenotypeTable();
	}

	private void initEntityTable()
	{
		tableModelProperties = new EntityTableModel();
		
		tableProperties = new JTable(tableModelProperties);
		tableProperties.setTableHeader(null);
		
		scrollPaneProperties = new JScrollPane(tableProperties);
		tabbedPane.addTab("Właściwości", scrollPaneProperties);
	}
	
	private void initGenotypeTable()
	{
		tableModelGenotype = new GenotypeTableModel();
		
		tableGenotype = new JTable(tableModelGenotype);
		tableGenotype.setTableHeader(null);
		
		scrollPaneGenotype = new JScrollPane(tableGenotype);
		tabbedPane.addTab("Genotyp", scrollPaneGenotype);
	}
	
	private void initSearchField()
	{
		fieldSearch = new JTextField();
		fieldSearch.getDocument().addDocumentListener(this);
		add(fieldSearch, new GridBagConstraints(0, 2, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	public void updateData()
	{
		updateTitleLabel();
		updateTableModels();
	}
	
	private void updateTitleLabel()
	{
		int index = entities.getSelectedEntityIndex();
		if(index == lastEntityIndex) return;
		lastEntityIndex = index;
		
		String title;
		if(index == -1) title = " ";
		else title = String.format("Istota #%d", index);
		labelTitle.setText(title);
	}
	
	private void updateTableModels()
	{
		Entity entity = entities.getSelectedEntity();
		
		tableModelProperties.setEntity(entity);
		tableModelGenotype.setEntity(entity);
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