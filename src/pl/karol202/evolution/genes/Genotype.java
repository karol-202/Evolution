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
	
	public Gene[] getGenes()
	{
		Gene[] copy = new Gene[genes.length];
		System.arraycopy(genes, 0, copy, 0, genes.length);
		return copy;
	}
}