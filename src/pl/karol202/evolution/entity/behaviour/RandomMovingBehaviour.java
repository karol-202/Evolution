package pl.karol202.evolution.entity.behaviour;

import pl.karol202.evolution.entity.Component;
import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.entity.EntityMovement;

import java.util.ArrayList;
import java.util.Random;

public class RandomMovingBehaviour extends Behaviour
{
	private EntityMovement movement;
	private Random random;
	
	public RandomMovingBehaviour(Entity entity, ArrayList<Component> components, ArrayList<Behaviour> behaviours)
	{
		super(entity, components, behaviours);
		movement = (EntityMovement) getComponent(EntityMovement.class);
		random = new Random();
	}
	
	@Override
	public void update()
	{
		if(!movement.isMoving()) movement.setTarget(random.nextFloat() * 1023, random.nextFloat() * 1023);
	}
	
	@Override
	public String getName()
	{
		return "Losowe poruszanie się";
	}
}