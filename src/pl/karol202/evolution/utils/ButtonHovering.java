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
package pl.karol202.evolution.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ButtonHovering extends JPanel implements MouseListener
{
	public interface OnButtonClickListener
	{
		void onButtonClicked();
	}
	
	private OnButtonClickListener listener;
	private Image image;
	private Color defaultColor;
	private Color hoverColor;
	private Color pressedColor;
	private boolean pressed;
	private boolean hover;
	
	public ButtonHovering(Image image)
	{
		super();
		this.image = image;
		setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
		addMouseListener(this);
		
		defaultColor = UIManager.getColor("Control");
		hoverColor = new Color(216, 216, 216);
		pressedColor = new Color(180, 180, 180);
		pressed = false;
		hover = false;
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(listener != null) listener.onButtonClicked();
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		setBackground(pressedColor);
		pressed = true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		setBackground(hover ? hoverColor : defaultColor);
		pressed = false;
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
		setBackground(pressed ? pressedColor : hoverColor);
		hover = true;
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		setBackground(defaultColor);
		hover = false;
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
	}
	
	public void setListener(OnButtonClickListener listener)
	{
		this.listener = listener;
	}
}