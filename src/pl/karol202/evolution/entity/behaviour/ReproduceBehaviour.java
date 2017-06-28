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

import pl.karol202.evolution.entity.*;
import pl.karol202.evolution.ui.main.ViewInfo;
import pl.karol202.evolution.utils.Vector2;
import pl.karol202.evolution.world.World;

import java.awt.*;
import java.util.Random;
import java.util.stream.Stream;

public class ReproduceBehaviour extends SavableBehaviour
{
	static final int BEHAVIOUR_ID = 2;
	
	private static final int REPRODUCING_TIME = 4 * 1000;
	private static final float MAX_RANDOM_DISTANCE = 128;
	
	private EntityMovement movement;
	private EntitySight sight;
	
	private Entities entities;
	private Random random;
	
	private Entity partner;
	private ReproduceBehaviour partnerBehaviour;
	private boolean reproducing;
	private long reproducingStartTime;
	
	ReproduceBehaviour(Entity entity, ComponentManager components, BehaviourManager behaviours)
	{
		super(entity, behaviours);
		
		movement = (EntityMovement) components.getComponent(EntityMovement.class);
		sight = (EntitySight) components.getComponent(EntitySight.class);
		
		entities = sight.getEntities();
		random = new Random();
	}
	
	@Override
	public void update()
	{
		if(partner == null) findPartner();
		else if(reproducing) updateWhileReproducing();
		else if(!movement.isMoving() && !hasReachedPartner()) goToPartner();
		else if(hasReachedPartner()) startReproducing();
	}
	
	private void findPartner()
	{
		sight.enablePartnerMode();
		partner = getNearestPartnerReadyToReproduce();
		if(partner != null) partnerBehaviour = partner.reproduceWith(entity);
		else if(!movement.isMoving()) newRandomTarget();
	}
	
	private Entity getNearestPartnerReadyToReproduce()
	{
		Stream<Entity> potentialPartners = sight.getPotentialPartners();
		if(potentialPartners == null) return null;
		return potentialPartners.filter(Entity::isReadyToReproduce)
								.min(sight.getEntitiesDistanceComparator())
								.orElse(null);
	}
	
	private void newRandomTarget()
	{
		float randomDistance = random.nextFloat() * MAX_RANDOM_DISTANCE;
		float randomAngle = (float) (random.nextFloat() * 2 * Math.PI);
		float randomXOffset = (float) (randomDistance * Math.cos(randomAngle));
		float randomYOffset = (float) (randomDistance * Math.sin(randomAngle));
		float x = entity.getX() + randomXOffset;
		float y = entity.getY() + randomYOffset;
		if(x < 0) x = 0;
		if(x > World.getWorldWidth()) x = World.getWorldWidth();
		if(y < 0) y = 0;
		if(y > World.getWorldHeight()) y = World.getWorldHeight();
		movement.setTarget(x, y);
	}
	
	private boolean hasReachedPartner()
	{
		Vector2 partnerPos = new Vector2(partner.getX(), partner.getY());
		Vector2 difference = partnerPos.sub(movement.getPosition());
		float distance = difference.length();
		return distance <= EntityMovement.CLOSE_ENOUGH_DISTANCE;
	}
	
	private void goToPartner()
	{
		movement.setTarget(partner.getX(), partner.getY());
	}
	
	private void startReproducing()
	{
		reproducing = true;
		reproducingStartTime = System.currentTimeMillis();
	}
	
	private void updateWhileReproducing()
	{
		if(isReproducingDone() && partnerBehaviour.isReproducingDone())
		{
			partnerBehaviour.endReproducing();
			createChild();
			endReproducing();
		}
	}
	
	private boolean isReproducingDone()
	{
		return reproducingStartTime + REPRODUCING_TIME <= System.currentTimeMillis();
	}
	
	private void endReproducing()
	{
		partner = null;
		partnerBehaviour = null;
		reproducing = false;
		reproducingStartTime = -1;
		behaviours.abandonCurrentBehaviour();
	}
	
	private void createChild()
	{
		System.out.println("child!!!");
	}
	
	public void reproduceWith(Entity entity)
	{
		partner = entity;
	}
	
	@Override
	public void drawBehaviour(Graphics2D g, ViewInfo viewInfo)
	{
		
	}
	
	@Override
	public void loadState(BehaviourState state)
	{
		
	}
	
	@Override
	void saveState(BehaviourState state)
	{
		
	}
	
	@Override
	public int getId()
	{
		return BEHAVIOUR_ID;
	}
	
	@Override
	public String getName()
	{
		return "Rozmnażanie";
	}
}