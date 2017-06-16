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

import java.util.Random;

public class Gene
{
	private GeneType type;
	private int level;
	private Allele alleleA;
	private Allele alleleB;
	
	public Gene(GeneType type, int level, Random random)
	{
		this(type, level, Allele.random(random), Allele.random(random));
	}
	
	public Gene(GeneType type, int level, Allele alleleA, Allele alleleB)
	{
		this.type = type;
		this.level = level;
		this.alleleA = alleleA;
		this.alleleB = alleleB;
	}
	
	public Allele getRandomAllele(Random random)
	{
		return random.nextBoolean() ? alleleA : alleleB;
	}
	
	public boolean isCorrespondingGene(Gene gene)
	{
		return type == gene.type && level == gene.level;
	}
	
	public boolean checkGene()
	{
		return alleleA == Allele.DOMINANT || alleleB == Allele.DOMINANT;
	}
	
	@Override
	public String toString()
	{
		return "Gene{" + "type = " + type + ", level = " + level + ", alleleA = " + alleleA + ", alleleB = " + alleleB + '}';
	}
	
	public GeneType getType()
	{
		return type;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public Allele getAlleleA()
	{
		return alleleA;
	}
	
	public Allele getAlleleB()
	{
		return alleleB;
	}
}