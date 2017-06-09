package pl.karol202.evolution.ui;

import pl.karol202.evolution.entity.Entities;
import pl.karol202.evolution.entity.Entity;

import javax.swing.*;
import java.awt.*;

public class EntityPanel extends JPanel
{
	private Entities entities;
	
	private JLabel labelTitle;
	
	private JTable tableEntity;
	private EntityTableModel tableModel;
	
	public EntityPanel(Entities entities)
	{
		this.entities = entities;
		
		setLayout(new GridBagLayout());
		initTitleLabel();
		initEntityTable();
		
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
	
	private void initEntityTable()
	{
		tableModel = new EntityTableModel();
		
		tableEntity = new JTable(tableModel);
		add(tableEntity, new GridBagConstraints(0, 1, 1, 1, 0, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0));
	}
	
	public void updateData()
	{
		updateTitleLabel();
		updateTableModel();
	}
	
	private void updateTitleLabel()
	{
		int index = entities.getSelectedEntityIndex();
		String title;
		if(index == -1) title = " ";
		else title = String.format("Istota #%d", index);
		labelTitle.setText(title);
	}
	
	private void updateTableModel()
	{
		Entity entity = entities.getSelectedEntity();
		tableModel.setEntity(entity);
	}
}