package pl.karol202.evolution.entity;

import java.util.ArrayList;

public class Entities
{
	private ArrayList<Entity> entities;
	
	public Entities()
	{
		entities = new ArrayList<>();
	}
	
	public void generateEntities()
	{
		entities.add(new Entity(100, 100));
	}
	
	public Entity[] getEntities()
	{
		return entities.toArray(new Entity[entities.size()]);
	}
}