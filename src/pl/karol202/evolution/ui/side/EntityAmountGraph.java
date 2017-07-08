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

import pl.karol202.evolution.utils.Utils;
import pl.karol202.evolution.world.World;

import java.awt.*;

class EntityAmountGraph extends Graph
{
	private World world;
	
	EntityAmountGraph(World world)
	{
		this.world = world;
	}
	
	@Override
	void drawGraphLines(Graphics2D g)
	{
		g.setColor(Color.GRAY);
		g.setStroke(new BasicStroke(1));
		
		int previousAmount = world.getInitialEntitiesAmount();
		for(int x = MARGIN; x < getWidth() - MARGIN; x++)
		{
			float time = Utils.map(x, MARGIN, getWidth() - MARGIN - 1, 0, stats.getCurrentTime());
			int amount = stats.getEntitiesAmount(time);
			
			int previousY = (int) Utils.map(previousAmount, 0, stats.getHighestEntitiesAmount(), getHeight() - MARGIN, MARGIN);
			int currentY = (int) Utils.map(amount, 0, stats.getHighestEntitiesAmount(), getHeight() - MARGIN, MARGIN);
			if(x > MARGIN) g.drawLine(x - 1, previousY, x, currentY);
			
			previousAmount = amount;
		}
	}
	
	@Override
	void updateTooltip(float time)
	{
		int amount = stats.getEntitiesAmount(time);
		setToolTipText(String.format("<html>Czas: %.2f<br>Istoty: %d</html>", time, amount));
	}
}