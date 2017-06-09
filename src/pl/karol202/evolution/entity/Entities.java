package pl.karol202.evolution.entity;

import java.util.ArrayList;
import java.util.Random;

public class Entities
{
	private Random random;
	private ArrayList<Entity> entities;
	private int selectedEntity;
	
	public Entities(Random random)
	{
		this.random = random;
		entities = new ArrayList<>();
		selectedEntity = -1;
	}
	
	public void generateEntities()
	{
		entities.add(Entity.createRandomEntity(100, 100, random));
	}
	
	public void selectEntity(Entity entity)
	{
		if(!entities.contains(entity)) throw new RuntimeException("Unknown entity: " + entity);
		selectedEntity = entities.indexOf(entity);
	}
	
	public void selectNothing()
	{
		selectedEntity = -1;
	}
	
	public Entity[] getEntities()
	{
		return entities.toArray(new Entity[entities.size()]);
	}
	
	public Entity getSelectedEntity()
	{
		if(selectedEntity == -1) return null;
		return entities.get(selectedEntity);
	}
	
	public int getSelectedEntityIndex()
	{
		return selectedEntity;
	}
}