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

import pl.karol202.evolution.entity.Entity;

public class Reproduction
{
	private static final int REPRODUCING_TIME = 4 * 1000;
	
	private Entity entityA;
	private Entity entityB;
	private ReproduceBehaviour behaviourA;
	private ReproduceBehaviour behaviourB;
	
	private long startTime;
	
	Reproduction(Entity entityA, Entity entityB)
	{
		this.entityA = entityA;
		this.entityB = entityB;
		
		startTime = System.currentTimeMillis();
		
		startReproducing();
	}
	
	private void startReproducing()
	{
		behaviourA = entityA.reproduce(this);
		behaviourB = entityB.reproduce(this);
	}
	
	void update()
	{
		if(isReproducingDone())
		{
			createChild();
			endReproducing();
		}
	}
	
	private boolean isReproducingDone()
	{
		return startTime + REPRODUCING_TIME <= System.currentTimeMillis();
	}
	
	private void createChild()
	{
		System.out.println("child!!!");
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
}