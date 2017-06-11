package pl.karol202.evolution.entity.behaviour;

import pl.karol202.evolution.entity.Component;
import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.entity.EntityMovement;
import pl.karol202.evolution.world.World;

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
		if(!movement.isMoving()) newTarget();
	}
	
	private void newTarget()
	{
		float x = random.nextFloat() * (World.getWorldWidth() - 1);
		float y = random.nextFloat() * (World.getWorldHeight() - 1);
		movement.setTarget(x, y);
	}
	
	@Override
	public String getName()
	{
		return "Losowe poruszanie się";
	}
}