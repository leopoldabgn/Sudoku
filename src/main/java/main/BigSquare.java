package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BigSquare extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private Square[][] squares;
	
	public BigSquare(Square[][] squares)
	{
		super();
		this.squares = squares;
		int s = (int)squares[0][0].getPreferredSize().getWidth()*squares.length;
		this.setPreferredSize(new Dimension(s, s));
		this.setLayout(new GridLayout(squares.length, squares[0].length));
		for(int j=0;j<squares.length;j++)
			for(int i=0;i<squares[j].length;i++)
				this.add(squares[j][i]);
		this.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		this.setBackground(Color.BLACK);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(10));
		g2d.drawRect(0, 0, getWidth(), getHeight());
	}
	
	public Square[][] getSquares()
	{
		return squares;
	}
	
}
