/*
  Copyright 2017 karol-202
 
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
 
        http://www.apache.org/licenses/LICENSE-2.0
 
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
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
	
	public boolean isMoving()
	{
		return target != null;
	}
	
	private Vector2 getPosition()
	{
		return new Vector2(entity.x, entity.y);
	}
}