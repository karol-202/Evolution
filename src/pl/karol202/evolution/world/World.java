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
package pl.karol202.evolution.world;

import pl.karol202.evolution.entity.Entities;
import pl.karol202.evolution.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

public class World
{
	public static final int MAX_SIZE = 8192;
	
	static final int MIN_OFFSET = -10000;
	static final int MAX_OFFSET = 10000;
	
	private static final double[] TEMPERATURE_OCTAVES = {
			0.55, 0.3
	};
	private static final double[] HUMIDITY_OCTAVES = {
			0.5, 0.25
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
	private int entitiesAmount;
	
	private int temperatureXOffset;
	private int temperatureYOffset;
	private int humidityXOffset;
	private int humidityYOffset;
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
		entitiesAmount = 5;
		
		plants = new Plants(random, this);
		entities = new Entities(random, this);
	}
	
	void generateEmptyWorld(int width, int height)
	{
		World.staticWidth = width;
		World.staticHeight = height;
		this.width = width;
		this.height = height;
		this.temperature = new float[width][height];
		this.humidity = new float[width][height];
	}
	
	public void generateRandomWorld(int width, int height)
	{
		World.staticWidth = width;
		World.staticHeight = height;
		this.width = width;
		this.height = height;
		this.temperature = new float[width][height];
		this.humidity = new float[width][height];
		
		generateTemperature(getRandomOffset(), getRandomOffset());
		generateHumidity(getRandomOffset(), getRandomOffset());
		plants.generatePlants();
		entities.generateEntities(entitiesAmount);
	}
	
	void generateTemperature(int xOffset, int yOffset)
	{
		temperatureXOffset = xOffset;
		temperatureYOffset = yOffset;
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				float noiseX = (x / (float) temperatureFrequency) + xOffset;
				float noiseY = (y / (float) temperatureFrequency) + yOffset;
				float noise = (float) OctaveSimplexNoise.noise(noiseX, noiseY, TEMPERATURE_OCTAVES);
				temperature[x][y] = Utils.map(noise, -1, 1, minTemperature, maxTemperature);
			}
		}
		listeners.forEach(OnWorldUpdateListener::onWorldUpdated);
	}
	
	void generateHumidity(int xOffset, int yOffset)
	{
		humidityXOffset = xOffset;
		humidityYOffset = yOffset;
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				float noiseX = (x / (float) humidityFrequency) + xOffset;
				float noiseY = (y / (float) humidityFrequency) + yOffset;
				float noise = (float) OctaveSimplexNoise.noise(noiseX, noiseY, HUMIDITY_OCTAVES);
				humidity[x][y] = Utils.map(noise, -1, 1, minHumidity, maxHumidity);
			}
		}
		listeners.forEach(OnWorldUpdateListener::onWorldUpdated);
	}
	
	private int getRandomOffset()
	{
		return random.nextInt(MAX_OFFSET - MIN_OFFSET) + MIN_OFFSET;
	}
	
	public float getTemperature(float x, float y)
	{
		return interpolateFromMap(temperature, x, y);
	}
	
	public float getHumidity(float x, float y)
	{
		return interpolateFromMap(humidity, x, y);
	}
	
	private float interpolateFromMap(float[][] map, float x, float y)
	{
		int xInt = (int) x;
		int yInt = (int) y;
		float xFract = x - xInt;
		float yFract = y - yInt;
		float leftTop = getValueFromMapOrClamp(map, xInt, yInt);
		float rightTop = getValueFromMapOrClamp(map, xInt + 1, yInt);
		float leftBottom = getValueFromMapOrClamp(map, xInt, yInt + 1);
		float rightBottom = getValueFromMapOrClamp(map, xInt + 1, yInt + 1);
		float interpolatedTop = Utils.lerp(xFract, leftTop, rightTop);
		float interpolatedBottom = Utils.lerp(xFract, leftBottom, rightBottom);
		return Utils.lerp(yFract, interpolatedTop, interpolatedBottom);
	}
	
	private float getValueFromMapOrClamp(float[][] map, int x, int y)
	{
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		if(x >= width) x = width - 1;
		if(y >= height) y = height - 1;
		return map[x][y];
	}
	
	public void update()
	{
		plants.update();
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
	
	public int getInitialEntitiesAmount()
	{
		return entitiesAmount;
	}
	
	public void setEntitiesAmount(int entitiesAmount)
	{
		this.entitiesAmount = entitiesAmount;
	}
	
	int getTemperatureXOffset()
	{
		return temperatureXOffset;
	}
	
	int getTemperatureYOffset()
	{
		return temperatureYOffset;
	}
	
	int getHumidityXOffset()
	{
		return humidityXOffset;
	}
	
	int getHumidityYOffset()
	{
		return humidityYOffset;
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