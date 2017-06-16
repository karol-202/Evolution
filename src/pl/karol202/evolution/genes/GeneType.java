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
package pl.karol202.evolution.genes;

public enum GeneType
{
	SIZ(new float[] { 7, 6, 5, 4, 3, 2, 1 }),//Size
	SPD(new float[] { 25, 25, 20, 15, 15, 12, 12, 10 }),//Speed
	EMX(new float[] { 30, 30, 25, 25, 20, 20, 15, 15, 10, 10, 5, 5 }),//Max energy
	EPS(new float[] { 1f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.25f, 0.25f }); //Energy usage per second
	
	private float[] propertyIngredients;
	
	GeneType() { }
	
	GeneType(float[] propertyIngredients)
	{
		this.propertyIngredients = propertyIngredients;
	}
	
	public int getLevels()
	{
		if(propertyIngredients == null) return 1;
		else return propertyIngredients.length;
	}
	
	public float[] getPropertyIngredients()
	{
		return propertyIngredients;
	}
	
	public static int getAllGenesCount()
	{
		int count = 0;
		for(GeneType type : values()) count += type.getLevels();
		return count;
	}
}