package pl.karol202.evolution.utils;

import java.util.ArrayList;

public class Cell
{
	private ArrayList<Vector2> points;
	
	public Cell()
	{
		this.points = new ArrayList<>();
	}
	
	public void addPoint(Vector2 point)
	{
		points.add(point);
	}
	
	public ArrayList<Vector2> getPoints()
	{
		return points;
	}
}