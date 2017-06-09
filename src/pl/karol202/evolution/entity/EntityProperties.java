package pl.karol202.evolution.entity;

import java.util.function.Function;

public enum  EntityProperties
{
	X("X", e -> toString(e.getX())),
	Y("Y", e -> toString(e.getY())),
	SIZE("Wielkość", e -> toString(e.getSize()));
	
	private String name;
	private Function<Entity, String> function;
	
	EntityProperties(String name, Function<Entity, String> function)
	{
		this.name = name;
		this.function = function;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getValueForEntity(Entity entity)
	{
		return function.apply(entity);
	}
	
	private static String toString(float f)
	{
		return Float.toString(f);
	}
}