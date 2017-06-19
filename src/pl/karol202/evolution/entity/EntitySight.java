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

public class EntitySight extends Component
{
	private static final float MIN_PLANT_HEALTH = 20;
	
	private Plants plants;
	private float sightRange;
	
	private Plant nearestPlant;
	
	public EntitySight(Entity entity, Plants plants, float sightRange)
	{
		super(entity);
		this.plants = plants;
		this.sightRange = sightRange;
	}
	
	@Override
	void update()
	{
		findNearestPlant();
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
	
	public Plant getNearestPlant()
	{
		return nearestPlant;
	}
	
	public Plants getPlants()
	{
		return plants;
	}
}