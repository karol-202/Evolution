package pl.karol202.evolution.ui.entity;

import pl.karol202.evolution.entity.Entities;
import pl.karol202.evolution.entity.Entity;

import javax.swing.*;
import java.awt.*;

public class EntityPanel extends JPanel
{
	private Entities entities;
	
	private JLabel labelTitle;
	
	private JTabbedPane tabbedPane;
	
	private JScrollPane scrollPaneProperties;
	private JTable tableProperties;
	private EntityTableModel tableModelProperties;
	
	private JScrollPane scrollPaneGenotype;
	private JTable tableGenotype;
	private GenotypeTableModel tableModelGenotype;
	
	public EntityPanel(Entities entities)
	{
		this.entities = entities;
		
		setLayout(new GridBagLayout());
		initTitleLabel();
		initTabbedPane();
		initEntityTable();
		initGenotypeTable();
		setPreferredSize(new Dimension(200, (int) getPreferredSize().getHeight()));
		
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
	
	public void updateData()
	{
		updateTitleLabel();
		updateTableModels();
	}
	
	private void updateTitleLabel()
	{
		int index = entities.getSelectedEntityIndex();
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
}