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
import pl.karol202.evolution.utils.Utils;
import pl.karol202.evolution.utils.Vector2;
import pl.karol202.evolution.world.World;

import java.awt.*;
import java.util.Random;
import java.util.stream.Stream;

public class ReproduceBehaviour extends SavableBehaviour
{
	static final int BEHAVIOUR_ID = 2;
	
	private static final float MAX_RANDOM_DISTANCE = 128;
	private static final float ARC_DIAMETER = 60;
	
	private EntityMovement movement;
	private EntitySight sight;
	private EntitySmell smell;
	
	private Entities entities;
	private Random random;
	
	private Reproduction reproduction;
	private Entity partner;
	
	ReproduceBehaviour(Entity entity, ComponentManager components, BehaviourManager behaviours)
	{
		super(entity, behaviours);
		
		movement = (EntityMovement) components.getComponent(EntityMovement.class);
		sight = (EntitySight) components.getComponent(EntitySight.class);
		smell = (EntitySmell) components.getComponent(EntitySmell.class);
		
		entities = sight.getEntities();
		random = new Random();
	}
	
	@Override
	public void update()
	{
		if(partner == null) findPartner();
		else if(isReproducing()) reproduction.update();
		else if(!hasReachedPartner()) goToPartner();
		else if(!partner.isReproducing()) new Reproduction(entities, entity, partner);
	}
	
	private void findPartner()
	{
		sight.enablePartnerMode();
		partner = getNearestPartnerReadyToReproduce();
		if(partner == null && !movement.isMoving()) newRandomTarget();
	}
	
	private Entity getNearestPartnerReadyToReproduce()
	{
		Stream<Entity> potentialPartners = sight.getSightRange() > smell.getSmellRange() ?
										   sight.getPotentialPartners() : smell.getPotentialPartners();
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
		if(partner.isReproducing()) partner = null;
	}
	
	private boolean isReproducing()
	{
		return reproduction != null;
	}
	
	public boolean isBusy()
	{
		return partner != null;
	}
	
	public void reproduce(Reproduction reproduction)
	{
		if(isReproducing()) throw new RuntimeException("Trying to reproduce with entity that is already reproducing.");
		this.reproduction = reproduction;
		this.partner = reproduction.getPartner(entity);
		movement.stop();
	}
	
	void endReproducing()
	{
		reproduction = null;
		partner = null;
		entity.setRandomReproduceCooldown();
		behaviours.abandonCurrentBehaviour();
	}
	
	@Override
	public void drawBehaviour(Graphics2D g, ViewInfo viewInfo, boolean selected)
	{
		if(partner == null) return;
		drawLine(g, viewInfo);
		if(reproduction != null && selected) drawArc(g, viewInfo);
	}
	
	private void drawLine(Graphics2D g, ViewInfo info)
	{
		Point entityPos = transform(info, entity.getX(), entity.getY());
		Point partnerPos = transform(info, partner.getX(), partner.getY());
		
		g.setStroke(new BasicStroke(4));
		g.setColor(Color.MAGENTA);
		g.drawLine(entityPos.x, entityPos.y, partnerPos.x, partnerPos.y);
	}
	
	private Point transform(ViewInfo info, float x, float y)
	{
		return new Point((int) (x * info.getScale() + info.getXPosition()), (int) (y * info.getScale() + info.getYPosition()));
	}
	
	private void drawArc(Graphics2D g, ViewInfo info)
	{
		Rectangle bounds = getReproductionBounds(info);
		float reamingTime = reproduction.getReamingTime();
		int angle = (int) Utils.map(reamingTime, Reproduction.REPRODUCING_TIME, 0, 0, -360);
		
		g.setColor(Color.MAGENTA);
		g.setStroke(new BasicStroke(4));
		g.drawArc(bounds.x, bounds.y, bounds.width, bounds.height, 90, angle);
	}
	
	private Rectangle getReproductionBounds(ViewInfo info)
	{
		float worldX = Utils.lerp(0.5f, entity.getX(), partner.getX());
		float worldY = Utils.lerp(0.5f, entity.getY(), partner.getY());
		
		int size = (int) (ARC_DIAMETER * info.getScale());
		int screenX = (int) ((worldX * info.getScale()) + info.getXPosition() - (size / 2));
		int screenY = (int) ((worldY * info.getScale()) + info.getYPosition() - (size / 2));
		return new Rectangle(screenX, screenY, size, size);
	}
	
	@Override
	public void loadState(BehaviourState state)
	{
		reproduction = ReproductionsLoader.instance.loadReproduction(state.getInt("reproduction"));
		partner = entities.getEntityById(state.getInt("partnerId"));
	}
	
	@Override
	void saveState(BehaviourState state)
	{
		state.putInt("reproduction", ReproductionsLoader.instance.saveReproduction(reproduction));
		state.putInt("partnerId", entities.getEntityId(partner));
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