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

import pl.karol202.evolution.entity.behaviour.Behaviour;
import pl.karol202.evolution.entity.behaviour.RandomMovingBehaviour;
import pl.karol202.evolution.genes.GeneType;
import pl.karol202.evolution.genes.Genotype;

import java.util.ArrayList;
import java.util.Random;

public class Entity
{
	float x;
	float y;
	
	private Genotype genotype;
	private float size;
	private float speed;
	
	private ArrayList<Component> components;
	private ArrayList<Behaviour> behaviours;
	private Behaviour currentBehaviour;
	
	private Entity(float x, float y, Genotype genotype)
	{
		this.x = x;
		this.y = y;
		
		this.genotype = genotype;
		setProperties();
		addComponents();
		addBehaviours();
	}
	
	private void setProperties()
	{
		size = genotype.getFloatProperty(GeneType.SIZ);
		speed = genotype.getFloatProperty(GeneType.SPD);
	}

	private void addComponents()
	{
		components = new ArrayList<>();
		components.add(new EntityMovement(this));
	}
	
	private void addBehaviours()
	{
		behaviours = new ArrayList<>();
		behaviours.add(new RandomMovingBehaviour(this, components, behaviours));
		
		currentBehaviour = behaviours.get(0);
	}
	
	public static Entity createRandomEntity(float x, float y, Random random)
	{
		Genotype genotype = new Genotype(random);
		return new Entity(x, y, genotype);
	}
	
	public void update()
	{
		components.forEach(Component::update);
		currentBehaviour.update();
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public String getCurrentBehaviourName()
	{
		return currentBehaviour.getName();
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
}