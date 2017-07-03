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

import java.util.stream.Stream;

public class EntitySmell extends Component
{
	private Entities entities;
	private float smellRange;
	
	private Stream<Entity> potentialPartners;
	
	EntitySmell(Entity entity, Entities entities, float smellRange)
	{
		super(entity);
		this.entities = entities;
		this.smellRange = smellRange;
	}
	
	@Override
	void update()
	{
		findPotentialPartners();
	}
	
	private void findPotentialPartners()
	{
		potentialPartners = entities.getEntitiesStream().filter(p -> getDistanceToPartner(p) <= smellRange)
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
	
	public Stream<Entity> getPotentialPartners()
	{
		return potentialPartners;
	}
	
	public float getSmellRange()
	{
		return smellRange;
	}
}