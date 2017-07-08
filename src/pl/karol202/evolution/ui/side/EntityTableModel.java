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

import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.entity.property.EntityProperties;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityTableModel extends AbstractTableModel
{
	private Entity entity;
	private String filter;
	private List<EntityProperties> filteredProperties;
	
	EntityTableModel()
	{
		filter = "";
		filter();
	}
	
	@Override
	public int getRowCount()
	{
		return filteredProperties.size();
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
		return filteredProperties.get(row).getName();
	}
	
	private String getPropertyValue(int row)
	{
		if(entity == null) return "";
		return filteredProperties.get(row).getStringValueForEntity(entity);
	}
	
	public void setEntity(Entity entity)
	{
		this.entity = entity;
		fireTableRowsUpdated(0, filteredProperties.size() - 1);
	}
	
	void setFilter(String filter)
	{
		this.filter = filter;
		filter();
		fireTableDataChanged();
	}
	
	private void filter()
	{
		filteredProperties = Stream.of(EntityProperties.values())
								   .filter(p -> p.getName().contains(filter) || filter.isEmpty())
								   .collect(Collectors.toList());
	}
}