package pl.karol202.evolution.entity.behaviour;

import pl.karol202.evolution.entity.Component;
import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.entity.EntityMovement;

import java.util.ArrayList;

public class RandomMovingBehaviour extends Behaviour
{
	private EntityMovement movement;
	
	private boolean targetSet;
	
	public RandomMovingBehaviour(Entity entity, ArrayList<Component> components, ArrayList<Behaviour> behaviours)
	{
		super(entity, components, behaviours);
		movement = (EntityMovement) getComponent(EntityMovement.class);
	}
	
	@Override
	public void update()
	{
		if(!targetSet) movement.setTarget(300, 300);
		targetSet = true;
	}
}