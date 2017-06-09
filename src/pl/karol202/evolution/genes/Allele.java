package pl.karol202.evolution.genes;

import java.util.Random;

public enum Allele
{
	DOMINANT("Dominujący"),
	RECESSIVE("Recesywny");
	
	private String name;
	
	Allele(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	public static Allele random(Random random)
	{
		return random.nextBoolean() ? DOMINANT : RECESSIVE;
	}
}