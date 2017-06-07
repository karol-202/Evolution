package pl.karol202.evolution.utils;

public class Utils
{
	public static float map(float src, float srcMin, float srcMax, float dstMin, float dstMax)
	{
		float srcPoint = (src - srcMin) / (srcMax - srcMin);
		return lerp(srcPoint, dstMin, dstMax);
	}
	
	public static float lerp(float value, float v1, float v2)
	{
		return v1 + value * (v2 - v1);
	}
}