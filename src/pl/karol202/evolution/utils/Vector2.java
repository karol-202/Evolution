package pl.karol202.evolution.utils;

public class Vector2
{
	private float x;
	private float y;
	
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 vector)
	{
		this.x = vector.getX();
		this.y = vector.getY();
	}
	
	public String toString()
	{
		return "(" + x + " " + y + ")";
	}
	
	public float length()
	{
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public float dot(Vector2 r)
	{
		return x * r.getX() + y * r.getY();
	}
	
	public Vector2 normalize()
	{
		float length = length();
		x /= length;
		y /= length;
		return this;
	}
	
	public Vector2 rotate(double angle)
	{
		double rad = Math.toRadians(angle);
		double sin = Math.sin(rad);
		double cos = Math.cos(rad);
		return new Vector2((float)(x * cos - y * sin), (float)(x * sin + y * cos));
	}
	
	public Vector2 add(Vector2 r)
	{
		return new Vector2(x + r.getX(), y + r.getY());
	}
	
	public Vector2 add(float r)
	{
		return new Vector2(x + r, y + r);
	}
	
	public Vector2 sub(Vector2 r)
	{
		return new Vector2(x - r.getX(), y - r.getY());
	}
	
	public Vector2 sub(float r)
	{
		return new Vector2(x - r, y - r);
	}
	
	public Vector2 mul(Vector2 r)
	{
		return new Vector2(x * r.getX(), y * r.getY());
	}
	
	public Vector2 mul(float r)
	{
		return new Vector2(x * r, y * r);
	}
	
	public Vector2 div(Vector2 r)
	{
		return new Vector2(x / r.getX(), y / r.getY());
	}
	
	public Vector2 div(float r)
	{
		return new Vector2(x / r, y / r);
	}
	
	public Vector2 lerp(Vector2 dest, float lerpFactor)
	{
		return dest.sub(this).mul(lerpFactor).add(this);
	}
	
	public float getX()
	{
		return x;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public boolean equals(Vector2 r)
	{
		return x == r.x && y == r.y;
	}
	
	public float cross(Vector2 r)
	{
		return x * r.y - y *  r.x;
	}
}
