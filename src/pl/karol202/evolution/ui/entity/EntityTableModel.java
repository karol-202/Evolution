package pl.karol202.evolution.ui.entity;

import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.entity.EntityProperties;

import javax.swing.table.AbstractTableModel;

public class EntityTableModel extends AbstractTableModel
{
	private Entity entity;
	
	@Override
	public int getRowCount()
	{
		return EntityProperties.values().length;
	}
	
	@Override
	public int getColumnCount()
	{
		return 2;
	}
	
	@Override
	public Object getValueAt(int row, int column)
	{
		if(column == 0) return getPropertyName(row);
		else return getPropertyValue(row);
	}
	
	private String getPropertyName(int row)
	{
		return EntityProperties.values()[row].getName();
	}
	
	private String getPropertyValue(int row)
	{
		if(entity == null) return "";
		return EntityProperties.values()[row].getValueForEntity(entity);
	}
	
	public void setEntity(Entity entity)
	{
		this.entity = entity;
		fireTableDataChanged();
	}
}