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

import pl.karol202.evolution.entity.Entities;
import pl.karol202.evolution.entity.Entity;
import pl.karol202.evolution.genes.Genotype;
import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.utils.Utils;

import java.util.Random;

public class Reproduction
{
	static final int REPRODUCING_TIME = 4;
	
	private Entities entities;
	private Entity entityA;
	private Entity entityB;
	private ReproduceBehaviour behaviourA;
	private ReproduceBehaviour behaviourB;
	
	private int reamingTime;
	
	Reproduction(Entities entities, Entity entityA, Entity entityB)
	{
		this.entities = entities;
		this.entityA = entityA;
		this.entityB = entityB;
		
		reamingTime = REPRODUCING_TIME;
		
		startReproducing();
	}
	
	private void startReproducing()
	{
		behaviourA = entityA.reproduce(this);
		behaviourB = entityB.reproduce(this);
	}
	
	void update()
	{
		reamingTime -= Simulation.deltaTime;
		if(isReproducingDone())
		{
			createChild();
			endReproducing();
		}
	}
	
	private boolean isReproducingDone()
	{
		return reamingTime <= 0;
	}
	
	private void createChild()
	{
		float x = Utils.lerp(0.5f, entityA.getX(), entityB.getX());
		float y = Utils.lerp(0.5f, entityA.getY(), entityB.getY());
		Genotype genotype = new Genotype(new Random(), entityA.getGenotype(), entityB.getGenotype());
		
		Entity entity = new Entity(entities, x, y, genotype);
		entities.addEntity(entity);
	}
	
	private void endReproducing()
	{
		behaviourA.endReproducing();
		behaviourB.endReproducing();
	}
	
	Entity getPartner(Entity entity)
	{
		if(entity == entityA) return entityB;
		if(entity == entityB) return entityA;
		return null;
	}
	
	int getReamingTime()
	{
		return reamingTime;
	}
}