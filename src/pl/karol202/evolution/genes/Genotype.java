package pl.karol202.evolution.genes;

import java.util.Random;

public class Genotype
{
	private Gene[] genes;
	private Random random;
	private boolean empty;
	
	public Genotype(Random random)
	{
		this.random = random;
		genes = new Gene[GeneType.getAllGenesCount()];
		empty = true;
	}
	
	public void createRandomGenes()
	{
		int id = 0;
		for(GeneType type : GeneType.values())
			for(int level = 0; level < type.getLevels(); level++)
				genes[id++] = new Gene(type, level, random);
		empty = false;
	}
	
	public void inheritGenes(Genotype genotypeA, Genotype genotypeB)
	{
		if(genotypeA.empty || genotypeB.empty) throw new RuntimeException("Empty genotype");
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
		empty = false;
	}
}