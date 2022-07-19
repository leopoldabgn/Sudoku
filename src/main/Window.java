package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

public class Window extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private Sudoku sudoku = new Sudoku(3, 1);
	private JToolBar toolBar = new JToolBar();
	
	public Window(int w, int h)
	{
		super();
		this.setTitle("Sudoku");
		this.setSize(w, h);
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(true);
		//this.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		//this.setLayout(new GridBagLayout());
		
		initToolBar();
		
		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		this.getContentPane().add(sudoku);
		this.setVisible(true);
	}
	
	public void initToolBar()
	{
		toolBar.add(reset);
		toolBar.add(indice);
	}
	
    private AbstractAction reset = new AbstractAction() 
    {  
		private static final long serialVersionUID = 1L;
		{
            putValue(Action.NAME, "Refresh");
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("reset.png")));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
            putValue(Action.SHORT_DESCRIPTION, "Reset (CTRL+R)");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK)); 
        }
        
        @Override public void actionPerformed( ActionEvent e ) 
        {
            System.out.println("Reset...");
            int rep = 1, rep2 = -1;
            String[] options;
            while(rep2 == -1)
            {
            	options = new String[] {"2x2", "3x3", "4x4"};
            	rep = JOptionPane.showOptionDialog(null,
	                    "Nouvelle Partie ?", "Indices",
	                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
	            if(rep < 0 || rep > 2)
	            	return;
	            rep2 =  JOptionPane.showOptionDialog(null,
	                    "Difficulté ?", "Difficulté",
	                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] {"1", "2", "3"}, null);
            }
            remove(sudoku);
            sudoku = new Sudoku(rep+2, rep2);
            add(sudoku);
            System.out.println("Nouveau Sudoku : ");
            System.out.println("Niveau="+rep+"\nDifficulté="+rep2);
        }
    };
    
    private AbstractAction indice = new AbstractAction() 
    {  
		private static final long serialVersionUID = 1L;
		{
            putValue(Action.NAME, "Indice");
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("lamp2.jpg")));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
            putValue(Action.SHORT_DESCRIPTION, "Indice (CTRL+I)");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK)); 
        }
        
        @Override public void actionPerformed( ActionEvent e ) 
        {
            System.out.println("Indice...");
            int rep = JOptionPane.showOptionDialog(null,
                    "Il vous reste "+sudoku.nbIndices()+" indices. Voulez-vous en débloquer un ?", "Indices",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] {"Oui", "Non"}, null);
            if(rep == JOptionPane.YES_OPTION)
            	sudoku.revealIndice();
        }
    };
	
}
