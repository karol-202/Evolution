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

import pl.karol202.evolution.entity.property.EntityProperties;
import pl.karol202.evolution.utils.Utils;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

class EntityPropertyGraph extends Graph
{
	private EntityProperties property;
	
	@Override
	void drawGraphLines(Graphics2D g)
	{
		if(property == null) return;
		g.setStroke(new BasicStroke(1));
		
		int previousAverageY = 0;
		int previousMinY = 0;
		int previousMaxY = 0;
		for(int x = MARGIN; x < getWidth() - MARGIN; x++)
		{
			float time = Utils.map(x, MARGIN, getWidth() - MARGIN - 1, 0, stats.getCurrentTime());
			List<Map<EntityProperties, Float>> mapsList = stats.getEntitiesProperties(time);
			float average = getAverage(mapsList);
			float min = getMin(mapsList);
			float max = getMax(mapsList);
			
			int averageY = (int) Utils.map(average, property.getMin(), property.getMax(), getHeight() - MARGIN, MARGIN);
			int minY = (int) Utils.map(min, property.getMin(), property.getMax(), getHeight() - MARGIN, MARGIN);
			int maxY = (int) Utils.map(max, property.getMin(), property.getMax(), getHeight() - MARGIN, MARGIN);
			if(x > MARGIN)
			{
				g.setColor(Color.GREEN);
				g.drawLine(x - 1, previousAverageY, x, averageY);
				
				g.setColor(Color.BLUE);
				g.drawLine(x - 1, previousMinY, x, minY);
				
				g.setColor(Color.RED);
				g.drawLine(x - 1, previousMaxY, x, maxY);
			}
			
			previousAverageY = averageY;
			previousMinY = minY;
			previousMaxY = maxY;
		}
	}
	
	private DoubleStream getDoubleStream(List<Map<EntityProperties, Float>> mapsList)
	{
		return mapsList.stream().mapToDouble(m -> m.get(property));
	}
	
	private float getAverage(List<Map<EntityProperties, Float>> mapsList)
	{
		DoubleStream stream = getDoubleStream(mapsList);
		return (float) stream.average().orElse(0);
	}
	
	private float getMin(List<Map<EntityProperties, Float>> mapsList)
	{
		DoubleStream stream = getDoubleStream(mapsList);
		return (float) stream.min().orElse(0);
	}
	
	private float getMax(List<Map<EntityProperties, Float>> mapsList)
	{
		DoubleStream stream = getDoubleStream(mapsList);
		return (float) stream.max().orElse(0);
	}
	
	@Override
	void updateTooltip(float time)
	{
		if(property == null) return;
		List<Map<EntityProperties, Float>> mapsList = stats.getEntitiesProperties(time);
		float average = getAverage(mapsList);
		float min = getMin(mapsList);
		float max = getMax(mapsList);
		setToolTipText(String.format("<html>Czas: %.2f<br>Średnia: %f<br>Min: %f<br>Max: %f</html>", time, average, min, max));
	}
	
	public void setProperty(EntityProperties property)
	{
		if(!property.isFloatProperty() || !property.isRegistered()) this.property = null;
		else this.property = property;
		updateVisibility();
	}
	
	private void updateVisibility()
	{
		setVisible(property != null);
	}
}