package pl.karol202.evolution.simulation;

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
	
	public void reset()
	{
		running = false;
		listeners.forEach(OnSimulationUpdateListener::onSimulationStateChanged);
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
		listeners.forEach(OnSimulationUpdateListener::onSimulationUpdated);
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
	
	public void addStateListener(OnSimulationUpdateListener listener)
	{
		listeners.add(listener);
	}
}