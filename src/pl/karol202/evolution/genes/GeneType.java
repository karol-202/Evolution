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
	MSX(Allele.RECESSIVE), //Sex
	MSZ(new float[] { 7, 6, 5, 4, 3, 2, 1 }),//Size
	MSP(new float[] { 25, 25, 20, 15, 15, 12, 12, 10 }),//Speed
	EMX(new float[] { 30, 30, 25, 25, 20, 20, 15, 15, 10, 10, 5, 5 }),//Max energy
	EPS(new float[] { 1f, 1f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.25f, 0.25f }), //Energy usage per second
	FSP(new float[] { 3, 3, 3, 2, 2, 1, 1, 1 }), //Eating speed
	CSR(new float[] { 20, 20, 15, 15, 15, 15, 10, 10, 10, 10, 10, 10, 8, 8, 8, 5, 5, 5, 1 }), //Sight range
	BFS(new float[] { 0.1f, 0.1f, 0.05f, 0.05f, 0.05f, 0.05f, 0.04f, 0.03f, 0.03f }); //Eat start energy range
	
	private float[] propertyIngredients;
	private Allele constantAllele;
	
	GeneType() { }
	
	GeneType(Allele constantAllele)
	{
		this.constantAllele = constantAllele;
	}
	
	GeneType(float[] propertyIngredients)
	{
		this.propertyIngredients = propertyIngredients;
	}
	
	public int getLevels()
	{
		if(propertyIngredients == null) return 1;
		else return propertyIngredients.length;
	}
	
	public boolean hasConstantAllele()
	{
		return constantAllele != null;
	}
	
	public float[] getPropertyIngredients()
	{
		return propertyIngredients;
	}
	
	public Allele getConstantAllele()
	{
		return constantAllele;
	}
	
	public static int getAllGenesCount()
	{
		int count = 0;
		for(GeneType type : values()) count += type.getLevels();
		return count;
	}
	
	public static GeneType getTypeByName(String name)
	{
		for(GeneType type : values())
			if(type.name().equals(name)) return type;
		return null;
	}
}