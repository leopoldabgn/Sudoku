package main;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

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
	
	private Sudoku sudoku;
	private JToolBar toolBar = new JToolBar();
	
	public Window(int w, int h)
	{
		this.setTitle("Sudoku");
		this.setSize(w, h);
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(true);
		//this.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		//this.setLayout(new GridBagLayout());
		
		initToolBar();
		
		sudoku = Sudoku.loadLastSudoku();
		
		if(sudoku == null)
			sudoku = new Sudoku(3, 1);

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				sudoku.saveLastSudoku();
			}
			
		});

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
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("icons/reset.png")));
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
	                    "Difficult� ?", "Difficulté",
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
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getClassLoader().getResource("icons/lamp2.jpg")));
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
	
	public static Image getImage(final String pathAndFileName) {
		final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
		return Toolkit.getDefaultToolkit().getImage(url);
	}

}
