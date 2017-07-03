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
package pl.karol202.evolution.simulation;

import pl.karol202.evolution.stats.Stats;
import pl.karol202.evolution.world.World;

import java.util.ArrayList;

public class Simulation
{
	public interface OnSimulationUpdateListener
	{
		void onSimulationUpdated();
		
		void onSimulationStateChanged();
	}
	
	public static float deltaTime;
	
	private ArrayList<OnSimulationUpdateListener> listeners;
	private World world;
	private int timeStep;
	private boolean running;
	private long lastUpdateTime; //MS
	
	public Simulation(World world, int timeStep)
	{
		this.listeners = new ArrayList<>();
		this.world = world;
		this.timeStep = timeStep;
		
		Stats.instance.resetStats(world);
	}
	
	public void start()
	{
		if(running) return;
		running = true;
		listeners.forEach(OnSimulationUpdateListener::onSimulationStateChanged);
		lastUpdateTime = System.currentTimeMillis();
	}
	
	public void stop()
	{
		if(!running) return;
		running = false;
		listeners.forEach(OnSimulationUpdateListener::onSimulationStateChanged);
	}
	
	public void step()
	{
		if(running) return;
		update(timeStep);
	}
	
	void reset()
	{
		running = false;
		Stats.instance.resetStats(world);
		notifyChange();
	}
	
	public void mainLoop()
	{
		if(canUpdate()) updateInLoop();
	}
	
	private boolean canUpdate()
	{
		return running && lastUpdateTime + timeStep < System.currentTimeMillis();
	}
	
	private void updateInLoop()
	{
		long deltaTime = System.currentTimeMillis() - lastUpdateTime;
		lastUpdateTime = System.currentTimeMillis();
		update(deltaTime);
	}
	
	private void update(long deltaTime)
	{
		Simulation.deltaTime = deltaTime / 1000f;
		world.update();
		Stats.instance.update();
		listeners.forEach(OnSimulationUpdateListener::onSimulationUpdated);
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public int getTimeStep()
	{
		return timeStep;
	}
	
	public void setTimeStep(int timeStep)
	{
		if(running) return;
		this.timeStep = timeStep;
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public void addListener(OnSimulationUpdateListener listener)
	{
		listeners.add(listener);
	}
	
	void notifyChange()
	{
		listeners.forEach(OnSimulationUpdateListener::onSimulationStateChanged);
		listeners.forEach(OnSimulationUpdateListener::onSimulationUpdated);
	}
}