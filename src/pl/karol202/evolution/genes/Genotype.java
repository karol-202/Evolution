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

public class Genotype
{
	private Random random;
	private Gene[] genes;
	
	public Genotype(Random random)
	{
		this.random = random;
		genes = new Gene[GeneType.getAllGenesCount()];
		createRandomGenes();
	}
	
	public Genotype(Random random, Genotype genotypeA, Genotype genotypeB)
	{
		this.random = random;
		genes = new Gene[GeneType.getAllGenesCount()];
		inheritGenes(genotypeA, genotypeB);
	}
	
	private void createRandomGenes()
	{
		int id = 0;
		for(GeneType type : GeneType.values())
			for(int level = 0; level < type.getLevels(); level++)
				genes[id++] = new Gene(type, level, random);
	}
	
	private void inheritGenes(Genotype genotypeA, Genotype genotypeB)
	{
		for(int i = 0; i < genes.length; i++)
		{
			Gene geneA = genotypeA.genes[i];
			Gene geneB = genotypeB.genes[i];
			if(!geneA.isCorrespondingGene(geneB))
				throw new RuntimeException("Genes mismatch: " + geneA.toString() + ", " + geneB.toString());
			
			Allele alleleA = geneA.getRandomAllele(random);
			Allele alleleB = geneB.getRandomAllele(random);
			Gene newGene = new Gene(geneA.getType(), geneA.getLevel(), alleleA, alleleB);
			genes[i] = newGene;
		}
	}
	
	public float getFloatProperty(GeneType type)
	{
		if(type.getLevels() <= 1) throw new RuntimeException(type + " is not a float gene.");
		Gene[] genes = getGenesOfType(type);
		float property = 0;
		for(Gene gene : genes)
			if(gene.checkGene()) property += type.getPropertyIngredients()[gene.getLevel()];
		return property;
	}
	
	public boolean getBooleanProperty(GeneType type)
	{
		if(type.getLevels() != 1) throw new RuntimeException(type + " is not a boolean gene.");
		Gene gene = getGenesOfType(type)[0];
		return gene.checkGene();
	}
	
	private Gene[] getGenesOfType(GeneType type)
	{
		Gene[] filtered = new Gene[type.getLevels()];
		for(Gene gene : genes) if(gene.getType() == type) filtered[gene.getLevel()] = gene;
		return filtered;
	}
	
	public Gene getGeneOfTypeAndLevel(GeneType type, int level)
	{
		return getGenesOfType(type)[level];
	}
	
	void setGene(Gene gene)
	{
		Gene existingGene = getGeneOfTypeAndLevel(gene.getType(), gene.getLevel());
		existingGene.setAlleleA(gene.getAlleleA());
		existingGene.setAlleleB(gene.getAlleleB());
	}
}