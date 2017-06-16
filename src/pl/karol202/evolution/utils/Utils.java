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

public class Utils
{
	public static float map(float src, float srcMin, float srcMax, float dstMin, float dstMax)
	{
		float srcPoint = (src - srcMin) / (srcMax - srcMin);
		return lerp(srcPoint, dstMin, dstMax);
	}
	
	public static float lerp(float value, float v1, float v2)
	{
		return v1 + value * (v2 - v1);
	}
}