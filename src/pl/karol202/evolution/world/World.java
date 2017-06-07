package pl.karol202.evolution.world;

import pl.karol202.evolution.utils.OctaveSimplexNoise;
import pl.karol202.evolution.utils.Utils;

import java.util.Random;

public class World
{
	private static final float FREQUENCY = 256;
	private static final int MIN_OFFSET = -50000;
	private static final int MAX_OFFSET = 50000;
	private static final double[] OCTAVES = {
			0.75, 0.2, 0.05
	};
	private static final float MIN_TEMPERATURE = -20f;
	private static final float MAX_TEMPERATURE = 40f;
	
	private Random random;
	private int width;
	private int height;
	private float[][] temperature;
	private float[][] humidity;
	
	public World(Random random, int width, int height)
	{
		this.random = random;
		this.width = width;
		this.height = height;
		this.temperature = new float[width][height];
		this.humidity = new float[width][height];
		generateWorld();
	}
	
	private void generateWorld()
	{
		int xOffset = getRandomOffset();
		int yOffset = getRandomOffset();
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				float noise = (float) OctaveSimplexNoise.noise((x / FREQUENCY) + xOffset, (y / FREQUENCY) + yOffset, OCTAVES);
				temperature[x][y] = Utils.map(noise, -1, 1, MIN_TEMPERATURE, MAX_TEMPERATURE);
			}
		}
	}
	
	private int getRandomOffset()
	{
		return random.nextInt(MAX_OFFSET - MIN_OFFSET) + MIN_OFFSET;
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