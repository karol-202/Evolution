package pl.karol202.evolution.simulation;

import java.util.ArrayList;

public class Simulation
{
	public interface OnSimulationStateChangeListener
	{
		void onSimulationStateChanged();
	}
	
	private ArrayList<OnSimulationStateChangeListener> listeners;
	private int timeStep;
	private boolean running;
	private long lastUpdateTime; //MS
	
	public Simulation(int timeStep)
	{
		this.listeners = new ArrayList<>();
		this.timeStep = timeStep;
	}
	
	public void start()
	{
		if(running) return;
		running = true;
		listeners.forEach(OnSimulationStateChangeListener::onSimulationStateChanged);
	}
	
	public void stop()
	{
		if(!running) return;
		running = false;
		listeners.forEach(OnSimulationStateChangeListener::onSimulationStateChanged);
	}
	
	public void step()
	{
		if(running) return;
		update(timeStep);
	}
	
	public void reset()
	{
		running = false;
		listeners.forEach(OnSimulationStateChangeListener::onSimulationStateChanged);
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
	
	private void update(float deltaTime)
	{
		System.out.println("Update, delta time: " + deltaTime);
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
	
	public void addStateListener(OnSimulationStateChangeListener listener)
	{
		listeners.add(listener);
	}
}