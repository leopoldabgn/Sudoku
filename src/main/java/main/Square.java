package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Square extends JPanel
{

	private static final long serialVersionUID = 6662729383768132666L;

	private int nb, max;
	private boolean start, pushed, visible, error, light, indice;
	private transient boolean selected;
	private JLabel lbl;
	
	private static Square actualSquare;
	
	public Square(int nb, int max, boolean visible)
	{
		this.visible = visible;
		this.max = max;
		this.visible = visible;
		this.nb = nb;
		this.setBackground(Color.WHITE);
		this.setLayout(new GridBagLayout());
		lbl = new JLabel(""+nb);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		lbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 44)); // font-size:44px;
		this.add(lbl);
		if(visible)
		{
			start = true;
			pushed = true;
		}
		else
			lbl.setText("");
		addListeners();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		if(selected)
		{
			if(!pushed)
			{
				g2d.setColor(Color.BLUE);
				this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
			}
		}
		
		if(light)
			this.setBackground(Color.CYAN);
		
		if(!start)
			lbl.setForeground(error ? Color.RED : (indice ? new Color(255, 208, 0) : Color.BLUE));
			
		int min = getWidth() < getHeight() ? getWidth() : getHeight();
		lbl.setFont(new Font(Font.SANS_SERIF, Font.BOLD, min/2));
	}
	
	public void addListeners()
	{
		Square self = this;	
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(error)
					return;
				if(actualSquare != null && actualSquare.getLblNumber() != getLblNumber() && actualSquare.isLight())
					resetAllLight(Sudoku.actualSudoku);

				if(pushed)
				{
					resetSquare(actualSquare);
					if(!error)
						self.setAllLight(Sudoku.actualSudoku, nb);
					actualSquare = self;
					repaint();
					return;
				}
				if(selected)
				{
					selected = false;
					error = !checkLbl();
					repaint();
					pushed = true;
					setHide(false);
					actualSquare = null;
					return;
				}
				
				resetSquare(actualSquare);
				actualSquare = self;
				selected = true;
				lbl.setText("1");
				repaint();
			}
		});
		
		this.addMouseWheelListener(new MouseAdapter() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(pushed || !selected)
					return;
				int nb = Integer.parseInt(lbl.getText());
		        if (e.getWheelRotation() < 0)
		        {
		        	if(nb < max)
		        		lbl.setText(""+(++nb));
		        }
		        else
		        {
		            if(nb > 1)
		            {
		            	lbl.setText(""+--nb);
		            }
		        }
		    
			}
		});
	}

	public int getNb()
	{
		return nb;
	}

	public void setSelected(boolean bool)
	{
		this.selected = bool;
	}
	
	public boolean checkLbl()
	{
		if(lbl.getText() == "")
			return false;
		int n = Integer.parseInt(lbl.getText());
		return n == nb;
	}
	
	public int getLblNumber()
	{
		if(lbl.getText().isBlank())
			return 0;
		else
			return Integer.parseInt(lbl.getText());
	}
	
	public void setLbl(String str)
	{
		this.lbl.setText(str);
	}

	public boolean isPushed()
	{
		return pushed;
	}
	
	public void resetSquare(Square sqr)
	{
		if(sqr != null)
		{
			if(!sqr.isPushed())
				sqr.setLbl("");
			sqr.setSelected(false);
			sqr.repaint();
		}
	}
	
	public void setLight(boolean bool)
	{
		this.light = bool;
	}
	
	public boolean isLight()
	{
		return light;
	}
	
	public void resetAllLight(Sudoku sudo)
	{
		Square[][] grid = sudo.getSqrGrid();
		for(Square[] tab : grid)
		{
			for(Square s : tab)
			{
				if(s.isLight())
				{
					s.light = false;
					s.setBackground(Color.WHITE);
					s.repaint();
				}
			}
		}
	}
	
	public void setAllLight(Sudoku sudo, int nb)
	{
		Square[][] grid = sudo.getSqrGrid();
		for(Square[] tab : grid)
		{
			for(Square s : tab)
			{
				if(s.getLblNumber() == nb && !s.error)
				{
					s.light = true;
					s.repaint();
				}
			}
		}
	}
	
	public void setIndice(boolean bool)
	{
		if(!bool)
			return;
		this.indice = true;
		this.pushed = true;
		this.visible = true;
		this.lbl.setText(nb+"");
		this.repaint();
	}
	
	public int getMax()
	{
		return max;
	}
	
	public void setHide(boolean bool)
	{
		this.visible = !bool;
	}
	
	public boolean isHide()
	{
		return !visible;
	}

	public boolean isSelected()
	{
		return selected;
	}

}
