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

import pl.karol202.evolution.entity.behaviour.BehaviourManager;
import pl.karol202.evolution.genes.GeneType;
import pl.karol202.evolution.genes.Genotype;
import pl.karol202.evolution.simulation.Simulation;

import java.util.ArrayList;
import java.util.Random;

public class Entity
{
	private float x;
	private float y;
	private float energy;
	
	private Entities entities;
	private Genotype genotype;
	private float size;
	private float speed;
	private float maxEnergy;
	private float energyPerSecond;
	private float eatingSpeed;
	private float sightRange;
	private float eatStartEnergyThreshold;
	
	private ArrayList<Component> components;
	private BehaviourManager behaviourManager;
	
	private Entity(Entities entities, float x, float y, Genotype genotype)
	{
		this.entities = entities;
		this.genotype = genotype;
		setProperties();
		addComponents();
		addBehaviours();
		
		this.x = x;
		this.y = y;
		this.energy = maxEnergy;
	}
	
	private void setProperties()
	{
		size = genotype.getFloatProperty(GeneType.SIZ);
		speed = genotype.getFloatProperty(GeneType.SPD);
		maxEnergy = genotype.getFloatProperty(GeneType.EMX);
		energyPerSecond = genotype.getFloatProperty(GeneType.EPS);
		eatingSpeed = 15;
		sightRange = 100;
		eatStartEnergyThreshold = 0.4f;
	}

	private void addComponents()
	{
		components = new ArrayList<>();
		components.add(new EntityMovement(this));
		components.add(new EntitySight(this, entities.getPlants(), sightRange));
		components.add(new EntityNutrition(this));
	}
	
	private void addBehaviours()
	{
		behaviourManager = new BehaviourManager(this, components);
		behaviourManager.addBehaviours();
	}
	
	static Entity createRandomEntity(Entities entities, float x, float y, Random random)
	{
		Genotype genotype = new Genotype(random);
		return new Entity(entities, x, y, genotype);
	}
	
	void update()
	{
		components.forEach(Component::update);
		behaviourManager.update();
		manageEnergy();
	}
	
	private void manageEnergy()
	{
		reduceEnergy(energyPerSecond * Simulation.deltaTime);
		if(energy < 0) die();
	}
	
	void addEnergy(float energyAmount)
	{
		energy += energyAmount;
	}
	
	void reduceEnergy(float energyAmount)
	{
		energy -= energyAmount;
	}
	
	public float getReamingEnergyCapacity()
	{
		return maxEnergy - energy;
	}
	
	private void die()
	{
		entities.removeEntity(this);
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	void setX(float x)
	{
		this.x = x;
	}
	
	void setY(float y)
	{
		this.y = y;
	}
	
	public float getEnergy()
	{
		return energy;
	}
	
	public String getCurrentBehaviourName()
	{
		return behaviourManager.getCurrentBehaviourName();
	}
	
	public Genotype getGenotype()
	{
		return genotype;
	}
	
	public float getSize()
	{
		return size;
	}
	
	public float getSpeed()
	{
		return speed;
	}
	
	public float getMaxEnergy()
	{
		return maxEnergy;
	}
	
	public float getEnergyPerSecond()
	{
		return energyPerSecond;
	}
	
	public float getEatingSpeed()
	{
		return eatingSpeed;
	}
	
	public float getSightRange()
	{
		return sightRange;
	}
	
	public float getEatStartEnergyThreshold()
	{
		return eatStartEnergyThreshold;
	}
}