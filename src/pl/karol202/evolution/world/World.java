package pl.karol202.evolution.world;

import pl.karol202.evolution.utils.SimplexNoise;
import pl.karol202.evolution.utils.Utils;

import java.util.Random;

public class World
{
	public static final float SCALE = 256;
	public static final float MIN_TEMPERATURE = -20f;
	public static final float MAX_TEMPERATURE = 40f;
	
	private int width;
	private int height;
	private float[][] temperature;
	private float[][] humidity;
	
	public World(Random random, int width, int height)
	{
		this.width = width;
		this.height = height;
		this.temperature = new float[width][height];
		this.humidity = new float[width][height];
		generateWorld(random);
	}
	
	private void generateWorld(Random random)
	{
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				float perlin = (float) SimplexNoise.noise(x / SCALE + 20, y / SCALE + 20);
				temperature[x][y] = Utils.map(perlin, -1, 1, MIN_TEMPERATURE, MAX_TEMPERATURE);
			}
		}
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