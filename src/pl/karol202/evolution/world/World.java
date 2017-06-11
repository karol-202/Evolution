package pl.karol202.evolution.world;

import pl.karol202.evolution.entity.Entities;
import pl.karol202.evolution.utils.OctaveSimplexNoise;
import pl.karol202.evolution.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

public class World
{
	public static final int MAX_SIZE = 8192;
	
	private static final int MIN_OFFSET = -10000;
	private static final int MAX_OFFSET = 10000;
	
	private static final float MIN_TEMPERATURE = -20f;
	private static final float MAX_TEMPERATURE = 40f;
	private static final double[] TEMPERATURE_OCTAVES = {
			0.55, 0.3, 0.15
	};
	
	private static final float MIN_HUMIDITY = 0;
	private static final float MAX_HUMIDITY = 100;
	private static final double[] HUMIDITY_OCTAVES = {
			0.5, 0.25, 0.125, 0.0625, 0.03125
	};
	
	private static int staticWidth;
	private static int staticHeight;
	
	private Random random;
	private int width;
	private int height;
	private int temperatureFrequency;
	private int humidityFrequency;
	private float[][] temperature;
	private float[][] humidity;
	private Entities entities;
	private ArrayList<OnWorldUpdateListener> listeners;
	
	public World(Random random)
	{
		this.random = random;
		this.temperatureFrequency = 8192;
		this.humidityFrequency = 4096;
		this.entities = new Entities(random);
		this.listeners = new ArrayList<>();
	}
	
	public void generateWorld(int width, int height)
	{
		World.staticWidth = width;
		World.staticHeight = height;
		this.width = width;
		this.height = height;
		this.temperature = new float[width][height];
		this.humidity = new float[width][height];
		
		generateTemperature();
		generateHumidity();
		entities.generateEntities();
		listeners.forEach(OnWorldUpdateListener::onWorldUpdated);
	}
	
	private void generateTemperature()
	{
		int xOffset = getRandomOffset();
		int yOffset = getRandomOffset();
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				float noise = (float) OctaveSimplexNoise.noise((x / (float) temperatureFrequency) + xOffset, (y / (float) temperatureFrequency) + yOffset, TEMPERATURE_OCTAVES);
				temperature[x][y] = Utils.map(noise, -1, 1, MIN_TEMPERATURE, MAX_TEMPERATURE);
			}
		}
	}
	
	private void generateHumidity()
	{
		int xOffset = getRandomOffset();
		int yOffset = getRandomOffset();
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				float noise = (float) OctaveSimplexNoise.noise((x / (float) humidityFrequency) + xOffset, (y / (float) humidityFrequency) + yOffset, HUMIDITY_OCTAVES);
				humidity[x][y] = Utils.map(noise, -1, 1, MIN_HUMIDITY, MAX_HUMIDITY);
			}
		}
	}
	
	private int getRandomOffset()
	{
		return random.nextInt(MAX_OFFSET - MIN_OFFSET) + MIN_OFFSET;
	}
	
	public void update()
	{
		entities.update();
	}
	
	public void addListener(OnWorldUpdateListener listener)
	{
		listeners.add(listener);
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getTemperatureFrequency()
	{
		return temperatureFrequency;
	}
	
	public void setTemperatureFrequency(int temperatureFrequency)
	{
		this.temperatureFrequency = temperatureFrequency;
	}
	
	public int getHumidityFrequency()
	{
		return humidityFrequency;
	}
	
	public void setHumidityFrequency(int humidityFrequency)
	{
		this.humidityFrequency = humidityFrequency;
	}
	
	public float[][] getTemperature()
	{
		return temperature;
	}
	
	public float[][] getHumidity()
	{
		return humidity;
	}
	
	public Entities getEntities()
	{
		return entities;
	}
	
	public static int getWorldWidth()
	{
		return staticWidth;
	}
	
	public static int getWorldHeight()
	{
		return staticHeight;
	}
}