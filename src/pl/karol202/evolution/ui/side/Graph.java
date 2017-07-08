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

import pl.karol202.evolution.stats.Stats;
import pl.karol202.evolution.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

abstract class Graph extends JPanel
{
	private static final int REPAINT_TIME = 15;
	static final int MARGIN = 10;
	
	Stats stats;
	private int mouseX;
	
	Graph()
	{
		this.stats = Stats.instance;
		
		setBackground(Color.WHITE);
		ToolTipManager.sharedInstance().setInitialDelay(3);
		
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
				Graph.this.onMouseMoved(-1);
			}
			
			@Override
			public void mouseDragged(MouseEvent e)
			{
				Graph.this.onMouseMoved(e.getX());
			}
			
			@Override
			public void mouseMoved(MouseEvent e)
			{
				Graph.this.onMouseMoved(e.getX());
			}
		});
	}
	
	@SuppressWarnings("InfiniteLoopStatement")
	private void createUpdateThread()
	{
		Runnable runnable = () ->
		{
			while(true)
			{
				SwingUtilities.invokeLater(() -> Graph.this.update(getTime()));
				try
				{
					Thread.sleep(REPAINT_TIME);
				}
				catch(InterruptedException e)
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
		drawGraph(g);
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
	
	private void drawGraph(Graphics2D g)
	{
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawGraphLines(g);
	}
	
	abstract void drawGraphLines(Graphics2D g);
	
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
		float time = getTime();
		if(time < 0 || time > stats.getCurrentTime() || stats.getCurrentTime() < 1) mouseX = -1;
		update(time);
	}
	
	private void update(float time)
	{
		if(mouseX != -1) updateTooltip(time);
		else disableTooltip();
		repaint();
	}
	
	private float getTime()
	{
		return Utils.map(mouseX, MARGIN, getWidth() - MARGIN, 0, stats.getCurrentTime());
	}
	
	abstract void updateTooltip(float time);
	
	private void disableTooltip()
	{
		setToolTipText(null);
	}
}