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
import pl.karol202.evolution.entity.EntityProperties;

import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat;
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
	private DecimalFormat decimalFormat;
	
	EntityStatsTableModel(Entities entities)
	{
		this.entities = entities;
		this.filter = "";
		this.decimalFormat = new DecimalFormat("#0.0#");
		updateEntities();
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
		switch(column)
		{
		case 0: return getPropertyName(row);
		case 1: return getAverage(row);
		case 2: return getMedian(row);
		case 3: return getMin(row);
		case 4: return getMax(row);
		default: return "";
		}
	}
	
	private String getPropertyName(int row)
	{
		return filteredProperties.get(row).getName();
	}
	
	private String getAverage(int row)
	{
		DoubleStream stream = getDoubleStream(row);
		if(stream == null) return "";
		return decimalFormat.format(stream.average().orElse(-1));
	}
	
	private String getMedian(int row)
	{
		DoubleStream stream = getDoubleStream(row);
		if(stream == null) return "";
		
		double[] array = stream.sorted().toArray();
		int center = array.length / 2;
		if(array.length % 2 == 1) return decimalFormat.format(array[center]);
		else return decimalFormat.format((array[center - 1] + array[center]) / 2);
	}
	
	private String getMin(int row)
	{
		DoubleStream stream = getDoubleStream(row);
		if(stream == null) return "";
		return decimalFormat.format(stream.min().orElse(-1));
	}
	
	private String getMax(int row)
	{
		DoubleStream stream = getDoubleStream(row);
		if(stream == null) return "";
		return decimalFormat.format(stream.max().orElse(-1));
	}
	
	private DoubleStream getDoubleStream(int row)
	{
		if(entities.getSelectedEntities().count() == 0) return null;
		EntityProperties property = filteredProperties.get(row);
		if(!property.isFloatProperty()) return null;
		return entities.getSelectedEntities().mapToDouble(property::getFloatValueForEntity);
	}
	
	void updateEntities()
	{
		fireTableDataChanged();
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
				.filter(EntityProperties::isFloatProperty)
				.collect(Collectors.toList());
	}
}