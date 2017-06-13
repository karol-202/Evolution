package pl.karol202.evolution.world;

import pl.karol202.evolution.utils.OctaveSimplexNoise;
import pl.karol202.evolution.utils.PoissonDisk;
import pl.karol202.evolution.utils.Utils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
	
	public Plants(Random random, World world)
	{
		this.random = random;
		this.world = world;
		noiseFrequency = 2048;
		leastMinDistance = 30;
		greatestMinDistance = 120;
	}
	
	public void generatePlants()
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
	
	public List<Plant> getPlants()
	{
		return plants;
	}
}