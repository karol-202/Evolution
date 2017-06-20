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
import pl.karol202.evolution.entity.behaviour.SavableBehaviour;
import pl.karol202.evolution.genes.Allele;
import pl.karol202.evolution.genes.Gene;
import pl.karol202.evolution.genes.GeneType;
import pl.karol202.evolution.genes.Genotype;
import pl.karol202.evolution.simulation.Simulation;

import java.util.Random;
import java.util.stream.Stream;

public class Entity
{
	private float x;
	private float y;
	private float energy;
	
	private Entities entities;
	private Genotype genotype;
	private Sex sex;
	private float size;
	private float speed;
	private float maxEnergy;
	private float energyPerSecond;
	private float eatingSpeed;
	private float sightRange;
	private float eatStartEnergyThreshold;
	
	private ComponentManager componentManager;
	private BehaviourManager behaviourManager;
	
	public Entity(Entities entities, float x, float y, Genotype genotype)
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
		sex = getSexFromGenes();
		size = genotype.getFloatProperty(GeneType.MSZ);
		speed = genotype.getFloatProperty(GeneType.MSP);
		maxEnergy = genotype.getFloatProperty(GeneType.EMX);
		energyPerSecond = genotype.getFloatProperty(GeneType.EPS);
		eatingSpeed = genotype.getFloatProperty(GeneType.FSP);
		sightRange = genotype.getFloatProperty(GeneType.CSR);
		eatStartEnergyThreshold = genotype.getFloatProperty(GeneType.BFS);
	}

	private Sex getSexFromGenes()
	{
		Gene sexGene = genotype.getGeneOfTypeAndLevel(GeneType.MSX, 0);
		Allele alleleA = sexGene.getAlleleA();
		Allele alleleB = sexGene.getAlleleB();
		if(alleleA == Allele.DOMINANT && alleleB == Allele.DOMINANT) return Sex.NEUTER;
		else if(alleleA == Allele.RECESSIVE && alleleB == Allele.RECESSIVE) return Sex.FEMALE;
		else return Sex.MALE;
	}
	
	private void addComponents()
	{
		componentManager = new ComponentManager(this, entities);
		componentManager.addComponents();
	}
	
	private void addBehaviours()
	{
		behaviourManager = new BehaviourManager(this, componentManager);
		behaviourManager.addBehaviours();
	}
	
	void update()
	{
		componentManager.update();
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
	
	void setEnergy(float energy)
	{
		this.energy = energy;
	}
	
	Stream<SavableComponent> getSavableComponentsStream()
	{
		return componentManager.getSavableComponentsStream();
	}
	
	Stream<SavableBehaviour> getSavableBehavioursStream()
	{
		return behaviourManager.getSavableBehavioursStream();
	}
	
	String getCurrentBehaviourName()
	{
		return behaviourManager.getCurrentBehaviourName();
	}
	
	int getCurrentBehaviourId()
	{
		return behaviourManager.getCurrentBehaviourId();
	}
	
	void setCurrentBehaviourId(int id)
	{
		behaviourManager.setCurrentBehaviourId(id);
	}
	
	public Genotype getGenotype()
	{
		return genotype;
	}
	
	public Sex getSex()
	{
		return sex;
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
	
	static Entity createRandomEntity(Entities entities, float x, float y, Random random)
	{
		Genotype genotype = new Genotype(random);
		return new Entity(entities, x, y, genotype);
	}
}