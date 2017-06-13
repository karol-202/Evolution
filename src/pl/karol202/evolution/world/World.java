package pl.karol202.evolution.world;

import pl.karol202.evolution.entity.Entities;
import pl.karol202.evolution.utils.OctaveSimplexNoise;
import pl.karol202.evolution.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

public class World
{
	public static final int MAX_SIZE = 8192;
	
	public static final int MIN_OFFSET = -10000;
	public static final int MAX_OFFSET = 10000;
	
	private static final double[] TEMPERATURE_OCTAVES = {
			0.55, 0.3, 0.15
	};
	private static final double[] HUMIDITY_OCTAVES = {
			0.5, 0.25, 0.125, 0.0625, 0.03125
	};
	
	private static int staticWidth;
	private static int staticHeight;
	
	private Random random;
	private ArrayList<OnWorldUpdateListener> listeners;
	private int width;
	private int height;
	private int temperatureFrequency;
	private int humidityFrequency;
	private float minTemperature;
	private float maxTemperature;
	private float minHumidity;
	private float maxHumidity;
	
	private float[][] temperature;
	private float[][] humidity;
	private Plants plants;
	private Entities entities;
	
	public World(Random random)
	{
		this.random = random;
		listeners = new ArrayList<>();
		temperatureFrequency = 8192;
		humidityFrequency = 4096;
		minTemperature = -20;
		maxTemperature = 40;
		minHumidity = 0;
		maxHumidity = 100;
		
		plants = new Plants(random, this);
		entities = new Entities(random);
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
		plants.generatePlants();
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
				temperature[x][y] = Utils.map(noise, -1, 1, minTemperature, maxTemperature);
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
				humidity[x][y] = Utils.map(noise, -1, 1, minHumidity, maxHumidity);
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
	
	public float getMinTemperature()
	{
		return minTemperature;
	}
	
	public void setMinTemperature(float minTemperature)
	{
		this.minTemperature = minTemperature;
	}
	
	public float getMaxTemperature()
	{
		return maxTemperature;
	}
	
	public void setMaxTemperature(float maxTemperature)
	{
		this.maxTemperature = maxTemperature;
	}
	
	public float getMinHumidity()
	{
		return minHumidity;
	}
	
	public void setMinHumidity(float minHumidity)
	{
		this.minHumidity = minHumidity;
	}
	
	public float getMaxHumidity()
	{
		return maxHumidity;
	}
	
	public void setMaxHumidity(float maxHumidity)
	{
		this.maxHumidity = maxHumidity;
	}
	
	public float[][] getTemperature()
	{
		return temperature;
	}
	
	public float[][] getHumidity()
	{
		return humidity;
	}
	
	public Plants getPlants()
	{
		return plants;
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