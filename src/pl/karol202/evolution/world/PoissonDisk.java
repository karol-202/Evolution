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

import pl.karol202.evolution.utils.Utils;
import pl.karol202.evolution.utils.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PoissonDisk
{
	private static Random random = new Random();
	private static float[][] minDistanceMap;
	private static int width;
	private static int height;
	private static float cellSize;
	private static Cell[][] grid;
	private static ArrayList<Vector2> allPoints;
	private static RandomQueue<Vector2> activePoints;
	
	public static ArrayList<Vector2> poissonDisk(float[][] minDistanceMap, float greatestMinDistance, float k)
	{
		PoissonDisk.minDistanceMap = minDistanceMap;
		width = minDistanceMap.length;
		height = minDistanceMap[0].length;
		cellSize = (float) (greatestMinDistance / Math.sqrt(2));
		createGrid();
		
		allPoints = new ArrayList<>();
		activePoints = new RandomQueue<>();
		
		addPoint(createRandomPoint());
		
		while(!activePoints.isEmpty())
		{
			Vector2 currentPoint = activePoints.poll();
			float minDistance = getMinDistance(currentPoint.getX(), currentPoint.getY());
			for(int i = 0; i < k; i++)
			{
				Vector2 randomPoint = getRandomPointAround(currentPoint, minDistance);
				if(!isInBounds(randomPoint)) continue;
				if(hasNeighbourhood(randomPoint, minDistance)) continue;
				addPoint(randomPoint);
			}
		}
		
		cleanUp();
		return allPoints;
	}
	
	private static void createGrid()
	{
		int gridWidth = (int) Math.ceil(width / cellSize);
		int gridHeight = (int) Math.ceil(height / cellSize);
		grid = new Cell[gridWidth][gridHeight];
		for(int x = 0; x < gridWidth; x++)
			for(int y = 0; y < gridHeight; y++)
				grid[x][y] = new Cell();
	}
	
	private static Vector2 createRandomPoint()
	{
		return new Vector2(random.nextFloat() * width, random.nextFloat() * height);
	}
	
	private static void addPoint(Vector2 point)
	{
		getCellAt(point).addPoint(point);
		allPoints.add(point);
		activePoints.add(point);
	}
	
	private static Cell getCellAt(Vector2 position)
	{
		Point cell = getCellPositionAt(position);
		return grid[(int) cell.getX()][(int) cell.getY()];
	}
	
	private static Point getCellPositionAt(Vector2 position)
	{
		int x = (int) (position.getX() / cellSize);
		int y = (int) (position.getY() / cellSize);
		return new Point(x, y);
	}
	
	private static float getMinDistance(float x, float y)
	{
		int xInt = (int) x;
		int yInt = (int) y;
		float xFract = x - xInt;
		float yFract = y - yInt;
		float leftTop = getMinDistanceFromMapOrClamp(xInt, yInt);
		float rightTop = getMinDistanceFromMapOrClamp(xInt + 1, yInt);
		float leftBottom = getMinDistanceFromMapOrClamp(xInt, yInt + 1);
		float rightBottom = getMinDistanceFromMapOrClamp(xInt + 1, yInt + 1);
		float interpolatedTop = Utils.lerp(xFract, leftTop, rightTop);
		float interpolatedBottom = Utils.lerp(xFract, leftBottom, rightBottom);
		return Utils.lerp(yFract, interpolatedTop, interpolatedBottom);
	}
	
	private static float getMinDistanceFromMapOrClamp(int x, int y)
	{
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		if(x >= width) x = width - 1;
		if(y >= height) y = height - 1;
		return minDistanceMap[x][y];
	}
	
	private static Vector2 getRandomPointAround(Vector2 point, float minDistance)
	{
		float radius = minDistance * (random.nextFloat() + 1);
		float angle = (float) (random.nextFloat() * Math.PI * 2);
		float x = point.getX() + (float) (radius * Math.cos(angle));
		float y = point.getY() + (float) (radius * Math.sin(angle));
		return new Vector2(x, y);
	}
	
	private static boolean isInBounds(Vector2 point)
	{
		return point.getX() >= 0 && point.getY() >= 0 && point.getX() < width && point.getY() < height;
	}
	
	private static boolean hasNeighbourhood(Vector2 position, float minDistance)
	{
		Point cellPosition = getCellPositionAt(position);
		ArrayList<Cell> neighbourCells = getCellsInNeighbourhood(cellPosition);
		for(Cell cell : neighbourCells)
			for(Vector2 point : cell.getPoints())
				if(getDistance(position, point) < minDistance) return true;
		return false;
	}
	
	private static ArrayList<Cell> getCellsInNeighbourhood(Point cellPosition)
	{
		ArrayList<Cell> cells = new ArrayList<>();
		for(int x = cellPosition.x - 2; x <= cellPosition.x + 2; x++)
		{
			if(x < 0) continue;
			if(x >= grid.length) continue;
			
			for(int y = cellPosition.y - 2; y <= cellPosition.y + 2; y++)
			{
				if(y < 0) continue;
				if(y >= grid[0].length) continue;
				cells.add(grid[x][y]);
			}
		}
		return cells;
	}
	
	private static float getDistance(Vector2 first, Vector2 second)
	{
		return first.sub(second).length();
	}
	
	private static void cleanUp()
	{
		minDistanceMap = new float[0][0];
		grid = new Cell[0][0];
		activePoints.clear();
	}
}