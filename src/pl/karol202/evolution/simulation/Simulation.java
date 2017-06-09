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
	private int time; //Ms
	
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
		System.out.println("start");
	}
	
	public void stop()
	{
		if(!running) return;
		running = false;
		listeners.forEach(OnSimulationStateChangeListener::onSimulationStateChanged);
		System.out.println("stop");
	}
	
	public void step()
	{
		if(running) return;
		time += timeStep;
		update();
	}
	
	private void update()
	{
		System.out.println("update");
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