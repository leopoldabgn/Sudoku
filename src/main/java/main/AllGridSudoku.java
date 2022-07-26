package main;

import java.util.ArrayList;

public class AllGridSudoku
{
	
	private static int dim, dimTotale;
	private static int[][] grille;
	private static int sol;
	
	public static void init(int n)
	{
		dim = n;
		dimTotale = n*n;
		grille = new int[dimTotale][dimTotale];
	}
	
	public static void main(String[] args)
	{
		init(3);
		backtrack(new Pair(0, 0));
	}

	public static void backtrack(Pair p)
	{
		if(solutionComplete())
			printGrid();
		else
		{
			for(int i : possibles(p))
			{
				//System.out.println(p.x+" "+p.y);
				//printGrid();
				sol++;
				grille[p.x][p.y] = i;
				backtrack(nextPos(p));
				grille[p.x][p.y] = 0;
				sol--;
			}
		}
	}
	
	public static Pair nextPos(Pair p)
	{
		Pair q = p.add(new Pair(1, 0));
		if(q.x >= dimTotale)
			q = new Pair(0, p.y+1);
		return q;
	}
	
	public static ArrayList<Integer> possibles(Pair p)
	{
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for(int i=1;i<=dimTotale;i++)
		{
			if(isValid(p, i))
				numbers.add(i);
		}
		
		return numbers;
	}
	
	public static boolean isValid(Pair p, int n)
	{
		Pair q = firstIndexOfSquare(p);
		if(isInSquare(q, n))
			return false;
		for(int i=0;i<dimTotale;i++)
			if(grille[p.x][i] == n || grille[i][p.y] == n)
				return false;
		return true;
	}
	
	public static Pair firstIndexOfSquare(Pair p)
	{
		return new Pair((p.x/dim)*dim, (p.y/dim)*dim);
	}
	
	public static boolean isInSquare(Pair p, int n)
	{
		for(int j=p.x;j<p.x+dim;j++)
			for(int i=p.y;i<p.y+dim;i++)
				if(grille[j][i] == n)
					return true;
		return false;
	}
	
	public static boolean solutionComplete()
	{
		return sol == dimTotale*dimTotale;
	}
	
	public static void printGrid()
	{
		for(int j=0;j<dimTotale;j++)
		{
			for(int i=0;i<dimTotale;i++)
				System.out.print(grille[i][j]+" ");
			System.out.println();
		}
		System.out.println();
	}
	
}
