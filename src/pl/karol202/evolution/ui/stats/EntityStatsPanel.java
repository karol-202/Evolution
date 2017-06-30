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

import pl.karol202.evolution.stats.Stats;
import pl.karol202.evolution.utils.Utils;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EntityStatsPanel extends JPanel
{
	private static final int REPAINT_TIME = 10;
	private static final int MARGIN = 10;
	
	private World world;
	
	private int mouseX;
	
	EntityStatsPanel(World world)
	{
		this.world = world;
		
		setBackground(Color.WHITE);
		
		setMouseListener();
		createUpdateThread();
	}
	
	private void setMouseListener()
	{
		addMouseMotionListener(new MouseAdapter()
		{
			@Override
			public void mouseExited(MouseEvent e)
			{
				EntityStatsPanel.this.onMouseMoved(-1);
			}
			
			@Override
			public void mouseDragged(MouseEvent e)
			{
				EntityStatsPanel.this.onMouseMoved(e.getX());
			}
			
			@Override
			public void mouseMoved(MouseEvent e)
			{
				EntityStatsPanel.this.onMouseMoved(e.getX());
			}
		});
	}
	
	private void createUpdateThread()
	{
		Runnable runnable = () -> {
			while(EntityStatsPanel.this.isVisible())
			{
				SwingUtilities.invokeLater(EntityStatsPanel.this::repaint);
				try
				{
					Thread.sleep(REPAINT_TIME);
				} catch(InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		};
		new Thread(runnable).start();
	}
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		drawBorder(g);
		drawChart(g);
		drawHighlight(g);
	}
	
	private void drawBorder(Graphics2D g)
	{
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(1));
		
		g.drawLine(MARGIN, MARGIN, MARGIN, getHeight() - MARGIN);
		g.drawLine(getWidth() - MARGIN, MARGIN, getWidth() - MARGIN, getHeight() - MARGIN);
		g.drawLine(MARGIN, getHeight() - MARGIN, getWidth() - MARGIN, getHeight() - MARGIN);
	}
	
	private void drawChart(Graphics2D g)
	{
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.GRAY);
		g.setStroke(new BasicStroke(1));
		
		Stats stats = Stats.instance;
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
	
	private void drawHighlight(Graphics2D g)
	{
		if(mouseX == -1) return;
		
		g.setColor(Color.LIGHT_GRAY);
		g.setStroke(new BasicStroke(1));
		
		g.drawLine(mouseX, MARGIN, mouseX, getHeight() - MARGIN);
	}
	
	private void onMouseMoved(int x)
	{
		mouseX = x;
		
		float time = Utils.map(x, MARGIN, getWidth() - MARGIN, 0, Stats.instance.getCurrentTime());
		if(time < 0 || time > Stats.instance.getCurrentTime() || Stats.instance.getCurrentTime() < 1) mouseX = -1;
		int amount = Stats.instance.getEntitiesAmount(time);
		
		updateToolTip(time, amount);
		repaint();
	}
	
	private void updateToolTip(float time, int amount)
	{
		setToolTipText("<html>Czas: " + time + "<br>Istoty: " + amount + "</html>");
	}
}