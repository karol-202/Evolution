package pl.karol202.evolution.world;

import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.utils.Vector2;

public class Plant
{
	private static final float REGENERATION_SPEED = 1.5f;
	
	private float x;
	private float y;
	private float health;
	
	public Plant(Vector2 vector)
	{
		this(vector.getX(), vector.getY());
	}
	
	public Plant(float x, float y)
	{
		this.x = x;
		this.y = y;
		this.health = 50;
	}
	
	public void update()
	{
		if(health > 100) return;
		health += REGENERATION_SPEED * Simulation.deltaTime;
		if(health > 100) health = 100;
 	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getHealth()
	{
		return health;
	}
}