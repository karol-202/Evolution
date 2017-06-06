package pl.karol202.evolution.genes;

import java.util.Random;

public enum Allele
{
	DOMINANT, RECESSIVE;
	
	public static Allele random(Random random)
	{
		return random.nextBoolean() ? DOMINANT : RECESSIVE;
	}
}