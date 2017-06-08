package pl.karol202.evolution.world;

import pl.karol202.evolution.utils.OctaveSimplexNoise;
import pl.karol202.evolution.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

public class World
{
	private static final int MIN_OFFSET = -50000;
	private static final int MAX_OFFSET = 50000;
	
	private static final float TEMPERATURE_FREQUENCY = 8192;
	private static final float MIN_TEMPERATURE = -20f;
	private static final float MAX_TEMPERATURE = 40f;
	private static final double[] TEMPERATURE_OCTAVES = {
			0.75, 0.2, 0.05
	};
	
	private static final float HUMIDITY_FREQUENCY = 8192;
	private static final float MIN_HUMIDITY = 0;
	private static final float MAX_HUMIDITY = 100;
	private static final double[] HUMIDITY_OCTAVES = {
			0.5, 0.25, 0.125, 0.0625, 0.03125
	};

	private Random random;
	private int width;
	private int height;
	private float[][] temperature;
	private float[][] humidity;
	private ArrayList<OnWorldUpdateListener> listeners;
	
	public World(Random random, int width, int height)
	{
		this.random = random;
		this.width = width;
		this.height = height;
		this.temperature = new float[width][height];
		this.humidity = new float[width][height];
		this.listeners = new ArrayList<>();
		generateWorld();
	}
	
	public void generateWorld()
	{
		generateTemperature();
		generateHumidity();
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
				float noise = (float) OctaveSimplexNoise.noise((x / TEMPERATURE_FREQUENCY) + xOffset, (y / TEMPERATURE_FREQUENCY) + yOffset, TEMPERATURE_OCTAVES);
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
				float noise = (float) OctaveSimplexNoise.noise((x / HUMIDITY_FREQUENCY) + xOffset, (y / HUMIDITY_FREQUENCY) + yOffset, HUMIDITY_OCTAVES);
				humidity[x][y] = Utils.map(noise, -1, 1, MIN_HUMIDITY, MAX_HUMIDITY);
			}
		}
	}
	
	private int getRandomOffset()
	{
		return random.nextInt(MAX_OFFSET - MIN_OFFSET) + MIN_OFFSET;
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
	
	public float[][] getTemperature()
	{
		return temperature;
	}
	
	public float[][] getHumidity()
	{
		return humidity;
	}
}