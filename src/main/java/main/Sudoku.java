package main;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

public class Sudoku extends JPanel implements Cloneable
{
	private static final long serialVersionUID = 989728228987307283L;
	public static final String LAST_SUDOKU_NAME = "last_sudoku.save";

	private int dim, lvl, difficulty, indices;
	private int[][] grid;
	private Square[][] sqrGrid;
	
	public static Sudoku actualSudoku;
	
	public Sudoku(int lvl, int difficulty)
	{
		Sudoku.actualSudoku = this;
		
		if(lvl < 2)
			lvl = 2;
		else if(lvl > 4)
			lvl = 4;
		if(difficulty > 2)
			difficulty = 2;
		else if(difficulty < 0)
			difficulty = 0;
		
		this.lvl = lvl;
		this.difficulty = difficulty;
		this.indices = 3;
		
		init(lvl, difficulty);
		//printGrid();
		this.setLayout(new GridLayout(lvl, lvl));
	}

	public void init(int n, int diff)
	{
		diff += 2;
		dim = n;
		grid = new int[dim*dim][dim*dim];
		sqrGrid = new Square[dim*dim][dim*dim];
		backtrack(new Pair(0, 0));

		boolean rand;
		
		for(int j=0;j<grid.length;j++)
		{
			for(int i=0;i<grid[j].length;i++)
			{
				rand = (int)(Math.random()*diff) == 0 ? true : false;
				sqrGrid[j][i] = new Square(grid[j][i], lvl*lvl, rand);
			}
		}
		
		for(int j=0;j<dim;j++)
			for(int i=0;i<dim;i++)
				this.add(getBigSquare(j*dim, i*dim));
		
	}
	
	public BigSquare getBigSquare(int x, int y)
	{
		Square[][] tab = new Square[dim][dim];
		for(int j=0;j<dim;j++)
			for(int i=0;i<dim;i++)
				tab[j][i] = sqrGrid[x+j][y+i];
		
		return new BigSquare(tab);
	}
	
	public void backtrack(Pair p)
	{
		backtrack(p, 0);
	}
	
	public boolean backtrack(Pair p, int sol)
	{
		if(solutionComplete(sol))
			return true;
		else
		{
			for(int i : possibles(p))
			{
				sol++;
				grid[p.x][p.y] = i;
				if(backtrack(nextPos(p), sol))
					return true;
				grid[p.x][p.y] = 0;
				sol--;
			}
		}
		return false;
	}
	
	public Pair nextPos(Pair p)
	{
		Pair q = p.add(new Pair(1, 0));
		if(q.x >= dim*dim)
			q = new Pair(0, p.y+1);
		return q;
	}
	
	public ArrayList<Integer> possibles(Pair p)
	{
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for(int i=1;i<=dim*dim;i++)
		{
			if(isValid(p, i))
				numbers.add(i);
		}
		Collections.shuffle(numbers);
		
		return numbers;
	}
	
	public boolean isValid(Pair p, int n)
	{
		Pair q = firstIndexOfSquare(p);
		if(isInSquare(q, n))
			return false;
		for(int i=0;i<dim*dim;i++)
			if(grid[p.x][i] == n || grid[i][p.y] == n)
				return false;
		return true;
	}
	
	public Pair firstIndexOfSquare(Pair p)
	{
		return new Pair((p.x/dim)*dim, (p.y/dim)*dim);
	}
	
	public boolean isInSquare(Pair p, int n)
	{
		for(int j=p.x;j<p.x+dim;j++)
			for(int i=p.y;i<p.y+dim;i++)
				if(grid[j][i] == n)
					return true;
		return false;
	}
	
	public boolean solutionComplete(int sol)
	{
		return sol == dim*dim*dim*dim;
	}
	
	public void printGrid()
	{
		for(int j=0;j<dim*dim;j++)
		{
			for(int i=0;i<dim*dim;i++)
				System.out.print(grid[j][i]+" ");
			System.out.println();
		}
		System.out.println();
	}
	
	public ArrayList<Square> getHideList()
	{
		ArrayList<Square> l = new ArrayList<Square>();
		for(Square[] tab : sqrGrid)
			for(Square s : tab)
				if(s.isHide())
					l.add(s);
		return l;
	}
	
	public void revealIndice()
	{
		if(indices <= 0)
			return;
		ArrayList<Square> l = getHideList();
		if(l.isEmpty())
			return;
		Collections.shuffle(l);
		indices--;
		l.get(0).setIndice(true);
	}
	
	public void addListeners()
	{
		for(Square[] sqrTab : sqrGrid)
			for(Square s : sqrTab)
				s.addListeners();
	}

	public void save(String name)
	{
		Sudoku sudoku = null;
		try {
			sudoku = (Sudoku)this.clone();
			Square sqr = null;
			for(int j=0;j<sqrGrid.length;j++)
			{
				for(int i=0;i<sqrGrid[j].length;i++)
				{
					sqr = sqrGrid[j][i];
					if(sqr.isLight())
					{
						sqr.resetAllLight(sudoku);
						j = sqrGrid.length;
						break;
					}
					else if(sqr.isSelected() && !sqr.isPushed())
					{
						sqr.resetSquare(sqr);
						j = sqrGrid.length;
						break;
					}
				}
			}

		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
		ObjectOutputStream oos = null;
		File file = new File(name);
		try {
			oos =  new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(sudoku);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();	
		}
	}
	
	public static Sudoku load(String name)
	{
		Sudoku sudoku = null;
		ObjectInputStream ois = null;
		File file = new File(name);
		if(file.exists())
		{
			try {
				ois =  new ObjectInputStream(new FileInputStream(file)) ;
				sudoku = (Sudoku)ois.readObject();
				ois.close();
				sudoku.addListeners();
				Sudoku.actualSudoku = sudoku;
			} catch (NullPointerException | IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}	
		}
		
		return sudoku;
	}

	public void saveLastSudoku()
	{
		save(LAST_SUDOKU_NAME);
	}

	public static Sudoku loadLastSudoku()
	{
		return load(LAST_SUDOKU_NAME);
	}
	
	public Square[][] getSqrGrid()
	{
		return sqrGrid;
	}
	
	public int nbIndices()
	{
		return indices;
	}
	
	public int getDifficulty()
	{
		return difficulty;
	}
	
}
