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

import pl.karol202.evolution.world.Plant;
import pl.karol202.evolution.world.Plants;

import java.util.Comparator;
import java.util.stream.Stream;

public class EntitySight extends Component
{
	private enum Mode
	{
		PLANT, PARTNER
	}
	
	private static final float MIN_PLANT_HEALTH = 20;
	
	private Plants plants;
	private Entities entities;
	private float sightRange;
	private Mode mode;
	
	private Plant nearestPlant;
	private Stream<Entity> potentialPartners;
	
	EntitySight(Entity entity, Plants plants, Entities entities, float sightRange)
	{
		super(entity);
		this.plants = plants;
		this.entities = entities;
		this.sightRange = sightRange;
	}
	
	@Override
	void update()
	{
		if(mode == Mode.PLANT)
		{
			findNearestPlant();
			potentialPartners = null;
		}
		else if(mode == Mode.PARTNER)
		{
			findPotentialPartners();
			nearestPlant = null;
		}
	}
	
	private void findNearestPlant()
	{
		nearestPlant = plants.getPlantsStream().filter(p -> getDistanceToPlant(p) <= sightRange)
											   .filter(p -> p.getHealth() >= MIN_PLANT_HEALTH)
											   .min((p1, p2) -> Math.round(getDistanceToPlant(p1) - getDistanceToPlant(p2)))
											   .orElse(null);
	}
	
	private float getDistanceToPlant(Plant plant)
	{
		float x = plant.getX() - entity.getX();
		float y = plant.getY() - entity.getY();
		return (float) Math.hypot(x, y);
	}
	
	private void findPotentialPartners()
	{
		potentialPartners = entities.getEntitiesStream().filter(p -> getDistanceToPartner(p) <= sightRange)
														.filter(p -> areEntitiesOfOpponentSexes(entity, p));
	}
	
	private float getDistanceToPartner(Entity partner)
	{
		float x = partner.getX() - entity.getX();
		float y = partner.getY() - entity.getY();
		return (float) Math.hypot(x, y);
	}
	
	private boolean areEntitiesOfOpponentSexes(Entity entityA, Entity entityB)
	{
		return entityA.getSex() == Sex.MALE && entityB.getSex() == Sex.FEMALE ||
			   entityA.getSex() == Sex.FEMALE && entityB.getSex() == Sex.MALE;
	}
	
	public void enablePlantMode()
	{
		mode = Mode.PLANT;
	}
	
	public void enablePartnerMode()
	{
		mode = Mode.PARTNER;
	}
	
	public Plant getNearestPlant()
	{
		return nearestPlant;
	}
	
	public Stream<Entity> getPotentialPartners()
	{
		return potentialPartners;
	}
	
	public Comparator<Entity> getEntitiesDistanceComparator()
	{
		return (e1, e2) -> Math.round(getDistanceToPartner(e1) - getDistanceToPartner(e2));
	}
	
	public Plants getPlants()
	{
		return plants;
	}
	
	public Entities getEntities()
	{
		return entities;
	}
	
	public float getSightRange()
	{
		return sightRange;
	}
}