package pl.karol202.evolution.ui;

import pl.karol202.evolution.utils.Gradient;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EvolutionPanel extends JPanel
{
	private World world;
	private BufferedImage temperatureImage;
	private Gradient temperatureGradient;
	
	public EvolutionPanel(World world)
	{
		this.world = world;
		temperatureGradient = new Gradient();
		temperatureGradient.addColor(new Color(0.96f, 0.04f, 0f), 45);
		temperatureGradient.addColor(new Color(1f, 0.27f, 0.02f), 31);
		temperatureGradient.addColor(new Color(0.8f, 0.87f, 0.02f), 18);
		temperatureGradient.addColor(new Color(0.42f, 0.91f, 0f), 8);
		temperatureGradient.addColor(new Color(0f, 0.85f, 0.6f), -4);
		temperatureGradient.addColor(new Color(0.1f, 0.28f, 1f), -20);
		createTemperatureImage();
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		drawTemperature(g);
	}
	
	private void drawTemperature(Graphics g)
	{
		g.drawImage(temperatureImage, 0, 0, world.getWidth(), world.getHeight(), null);
	}
	
	private void createTemperatureImage()
	{
		temperatureImage = new BufferedImage(world.getWidth(), world.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < world.getWidth(); x++)
		{
			for(int y = 0; y < world.getHeight(); y++)
			{
				float temperature = world.getTemperature()[x][y];
				int color = temperatureGradient.getColorAtPosition(temperature).getRGB();
				temperatureImage.setRGB(x, y, color);
			}
		}
	}
}