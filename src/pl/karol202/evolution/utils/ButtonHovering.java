package pl.karol202.evolution.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ButtonHovering extends JPanel implements MouseListener
{
	public interface OnButtonClickListener
	{
		void onButtonClicked();
	}
	
	private OnButtonClickListener listener;
	private BufferedImage image;
	private Color defaultColor;
	private Color hoverColor;
	private Color pressedColor;
	private boolean pressed;
	private boolean hover;
	
	public ButtonHovering(String path)
	{
		super();
		setImage(path);
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
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
	
	private void setImage(String path)
	{
		try
		{
			image = ImageIO.read(new File(path));
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	}
	
	public void setListener(OnButtonClickListener listener)
	{
		this.listener = listener;
	}
}