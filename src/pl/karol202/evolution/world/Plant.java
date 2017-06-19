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
package pl.karol202.evolution.world;

import pl.karol202.evolution.simulation.Simulation;
import pl.karol202.evolution.utils.Vector2;

public class Plant
{
	private static final float REGENERATION_SPEED = 1.5f;
	
	private float x;
	private float y;
	private float health;
	
	public Plant(Vector2 vector)
	{
		this(vector.getX(), vector.getY());
	}
	
	public Plant(float x, float y)
	{
		this(x, y, 100);
	}
	
	public Plant(float x, float y, float health)
	{
		this.x = x;
		this.y = y;
		this.health = health;
	}
	
	public void update()
	{
		if(health >= 100) return;
		health += REGENERATION_SPEED * Simulation.deltaTime;
		if(health > 100) health = 100;
 	}
	
 	public void reduceHealth(float amount)
    {
    	health -= amount;
    	if(health < 0) health = 0;
    }
 	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getHealth()
	{
		return health;
	}
}