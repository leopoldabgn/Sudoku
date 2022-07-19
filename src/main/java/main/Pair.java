package main;

public class Pair
{
	public int x, y;
	
	public Pair(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	Pair add(Pair p)
	{
		return new Pair(p.x + x, p.y + y);
	}
	
}
