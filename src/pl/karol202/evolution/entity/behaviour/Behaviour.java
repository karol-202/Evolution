package pl.karol202.evolution.entity.behaviour;

import pl.karol202.evolution.entity.Component;
import pl.karol202.evolution.entity.Entity;

import java.util.ArrayList;

public abstract class Behaviour
{
	protected Entity entity;
	protected ArrayList<Component> components;
	protected ArrayList<Behaviour> behaviours;
	
	public Behaviour(Entity entity, ArrayList<Component> components, ArrayList<Behaviour> behaviours)
	{
		this.entity = entity;
		this.components = components;
		this.behaviours = behaviours;
	}
	
	public abstract void update();
	
	protected Component getComponent(Class<? extends Component> type)
	{
		return components.stream().filter(c -> c.getClass() == type).findAny().orElse(null);
	}
}