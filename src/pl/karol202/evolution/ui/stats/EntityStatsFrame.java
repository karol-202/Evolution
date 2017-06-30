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
package pl.karol202.evolution.ui.stats;

import pl.karol202.evolution.world.World;

import javax.swing.*;
import java.awt.*;

public class EntityStatsFrame extends JFrame
{
	private World world;
	
	private EntityStatsPanel entityStatsPanel;
	
	public EntityStatsFrame(World world)
	{
		super("Statystyki istot");
		this.world = world;
		
		setFrameParams();
		initEntityStatsPanel();
	}
	
	private void setFrameParams()
	{
		setSize(550, 400);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setLayout(new GridBagLayout());
		setVisible(true);
	}
	
	private void initEntityStatsPanel()
	{
		entityStatsPanel = new EntityStatsPanel(world);
		add(entityStatsPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
				0, 0));
	}
}