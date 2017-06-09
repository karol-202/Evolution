package pl.karol202.evolution.entity;

import pl.karol202.evolution.genes.Genotype;

import java.util.Random;

public class Entity
{
	private float x;
	private float y;
	
	private Genotype genotype;
	private float size;
	
	Entity(float x, float y, Genotype genotype)
	{
		this.x = x;
		this.y = y;
		this.genotype = genotype;
		setProperties();
	}
	
	private void setProperties()
	{
		setSize();
	}
	
	private void setSize()
	{
		size = 20;
	}
	
	public static Entity createRandomEntity(float x, float y, Random random)
	{
		Genotype genotype = new Genotype(random);
		return new Entity(x, y, genotype);
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public Genotype getGenotype()
	{
		return genotype;
	}
	
	public float getSize()
	{
		return size;
	}
}