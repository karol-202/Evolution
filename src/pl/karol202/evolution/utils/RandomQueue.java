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
	
	public void clear()
	{
		items.clear();
	}
}