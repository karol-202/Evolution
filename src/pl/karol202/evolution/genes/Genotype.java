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
	
	public Gene[] getGenesOfType(GeneType type)
	{
		Gene[] filtered = new Gene[type.getLevels()];
		for(Gene gene : genes) if(gene.getType() == type) filtered[gene.getLevel()] = gene;
		return filtered;
	}
	
	public Gene getGeneOfTypeAndLevel(GeneType type, int level)
	{
		return getGenesOfType(type)[level];
	}
	
	public Gene getGeneByIndex(int index)
	{
		return genes[index];
	}
}