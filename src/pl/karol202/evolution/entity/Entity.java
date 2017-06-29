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
import pl.karol202.evolution.entity.behaviour.ReproduceBehaviour;
import pl.karol202.evolution.entity.behaviour.Reproduction;
import pl.karol202.evolution.entity.behaviour.SavableBehaviour;
import pl.karol202.evolution.genes.Allele;
import pl.karol202.evolution.genes.Gene;
import pl.karol202.evolution.genes.GeneType;
import pl.karol202.evolution.genes.Genotype;
import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.ui.main.ViewInfo;
import pl.karol202.evolution.utils.Utils;
import pl.karol202.evolution.utils.Vector2;

import java.awt.*;
import java.util.Random;
import java.util.stream.Stream;

public class Entity
{
	private final Vector2 bornPosition;
	
	private float x;
	private float y;
	private float energy;
	private float timeOfLife;
	private float reproduceCooldown;
	
	private Entities entities;
	private Genotype genotype;
	private Sex sex;
	private float size;
	private float speed;
	private float optimalTemperature;
	private float optimalHumidity;
	private float maxTimeOfLife;
	private float maxEnergy;
	private float energyPerSecond;
	private float temperatureEnergyLoss;
	private float humidityEnergyLoss;
	private float eatingSpeed;
	private float sightRange;
	private float eatStartEnergyThreshold;
	private float reproduceReadyEnergyThreshold;
	private float minReproduceCooldown;
	private float maxReproduceCooldown;
	
	private ComponentManager componentManager;
	private BehaviourManager behaviourManager;
	
	public Entity(Entities entities, float x, float y, Genotype genotype)
	{
		this(entities, x, y, genotype, new Vector2(x, y));
	}
	
	Entity(Entities entities, float x, float y, Genotype genotype, Vector2 bornPosition)
	{
		this.bornPosition = bornPosition;
		
		this.entities = entities;
		this.genotype = genotype;
		setProperties();
		addComponents();
		addBehaviours();
		
		this.x = x;
		this.y = y;
		this.energy = maxEnergy;
		this.timeOfLife = 0;
		setRandomReproduceCooldown();
	}
	
	private void setProperties()
	{
		sex = getSexFromGenes();
		size = genotype.getFloatProperty(GeneType.MSZ);
		speed = genotype.getFloatProperty(GeneType.MSP);
		optimalTemperature = genotype.getFloatProperty(GeneType.MOT);
		optimalHumidity = clamp(genotype.getFloatProperty(GeneType.MOH), 0, 100);
		maxTimeOfLife = clamp(genotype.getFloatProperty(GeneType.MLT), 20, 300);
		maxEnergy = genotype.getFloatProperty(GeneType.EMX);
		energyPerSecond = genotype.getFloatProperty(GeneType.EPS);
		temperatureEnergyLoss = clamp(genotype.getFloatProperty(GeneType.ETL), 0.1f, 2.5f);
		humidityEnergyLoss = clamp(genotype.getFloatProperty(GeneType.EHL), 0.1f, 2f);
		eatingSpeed = genotype.getFloatProperty(GeneType.FSP);
		sightRange = genotype.getFloatProperty(GeneType.CSR);
		eatStartEnergyThreshold = genotype.getFloatProperty(GeneType.BFS) * maxEnergy;
		reproduceReadyEnergyThreshold = genotype.getFloatProperty(GeneType.BRR) * maxEnergy;
		minReproduceCooldown = clamp(genotype.getFloatProperty(GeneType.BRN), 15, 45);
		maxReproduceCooldown = clamp(genotype.getFloatProperty(GeneType.BRX), 60, 83);
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
	
	private float clamp(float value, float min, float max)
	{
		if(value < min) value = min;
		if(value > max) value = max;
		return value;
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
		manageTimeOfLife();
		reproduceCooldown -= Simulation.deltaTime;
	}
	
	private void manageEnergy()
	{
		reduceEnergy(energyPerSecond * Simulation.deltaTime);
		reduceEnergyDueToTemperature();
		reduceEnergyDueToHumidity();
		if(energy < 0) die();
	}
	
	private void reduceEnergyDueToTemperature()
	{
		float temperature = entities.getTemperature(x, y);
		float difference = Math.abs(temperature - optimalTemperature);
		float energyLoss = (difference / 20) * temperatureEnergyLoss * Simulation.deltaTime;
		reduceEnergy(energyLoss);
	}
	
	private void reduceEnergyDueToHumidity()
	{
		float humidity = entities.getHumidity(x, y);
		float difference = Math.abs(humidity - optimalHumidity);
		float energyLoss = (difference / 20) * humidityEnergyLoss * Simulation.deltaTime;
		reduceEnergy(energyLoss);
	}
	
	private void manageTimeOfLife()
	{
		timeOfLife += Simulation.deltaTime;
		if(timeOfLife >= maxTimeOfLife) die();
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
	
	public boolean isDead()
	{
		return energy < 0 || timeOfLife >= maxTimeOfLife;
	}
	
	public boolean shouldEat()
	{
		return energy < eatStartEnergyThreshold;
	}
	
	public boolean isReadyToReproduce()
	{
		return energy >= reproduceReadyEnergyThreshold && reproduceCooldown <= 0 && !behaviourManager.isReproducing();
	}
	
	public boolean isInRut()
	{
		return behaviourManager.isInRut();
	}
	
	public boolean isReproducing()
	{
		return behaviourManager.isReproducing();
	}
	
	public ReproduceBehaviour reproduce(Reproduction reproduction)
	{
		ReproduceBehaviour behaviour = behaviourManager.reproduce();
		behaviour.reproduce(reproduction);
		return behaviour;
	}
	
	public void setRandomReproduceCooldown()
	{
		this.reproduceCooldown = Utils.randomFloat(minReproduceCooldown, maxReproduceCooldown);
	}
	
	public void drawCurrentBehaviour(Graphics2D g, ViewInfo viewInfo, boolean selected)
	{
		behaviourManager.drawCurrentBehaviour(g, viewInfo, selected);
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
	
	public float getTimeOfLife()
	{
		return timeOfLife;
	}
	
	void setTimeOfLife(float timeOfLife)
	{
		this.timeOfLife = timeOfLife;
	}
	
	float getReproduceCooldown()
	{
		return reproduceCooldown;
	}
	
	void setReproduceCooldown(float reproduceCooldown)
	{
		this.reproduceCooldown = reproduceCooldown;
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
	
	public float getOptimalTemperature()
	{
		return optimalTemperature;
	}
	
	public float getOptimalHumidity()
	{
		return optimalHumidity;
	}
	
	public float getMaxTimeOfLife()
	{
		return maxTimeOfLife;
	}
	
	public float getMaxEnergy()
	{
		return maxEnergy;
	}
	
	public float getEnergyPerSecond()
	{
		return energyPerSecond;
	}
	
	public float getTemperatureEnergyLoss()
	{
		return temperatureEnergyLoss;
	}
	
	public float getHumidityEnergyLoss()
	{
		return humidityEnergyLoss;
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
	
	public float getReproduceReadyEnergyThreshold()
	{
		return reproduceReadyEnergyThreshold;
	}
	
	public float getMinReproduceCooldown()
	{
		return minReproduceCooldown;
	}
	
	public float getMaxReproduceCooldown()
	{
		return maxReproduceCooldown;
	}
	
	public Vector2 getBornPosition()
	{
		return bornPosition;
	}
	
	static Entity createRandomEntity(Entities entities, float x, float y, Random random)
	{
		Genotype genotype = new Genotype(random);
		return new Entity(entities, x, y, genotype);
	}
}