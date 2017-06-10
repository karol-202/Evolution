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