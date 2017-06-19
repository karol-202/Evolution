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
import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.utils.Vector2;
import pl.karol202.evolution.world.Plant;
import pl.karol202.evolution.world.Plants;
import pl.karol202.evolution.world.World;

import java.util.Random;

public class FoodSeekBehaviour extends SavableBehaviour
{
	public static final int BEHAVIOUR_ID = 1;
	
	private static final float MAX_RANDOM_DISTANCE = 200f;
	private static final float MAX_ENERGY_THRESHOLD = 0.98f;
	
	private EntityMovement movement;
	private EntitySight sight;
	private EntityNutrition nutrition;
	
	private Random random;
	private Plants plants;
	
	private Plant eatenPlant;
	
	public FoodSeekBehaviour(Entity entity, ComponentManager componentManager, BehaviourManager behaviours)
	{
		super(entity, behaviours);
		movement = (EntityMovement) componentManager.getComponent(EntityMovement.class);
		sight = (EntitySight) componentManager.getComponent(EntitySight.class);
		nutrition = (EntityNutrition) componentManager.getComponent(EntityNutrition.class);
		
		random = new Random();
		plants = sight.getPlants();
	}
	
	@Override
	public void update()
	{
		Plant nearestPlant = sight.getNearestPlant();
		if(nearestPlant != null && hasReachedPlant(nearestPlant)) eat(nearestPlant);
		else if(eatenPlant != null) eat(eatenPlant);
		else if(nearestPlant != null) setPlantTarget(nearestPlant);
		else if(!movement.isMoving()) setRandomTarget();
		
		if(canStopEating()) stopEating();
	}
	
	private boolean canStopEating()
	{
		return entity.getEnergy() > MAX_ENERGY_THRESHOLD * entity.getMaxEnergy();
	}
	
	private void stopEating()
	{
		behaviours.abandonCurrentBehaviour();
		eatenPlant = null;
	}
	
	private boolean hasReachedPlant(Plant plant)
	{
		Vector2 plantPos = new Vector2(plant.getX(), plant.getY());
		Vector2 difference = plantPos.sub(movement.getPosition());
		float distance = difference.length();
		return distance <= EntityMovement.CLOSE_ENOUGH_DISTANCE;
	}
	
	private void eat(Plant plant)
	{
		eatenPlant = plant;
		float energy = entity.getEatingSpeed() * Simulation.deltaTime;
		if(energy > plant.getHealth()) energy = plant.getHealth();
		if(energy > entity.getReamingEnergyCapacity()) energy = entity.getReamingEnergyCapacity();
		nutrition.eat(energy);
		plant.reduceHealth(energy);
		if(plant.getHealth() <= 0 || entity.getReamingEnergyCapacity() <= 0) eatenPlant = null;
	}
	
	private void setPlantTarget(Plant plant)
	{
		movement.setTarget(plant.getX(), plant.getY());
	}
	
	private void setRandomTarget()
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
	
	@Override
	void loadState(BehaviourState state)
	{
		int plantId = state.getInt("eatenPlant");
		eatenPlant = plants.getPlantById(plantId);
	}
	
	@Override
	void saveState(BehaviourState state)
	{
		int plantId = plants.getPlantId(eatenPlant);
		state.putInt("eatenPlant", plantId);
	}
	
	@Override
	public int getId()
	{
		return BEHAVIOUR_ID;
	}
	
	@Override
	public String getName()
	{
		return "Szukanie jedzenia";
	}
}