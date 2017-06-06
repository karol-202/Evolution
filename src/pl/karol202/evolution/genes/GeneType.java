package pl.karol202.evolution.genes;

public enum GeneType
{
	SIZ(3);//Size
	
	private int levels;
	
	GeneType(int levels)
	{
		this.levels = levels;
	}
	
	public int getLevels()
	{
		return levels;
	}
	
	public static int getAllGenesCount()
	{
		int count = 0;
		for(GeneType type : values()) count += type.levels;
		return count;
	}
}