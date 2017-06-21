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

import java.util.ArrayList;
import java.util.Random;

class RandomQueue<T>
{
	private ArrayList<T> items;
	private Random random;
	
	RandomQueue()
	{
		items = new ArrayList<T>();
		random = new Random();
	}
	
	void add(T item)
	{
		int index = random.nextInt(items.size() + 1);
		items.add(index, item);
	}
	
	T poll()
	{
		T item = items.get(items.size() - 1);
		items.remove(item);
		return item;
	}
	
	boolean isEmpty()
	{
		return items.isEmpty();
	}
	
	void clear()
	{
		items.clear();
	}
}