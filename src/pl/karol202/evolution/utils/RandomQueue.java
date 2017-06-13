package pl.karol202.evolution.utils;

import java.util.ArrayList;
import java.util.Random;

public class RandomQueue<T>
{
	private ArrayList<T> items;
	private Random random;
	
	public RandomQueue()
	{
		items = new ArrayList<T>();
		random = new Random();
	}
	
	public void add(T item)
	{
		int index = random.nextInt(items.size() + 1);
		items.add(index, item);
	}
	
	public T poll()
	{
		T item = items.get(items.size() - 1);
		items.remove(item);
		return item;
	}
	
	public boolean isEmpty()
	{
		return items.isEmpty();
	}
}