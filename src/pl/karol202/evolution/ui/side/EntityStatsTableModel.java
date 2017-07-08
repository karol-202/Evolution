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
import pl.karol202.evolution.entity.property.EntityProperties;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class EntityStatsTableModel extends AbstractTableModel
{
	private static final String[] COLUMNS = { "Właściwość", "Średnia", "Mediana", "Min", "Max" };
	
	private Entities entities;
	private String filter;
	private List<EntityProperties> filteredProperties;
	
	EntityStatsTableModel(Entities entities)
	{
		this.entities = entities;
		this.filter = "";
		filter();
		updateEntities();
	}
	
	@Override
	public int getRowCount()
	{
		return filteredProperties.size();
	}
	
	@Override
	public int getColumnCount()
	{
		return 5;
	}
	
	@Override
	public String getColumnName(int column)
	{
		return COLUMNS[column];
	}
	
	@Override
	public Object getValueAt(int row, int column)
	{
		EntityProperties property = filteredProperties.get(row);
		DoubleStream stream = getDoubleStream(property);
		if(stream == null && column != 0) return "";
		
		switch(column)
		{
		case 0: return getPropertyName(row);
		case 1: return getAverage(property, stream);
		case 2: return getMedian(property, stream);
		case 3: return getMin(property, stream);
		case 4: return getMax(property, stream);
		default: return "";
		}
	}
	
	private String getPropertyName(int row)
	{
		return filteredProperties.get(row).getName();
	}
	
	private String getAverage(EntityProperties property, DoubleStream stream)
	{
		return property.transformFloatToString((float) stream.average().orElse(-1));
	}
	
	private String getMedian(EntityProperties property, DoubleStream stream)
	{
		double[] array = stream.sorted().toArray();
		int center = array.length / 2;
		if(array.length % 2 == 1) return property.transformFloatToString((float) array[center]);
		else return property.transformFloatToString((float) (array[center - 1] + array[center]) / 2);
	}
	
	private String getMin(EntityProperties property, DoubleStream stream)
	{
		return property.transformFloatToString((float) stream.min().orElse(-1));
	}
	
	private String getMax(EntityProperties property, DoubleStream stream)
	{
		return property.transformFloatToString((float) stream.max().orElse(-1));
	}
	
	private DoubleStream getDoubleStream(EntityProperties property)
	{
		if(entities.getSelectedEntities().count() == 0) return null;
		if(!property.isFloatProperty()) return null;
		return entities.getSelectedEntities().mapToDouble(property::getFloatValueForEntity);
	}
	
	void updateEntities()
	{
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
				.filter(EntityProperties::isStatsCapable)
				.collect(Collectors.toList());
	}
	
	EntityProperties getFilteredPropertyAtRow(int row)
	{
		return filteredProperties.get(row);
	}
}