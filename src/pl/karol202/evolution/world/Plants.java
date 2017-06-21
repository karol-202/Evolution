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

import pl.karol202.evolution.utils.Utils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.karol202.evolution.world.World.MAX_OFFSET;
import static pl.karol202.evolution.world.World.MIN_OFFSET;

public class Plants
{
	private static final double[] NOISE_OCTAVES = { 0.5, 0.25, 0.25 };
	
	private Random random;
	private World world;
	private int noiseFrequency;
	private float leastMinDistance;
	private float greatestMinDistance;
	private List<Plant> plants;
	
	Plants(Random random, World world)
	{
		this.random = random;
		this.world = world;
		noiseFrequency = 2048;
		leastMinDistance = 20;
		greatestMinDistance = 200;
	}
	
	void generatePlants()
	{
		int width = world.getWidth();
		int height = world.getHeight();
		int xOffset = getRandomOffset();
		int yOffset = getRandomOffset();
		float[][] minDistance = new float[width][height];
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
			{
				float noise = (float) OctaveSimplexNoise.noise((x / (float) noiseFrequency) + xOffset, (y / (float) noiseFrequency) + yOffset, NOISE_OCTAVES);
				minDistance[x][y] = Utils.map(noise, -1, 1, leastMinDistance, greatestMinDistance);
			}
		plants = PoissonDisk.poissonDisk(minDistance, greatestMinDistance, 30).stream().map(Plant::new).collect(Collectors.toList());
	}
	
	private int getRandomOffset()
	{
		return random.nextInt(MAX_OFFSET - MIN_OFFSET) + MIN_OFFSET;
	}
	
	public void update()
	{
		plants.forEach(Plant::update);
	}
	
	public int getNoiseFrequency()
	{
		return noiseFrequency;
	}
	
	public void setNoiseFrequency(int noiseFrequency)
	{
		this.noiseFrequency = noiseFrequency;
	}
	
	public float getLeastMinDistance()
	{
		return leastMinDistance;
	}
	
	public void setLeastMinDistance(float leastMinDistance)
	{
		this.leastMinDistance = leastMinDistance;
	}
	
	public float getGreatestMinDistance()
	{
		return greatestMinDistance;
	}
	
	public void setGreatestMinDistance(float greatestMinDistance)
	{
		this.greatestMinDistance = greatestMinDistance;
	}
	
	public Stream<Plant> getPlantsStream()
	{
		return plants.stream();
	}
	
	void removePlants()
	{
		plants.clear();
	}
	
	void addPlant(Plant plant)
	{
		plants.add(plant);
	}
	
	public int getPlantId(Plant plant)
	{
		return plants.indexOf(plant);
	}
	
	public Plant getPlantById(int id)
	{
		if(id == -1) return null;
		return plants.get(id);
	}
}