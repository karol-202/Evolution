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

import java.awt.*;
import java.util.HashMap;

public class Gradient
{
	private HashMap<Float, Color> colors;
	private float smallestPos;
	private float largestPos;
	
	public Gradient()
	{
		colors = new HashMap<>();
	}
	
	public void addColor(Color color, float position)
	{
		colors.put(position, color);
		if(position < smallestPos) smallestPos = position;
		if(position > largestPos) largestPos = position;
	}
	
	public Color getColorAtPosition(float position)
	{
		float earlier = smallestPos;
		float later = largestPos;
		for(Float f : colors.keySet())
		{
			if(f > earlier && f < position) earlier = f;
			if(f < later && f > position) later = f;
		}
		
		Color earlierColor = colors.get(earlier);
		Color laterColor = colors.get(later);
		int r = (int) Utils.map(position, earlier, later, earlierColor.getRed(), laterColor.getRed());
		int g = (int) Utils.map(position, earlier, later, earlierColor.getGreen(), laterColor.getGreen());
		int b = (int) Utils.map(position, earlier, later, earlierColor.getBlue(), laterColor.getBlue());
		return new Color(r, g, b);
	}
}