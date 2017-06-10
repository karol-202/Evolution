package pl.karol202.evolution.entity;

import pl.karol202.evolution.entity.behaviour.Behaviour;
import pl.karol202.evolution.entity.behaviour.RandomMovingBehaviour;
import pl.karol202.evolution.genes.GeneType;
import pl.karol202.evolution.genes.Genotype;

import java.util.ArrayList;
import java.util.Random;

public class Entity
{
	float x;
	float y;
	
	private Genotype genotype;
	private float size;
	private float speed;
	
	private ArrayList<Component> components;
	private ArrayList<Behaviour> behaviours;
	private Behaviour currentBehaviour;
	
	private Entity(float x, float y, Genotype genotype)
	{
		this.x = x;
		this.y = y;
		
		this.genotype = genotype;
		setProperties();
		addComponents();
		addBehaviours();
	}
	
	private void setProperties()
	{
		size = genotype.getFloatProperty(GeneType.SIZ);
		speed = 100;
	}

	private void addComponents()
	{
		components = new ArrayList<>();
		components.add(new EntityMovement(this));
	}
	
	private void addBehaviours()
	{
		behaviours = new ArrayList<>();
		behaviours.add(new RandomMovingBehaviour(this, components, behaviours));
		
		currentBehaviour = behaviours.get(0);
	}
	
	public static Entity createRandomEntity(float x, float y, Random random)
	{
		Genotype genotype = new Genotype(random);
		return new Entity(x, y, genotype);
	}
	
	public void update()
	{
		components.forEach(Component::update);
		currentBehaviour.update();
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
	
	public float getSpeed()
	{
		return speed;
	}
}