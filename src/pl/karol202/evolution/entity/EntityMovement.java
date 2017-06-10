package pl.karol202.evolution.entity;

import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.utils.Vector2;

public class EntityMovement extends Component
{
	private static final float CLOSE_ENOUGH_DISTANCE = 1;
	
	private Vector2 target;
	
	public EntityMovement(Entity entity)
	{
		super(entity);
	}
	
	void update()
	{
		if(target == null) return;
		Vector2 position = getPosition();
		Vector2 targetDirection = target.sub(position).normalize();
		Vector2 offset = targetDirection.mul(entity.getSpeed() * Simulation.deltaTime);
		entity.x += offset.getX();
		entity.y += offset.getY();
		if(isAtTarget()) stop();
	}
	
	private boolean isAtTarget()
	{
		Vector2 difference = target.sub(getPosition());
		float distance = difference.length();
		return distance < CLOSE_ENOUGH_DISTANCE;
	}
	
	public void setTarget(float x, float y)
	{
		target = new Vector2(x, y);
	}
	
	public void stop()
	{
		target = null;
	}
	
	private Vector2 getPosition()
	{
		return new Vector2(entity.x, entity.y);
	}
}