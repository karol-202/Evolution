package pl.karol202.evolution.entity;

public abstract class Component
{
	protected Entity entity;
	
	public Component(Entity entity)
	{
		this.entity = entity;
	}
	
	abstract void update();
}