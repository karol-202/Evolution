package pl.karol202.evolution.ui;

import pl.karol202.evolution.utils.Gradient;
import pl.karol202.evolution.world.OnWorldUpdateListener;
import pl.karol202.evolution.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

public class EvolutionPanel extends JPanel implements OnWorldUpdateListener, MouseWheelListener
{
	private static final double SQRT2 = Math.sqrt(2);
	private static final double MIN_ZOOM = 0.03125f;
	private static final double MAX_ZOOM = 16;
	
	private World world;
	
	private Gradient temperatureGradient;
	private BufferedImage temperatureImage;
	private Gradient humidityGradient;
	private BufferedImage humidityImage;
	
	private ViewMode viewMode;
	private double scale;
	private int xOffset;
	private int yOffset;
	
	public EvolutionPanel(World world)
	{
		this.world = world;
		this.viewMode = ViewMode.TEMPERATURE;
		this.scale = 1;
		this.xOffset = 0;
		this.yOffset = 0;
		addMouseWheelListener(this);
		world.addListener(this);
		initTemperatureGradient();
		initHumidityGradient();
		createTemperatureImage();
		createHumidityImage();
	}
	
	private void initTemperatureGradient()
	{
		temperatureGradient = new Gradient();
		temperatureGradient.addColor(new Color(0.96f, 0.04f, 0f), 45);
		temperatureGradient.addColor(new Color(1f, 0.27f, 0.02f), 31);
		temperatureGradient.addColor(new Color(0.8f, 0.87f, 0.02f), 18);
		temperatureGradient.addColor(new Color(0.42f, 0.91f, 0f), 8);
		temperatureGradient.addColor(new Color(0f, 0.85f, 0.6f), -4);
		temperatureGradient.addColor(new Color(0.1f, 0.28f, 1f), -20);
	}
	
	private void initHumidityGradient()
	{
		humidityGradient = new Gradient();
		humidityGradient.addColor(Color.WHITE, 0);
		humidityGradient.addColor(new Color(0.1f, 0.28f, 1f), 100);
	}
	
	private void createTemperatureImage()
	{
		temperatureImage = new BufferedImage(world.getWidth(), world.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < temperatureImage.getWidth(); x++)
		{
			for(int y = 0; y < temperatureImage.getHeight(); y++)
			{
				float temperature = world.getTemperature()[x][y];
				int color = temperatureGradient.getColorAtPosition(temperature).getRGB();
				temperatureImage.setRGB(x, y, color);
			}
		}
	}
	
	private void createHumidityImage()
	{
		humidityImage = new BufferedImage(world.getWidth(), world.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < humidityImage.getWidth(); x++)
		{
			for(int y = 0; y < humidityImage.getHeight(); y++)
			{
				float humidity = world.getHumidity()[x][y];
				int color = humidityGradient.getColorAtPosition(humidity).getRGB();
				humidityImage.setRGB(x, y, color);
			}
		}
	}
	
	@Override
	public void onWorldUpdated()
	{
		initTemperatureGradient();
		initHumidityGradient();
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(viewMode == ViewMode.TEMPERATURE) drawTemperature(g);
		else if(viewMode == ViewMode.HUMIDITY) drawHumidity(g);
	}
	
	private void drawTemperature(Graphics g)
	{
		g.drawImage(temperatureImage, xOffset, yOffset, getImageWidth(temperatureImage), getImageHeight(temperatureImage), null);
	}
	
	private void drawHumidity(Graphics g)
	{
		g.drawImage(humidityImage, xOffset, yOffset, getImageWidth(humidityImage), getImageHeight(humidityImage), null);
	}
	
	private int getImageWidth(BufferedImage image)
	{
		return (int) Math.round(image.getWidth() * scale);
	}
	
	private int getImageHeight(BufferedImage image)
	{
		return (int) Math.round(image.getHeight() * scale);
	}

	public void setViewMode(ViewMode viewMode)
	{
		this.viewMode = viewMode;
		repaint();
	}
	
	public double getScale()
	{
		return scale;
	}
	
	public void scaleDown()
	{
		scale = getLowerZoom();
		if(scale < MIN_ZOOM) scale = MIN_ZOOM;
		repaint();
	}
	
	public void scaleUp()
	{
		scale = getGreaterZoom();
		if(scale > MAX_ZOOM) scale = MAX_ZOOM;
		repaint();
	}
	
	private double getLowerZoom()
	{
		int position = 0;
		if(scale > 1)
		{
			double lower = 1;
			while(true)
			{
				double next = calculateZoomRatio(++position);
				if(next >= scale) return lower;
				lower = next;
			}
		}
		else
		{
			while(true)
			{
				double next = calculateZoomRatio(--position);
				if(next < scale) return next;
			}
		}
	}
	
	private double getGreaterZoom()
	{
		int position = 0;
		if(scale < 1)
		{
			double greater = 1;
			while(true)
			{
				double next = calculateZoomRatio(--position);
				if(next <= scale) return greater;
				greater = next;
			}
		}
		else
		{
			while(true)
			{
				double next = calculateZoomRatio(++position);
				if(next > scale) return next;
			}
		}
	}
	
	private double calculateZoomRatio(int position)
	{
		int posAbs = Math.abs(position);
		double fract = Math.pow(SQRT2, posAbs);
		double round = Math.round(fract * 2) / 2f;
		if(position >= 0) return round;
		else return 1 / round;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if(e.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) return;
		int rotation = e.getWheelRotation();
		if(rotation > 0) scaleDown();
		else scaleUp();
	}
}