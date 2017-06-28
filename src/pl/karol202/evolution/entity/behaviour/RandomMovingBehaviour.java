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
package pl.karol202.evolution.entity.behaviour;

import pl.karol202.evolution.entity.ComponentManager;
import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.entity.EntityMovement;
import pl.karol202.evolution.ui.main.ViewInfo;
import pl.karol202.evolution.world.World;

import java.awt.*;
import java.util.Random;

public class RandomMovingBehaviour extends Behaviour
{
	private static final float MAX_RANDOM_DISTANCE = 512f;
	
	static final int BEHAVIOUR_ID = 0;
	
	private EntityMovement movement;
	private Random random;
	
	RandomMovingBehaviour(Entity entity, ComponentManager componentManager, BehaviourManager behaviours)
	{
		super(entity, behaviours);
		movement = (EntityMovement) componentManager.getComponent(EntityMovement.class);
		random = new Random();
	}
	
	@Override
	public void update()
	{
		if(!movement.isMoving()) newTarget();
	}
	
	private void newTarget()
	{
		float randomDistance = random.nextFloat() * MAX_RANDOM_DISTANCE;
		float randomAngle = (float) (random.nextFloat() * 2 * Math.PI);
		float randomXOffset = (float) (randomDistance * Math.cos(randomAngle));
		float randomYOffset = (float) (randomDistance * Math.sin(randomAngle));
		float x = entity.getBornPosition().getX() + randomXOffset;
		float y = entity.getBornPosition().getY() + randomYOffset;
		if(x < 0) x = 0;
		if(x > World.getWorldWidth()) x = World.getWorldWidth();
		if(y < 0) y = 0;
		if(y > World.getWorldHeight()) y = World.getWorldHeight();
		movement.setTarget(x, y);
	}
	
	@Override
	public void drawBehaviour(Graphics2D g, ViewInfo viewInfo)
	{
		Rectangle bounds = getMovementAreaBounds(viewInfo);
		
		g.setColor(new Color(0, 0, 0, 32));
		g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
	private Rectangle getMovementAreaBounds(ViewInfo viewInfo)
	{
		int size = (int) (MAX_RANDOM_DISTANCE * 2 * viewInfo.getScale());
		float left = (entity.getBornPosition().getX() * viewInfo.getScale()) + viewInfo.getXPosition() - (size / 2);
		float top = (entity.getBornPosition().getY() * viewInfo.getScale()) + viewInfo.getYPosition() - (size / 2);
		
		return new Rectangle((int) left, (int) top, size, size);
	}
	
	@Override
	public int getId()
	{
		return BEHAVIOUR_ID;
	}
	
	@Override
	public String getName()
	{
		return "Losowe poruszanie się";
	}
}