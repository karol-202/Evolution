package pl.karol202.evolution.entity;

import pl.karol202.evolution.genes.Allele;
import pl.karol202.evolution.genes.Gene;
import pl.karol202.evolution.genes.GeneType;
import pl.karol202.evolution.genes.Genotype;

import java.util.Random;

public class Entity
{
	private float x;
	private float y;
	
	private Genotype genotype;
	private float size;
	
	private Entity(float x, float y, Genotype genotype)
	{
		this.x = x;
		this.y = y;
		this.genotype = genotype;
		setProperties();
	}
	
	private void setProperties()
	{
		size = getFloatProperty(GeneType.SIZ);
	}
	
	private float getFloatProperty(GeneType type)
	{
		if(type.getLevels() <= 1) throw new RuntimeException(type + " is not a float gene.");
		Gene[] genes = genotype.getGenesOfType(type);
		float property = 0;
		for(Gene gene : genes)
			if(checkGene(gene)) property += type.getPropertyIngredients()[gene.getLevel()];
		return property;
	}
	
	private boolean getBooleanProperty(GeneType type)
	{
		if(type.getLevels() != 1) throw new RuntimeException(type + " is not a boolean gene.");
		Gene gene = genotype.getGenesOfType(type)[0];
		return checkGene(gene);
	}
	
	private boolean checkGene(Gene gene)
	{
		return gene.getAlleleA() == Allele.DOMINANT || gene.getAlleleB() == Allele.DOMINANT;
	}
	
	public static Entity createRandomEntity(float x, float y, Random random)
	{
		Genotype genotype = new Genotype(random);
		return new Entity(x, y, genotype);
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public Genotype getGenotype()
	{
		return genotype;
	}
	
	public float getSize()
	{
		return size;
	}
}