package pl.karol202.evolution.utils;

public class OctaveSimplexNoise
{
	public static double noise(double x, double y, double[] octaves)
	{
		double noiseSum = 0;
		double scale = 1;
		for(double octave : octaves)
		{
			double noise = SimplexNoise.noise(x * scale, y * scale);
			noiseSum += noise * octave;
			scale *= 2;
		}
		return noiseSum;
	}
}