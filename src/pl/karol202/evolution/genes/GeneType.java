package pl.karol202.evolution.genes;

public enum GeneType
{
	SIZ(new float[] { 7, 6, 5, 4, 3, 2, 1 });//Size
	
	private float[] propertyIngredients;
	
	GeneType() { }
	
	GeneType(float[] propertyIngredients)
	{
		this.propertyIngredients = propertyIngredients;
	}
	
	public int getLevels()
	{
		if(propertyIngredients == null) return 1;
		else return propertyIngredients.length;
	}
	
	public float[] getPropertyIngredients()
	{
		return propertyIngredients;
	}
	
	public static int getAllGenesCount()
	{
		int count = 0;
		for(GeneType type : values()) count += type.getLevels();
		return count;
	}
}