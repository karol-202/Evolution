package pl.karol202.evolution.world;

import pl.karol202.evolution.utils.Vector2;

public class Plant
{
	private float x;
	private float y;
	
	public Plant(Vector2 vector)
	{
		this(vector.getX(), vector.getY());
	}
	
	public Plant(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
}